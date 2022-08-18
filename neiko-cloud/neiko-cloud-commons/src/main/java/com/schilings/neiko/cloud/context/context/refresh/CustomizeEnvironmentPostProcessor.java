package com.schilings.neiko.cloud.context.context.refresh;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

public class CustomizeEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        
    }
}
