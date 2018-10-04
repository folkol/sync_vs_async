package com.folkol;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.folkol.resources.ContentServiceAsync;
import com.folkol.resources.ContentServiceSync;
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
        Cluster cluster = CouchbaseCluster.create();
        Bucket bucket = cluster.openBucket(BUCKET, PASSWORD);
        environment.jersey().register(new ContentServiceSync(bucket));
        environment.jersey().register(new ContentServiceAsync(bucket));
        environment.healthChecks().register("dummy", new DummyHealthCheck());
    }

}
