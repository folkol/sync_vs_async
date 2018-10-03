package com.folkol;

import com.folkol.resources.ContentServiceAsync;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ContentApplicationAsync extends Application<ContentConfigurationAsync> {

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
        environment.jersey().register(new ContentServiceAsync());
        environment.healthChecks().register("dummy", new DummyHealthCheck());
    }

}
