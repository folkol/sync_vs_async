package com.folkol;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ContentApplication extends Application<ContentConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ContentApplication().run(args);
    }

    @Override
    public String getName() {
        return "Content";
    }

    @Override
    public void initialize(final Bootstrap<ContentConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final ContentConfiguration configuration,
                    final Environment environment)
    {
        // TODO: implement application
    }

}
