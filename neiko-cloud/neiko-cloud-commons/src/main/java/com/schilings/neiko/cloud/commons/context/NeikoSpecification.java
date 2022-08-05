package com.schilings.neiko.cloud.commons.context;


import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Objects;

public class NeikoSpecification implements NamedContextFactory.Specification {


    private String name;

    private Class<?>[] configuration;

    public NeikoSpecification() {
    }

    public NeikoSpecification(String name, Class<?>[] configuration) {
        Assert.hasText(name, "name must not be empty");
        this.name = name;
        Assert.notNull(configuration, "configuration must not be null");
        this.configuration = configuration;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        Assert.hasText(name, "name must not be empty");
        this.name = name;
    }

    public Class<?>[] getConfiguration() {
        return this.configuration;
    }

    public void setConfiguration(Class<?>[] configuration) {
        Assert.notNull(configuration, "configuration must not be null");
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        ToStringCreator to = new ToStringCreator(this);
        to.append("name", this.name);
        to.append("configuration", this.configuration);
        return to.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NeikoSpecification that = (NeikoSpecification) o;
        return Objects.equals(this.name, that.name) && Arrays.equals(this.configuration, that.configuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.configuration);
    }
}
