package com.folkol;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.folkol.resources.ContentServiceAsync;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ContentApplicationAsync extends Application<ContentConfigurationAsync> {
    private static final String BUCKET = "content";
    private static final String PASSWORD = "content";

    public static void main(final String[] args) throws Exception {
        new ContentApplicationAsync().run(args);
    }

    @Override
    public String getName() {
        return "AsyncContent";
    }

    @Override
    public void initialize(final Bootstrap<ContentConfigurationAsync> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final ContentConfigurationAsync configuration,
                    final Environment environment)
    {
        Cluster cluster = CouchbaseCluster.create();
        Bucket bucket = cluster.openBucket(BUCKET, PASSWORD);
        environment.jersey().register(new ContentServiceAsync(bucket));
        environment.healthChecks().register("dummy", new DummyHealthCheck());
    }

}
