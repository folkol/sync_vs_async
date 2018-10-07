package com.folkol;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.folkol.resources.ContentResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ContentApplication extends Application<ContentConfiguration> {

    private static final String BUCKET = "content";
    private static final String PASSWORD = "content";

    public static void main(final String[] args) throws Exception {
        new ContentApplication().run(args);
    }

    @Override
    public String getName() {
        return "Content";
    }

    @Override
    public void initialize(final Bootstrap<ContentConfiguration> bootstrap) {
    }

    @Override
    public void run(final ContentConfiguration configuration,
                    final Environment environment)
    {
//        Cluster cluster = CouchbaseCluster.create("192.168.1.120");
        Cluster cluster = CouchbaseCluster.create();
        Bucket bucket = cluster.openBucket(BUCKET, PASSWORD);
        ContentService contentService = new ContentService(bucket);
        environment.jersey().register(new ContentResource(contentService));
        environment.healthChecks().register("dummy", new DummyHealthCheck());
    }

}
