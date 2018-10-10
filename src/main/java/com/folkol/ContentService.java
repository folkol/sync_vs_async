package com.folkol;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.folkol.model.Content;
import java.util.HashMap;
import java.util.Map;
import rx.Observable;

public class ContentService {
    private final Bucket bucket;

    public ContentService(Bucket bucket) {
        this.bucket = bucket;
    }

    public void updateContent(String id, Content content) {
        JsonArray parts = writeParts(id, content);
        JsonObject json =
            JsonObject.create()
                      .put("description", content.getDescription())
                      .put("parts", parts);
        bucket.upsert(JsonDocument.create(id, json));
    }

    public Content getContentSync(String id) {
        JsonObject json = bucket.get(id).content();
        String description = json.getString("description");
        var parts = loadPartsSync(id, json.getArray("parts"));

        return new Content(id, description, parts);
    }

    public Observable<Content> getContentAsync(String id) {
        return bucket.async().get(id)
                     .map(JsonDocument::content)
                     .flatMap(json -> {
                         Content content = new Content();
                         content.setId(id);
                         content.setDescription(json.getString("description"));
                         return loadPartsAsync(id, json.getArray("parts"))
                                    .map(parts -> {
                                        content.setParts(parts);
                                        return content;
                                    });
                     });
    }

    private JsonArray writeParts(String id, Content content) {
        JsonArray parts = JsonArray.create();
        content.getParts().forEach((name, part) -> {
            bucket.upsert(JsonDocument.create(id + name, JsonObject.from(part)));
            parts.add(name);
        });
        return parts;
    }

    private Map<String, Map<String, Object>> loadPartsSync(String id, JsonArray names) {
        Map<String, Map<String, Object>> parts = new HashMap<>();
        names.forEach(e -> {
            String name = (String) e;
            JsonObject json = bucket.get(id + name).content();
            parts.put(name, json.toMap());
        });
        return parts;
    }

    private Observable<Map<String, Map<String, Object>>> loadPartsAsync(String id, JsonArray json) {
        return Observable.from(json)
                         .flatMap(e -> {
                             String key = (String) e;
                             return bucket.async().get(key);
                         })
                         .toMap(doc -> doc.id().substring(id.length()))
                         .map(docs -> {
                             Map<String, Map<String, Object>> parts = new HashMap<>();
                             docs.forEach((s, jsonDocument) -> {
                                 Map<String, Object> part = new HashMap<>();
                                 JsonObject partJson = jsonDocument.content();
                                 for (String name : partJson.getNames()) {
                                     part.put(name, partJson.getString(name));
                                 }
                                 parts.put(s, part);
                             });
                             return parts;
                         });
    }
}