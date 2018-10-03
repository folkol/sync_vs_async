package com.folkol;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.folkol.resources.ContentServiceSync;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ContentApplicationSync extends Application<ContentConfigurationSync> {

    private static final String BUCKET = "content";
    private static final String PASSWORD = "content";

    public static void main(final String[] args) throws Exception {
        new ContentApplicationSync().run(args);
    }

    @Override
    public String getName() {
        return "SyncContent";
    }

    @Override
    public void initialize(final Bootstrap<ContentConfigurationSync> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final ContentConfigurationSync configuration,
                    final Environment environment)
    {
        Cluster cluster = CouchbaseCluster.create();
        Bucket bucket = cluster.openBucket(BUCKET, PASSWORD);
        environment.jersey().register(new ContentServiceSync(bucket));
        environment.healthChecks().register("dummy", new DummyHealthCheck());
    }

}
