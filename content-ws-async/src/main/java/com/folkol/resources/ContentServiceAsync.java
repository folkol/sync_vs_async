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
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import rx.Observable;

@Path("content")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContentServiceAsync {
    private final Bucket bucket;

    public ContentServiceAsync(Bucket bucket) {
        this.bucket = bucket;
    }

    @GET
    @Path("{id}")
    public void get(@Suspended AsyncResponse ar, @PathParam("id") String id) {
        Content content = new Content();
        content.setId(id);

        var parts = content.getParts();
        bucket.async().get(id).flatMap(doc -> {
            JsonObject json = doc.content();
            String description = json.getString("description");
            content.setDescription(description);
            JsonArray partsJson = json.getArray("parts");
            return Observable.from(partsJson)
                             .flatMap(o -> {
                                 String key = (String) o;
                                 return bucket.async().get(key);
                             });
        }).subscribe(partDoc -> {
                         Map<String, String> part = new HashMap<>();
                         JsonObject partJson = partDoc.content();
                         for (String name : partJson.getNames()) {
                             part.put(name, partJson.getString(name));
                         }
                         String partName = partDoc.id().substring(id.length());
                         parts.put(partName, part);
                     },
                     ar::resume,
                     () -> ar.resume(content));
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
