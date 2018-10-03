package com.folkol;

import com.codahale.metrics.health.HealthCheck;

public class DummyHealthCheck extends HealthCheck {
    @Override
    protected Result check() {
        return null;
    }
}
