package com.folkol.resources;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.folkol.model.Content;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("sync")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContentServiceSync {
    private final Bucket bucket;

    public ContentServiceSync(Bucket bucket) {
        this.bucket = bucket;
    }

    @GET
    @Path("{id}")
    public Content get(@PathParam("id") String id) {
        JsonObject json = bucket.get(id).content();
        String description = json.getString("description");

        Content content = new Content();
        content.setId(id);
        content.setDescription(description);
        var parts = content.getParts();

        JsonArray partsJson = json.getArray("parts");
        for (int i = 0; i < partsJson.size(); i++) {
            Map<String, String> part = new HashMap<>();
            String key = partsJson.getString(i);
            JsonObject partJson = bucket.get(key).content();
            for (String name : partJson.getNames()) {
                part.put(name, partJson.getString(name));
            }
            String partName = key.substring(id.length());
            parts.put(partName, part);
        }
        return content;
    }

    @PUT
    @Path("{id}")
    public void put(@PathParam("id") String id, Content content) {
        JsonArray parts = JsonArray.create();
        for (var entry : content.getParts().entrySet()) {
            JsonObject part = JsonObject.create();
            entry.getValue().forEach(part::put);
            bucket.upsert(JsonDocument.create(id + entry.getKey(), part));
            parts.add(id + entry.getKey());
        }
        JsonObject json =
            JsonObject.create()
                      .put("description", content.getDescription())
                      .put("parts", parts);
        bucket.upsert(JsonDocument.create(id, json));
    }
}
