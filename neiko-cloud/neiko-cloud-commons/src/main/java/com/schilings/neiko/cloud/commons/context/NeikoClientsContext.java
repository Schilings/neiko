package com.schilings.neiko.cloud.commons.context;


import com.schilings.neiko.cloud.commons.context.config.DefaultNeikoClientsConfiguration;
import com.schilings.neiko.common.util.spring.SpringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * <pre>
 * <p></p>
 * </pre>
 * @author Schilings
*/
public class NeikoClientsContext extends NamedContextFactory<NeikoSpecification> implements ApplicationContextAware {
    
    
    public static final String NAMESPACE = "neiko";

    public static final String PROPERTY_NAME = NAMESPACE + ".client.name";

    public NeikoClientsContext() {
        super(DefaultNeikoClientsConfiguration.class, NAMESPACE, PROPERTY_NAME);
    }
    
    public NeikoClientsContext(Class<?> defaultConfigType, String propertySourceName, String propertyName) {
        super(defaultConfigType, propertySourceName, propertyName);
    }

    @Nullable
    public <T> T getInstanceWithoutAncestors(String name, Class<T> type) {
        
        try {
            return BeanFactoryUtils.beanOfType(getContext(name), type);
        }
        catch (BeansException ex) {
            return null;
        }
    }

    @Nullable
    public <T> Map<String, T> getInstancesWithoutAncestors(String name, Class<T> type) {
        return getContext(name).getBeansOfType(type);
    }
}
