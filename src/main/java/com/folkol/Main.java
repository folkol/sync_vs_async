package com.folkol;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.ReplicaMode;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import java.util.concurrent.TimeUnit;

public class Main {

    public static final String BUCKET = "content";
    public static final String PASSWORD = "content";

    public static void main(String[] args) {
        Cluster cluster = CouchbaseCluster.create();

        Bucket bucket = cluster.openBucket(BUCKET, PASSWORD);

        JsonObject user = JsonObject.empty()
                                    .put("firstname", "Walter")
                                    .put("lastname", "White")
                                    .put("job", "chemistry teacher")
                                    .put("age", 50);
        JsonDocument stored = bucket.upsert(JsonDocument.create("walter", user));

        JsonDocument walter = bucket.get("walter");
        System.out.println("Found: " + walter.content().getString("firstname"));

        bucket
            .async()
            .get("beer")
            .onErrorResumeNext(bucket.async().getFromReplica("beer", ReplicaMode.ALL))
            .first()
            .map(doc -> doc.content().getString("name"))
            .timeout(2, TimeUnit.SECONDS)
            .doOnError(System.err::println)
            .onErrorReturn(error -> "Not Found!");

//        ViewResult result = bucket.query(ViewQuery.from("beers_and_breweries", "by_name"));
//
//        for (ViewRow row : result) {
//            JsonDocument doc = row.document();
//
//            if (doc.content().getString("type").equals("beer")) {
//                System.out.println(doc.content().getString("name"));
//            }
//        }
//
//        bucket
//            .async()
//            .query(ViewQuery.from("beers_and_breweries", "by_name"))
//            .flatMap(AsyncViewResult::rows)
//            .flatMap(AsyncViewRow::document)
//            .filter(doc -> doc.content().getString("type").equals("beer"))
//            .subscribe(doc -> System.out.println(doc.content().getString("name")));

        cluster.disconnect();
    }
}
