package com.folkol;

import com.folkol.resources.ContentServiceSync;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ContentApplicationSync extends Application<ContentConfigurationSync> {

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
        environment.jersey().register(new ContentServiceSync());
        environment.healthChecks().register("dummy", new DummyHealthCheck());
    }

}
