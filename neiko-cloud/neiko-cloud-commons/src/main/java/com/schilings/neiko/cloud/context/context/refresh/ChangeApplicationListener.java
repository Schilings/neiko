package com.schilings.neiko.cloud.context.context.refresh;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
public class ChangeApplicationListener implements ApplicationListener<EnvironmentChangeEvent> {

    private static Log log = LogFactory.getLog(ChangeApplicationListener.class);
    
    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        
        //就是一个简单是环境改变事件，但是可以自己判断
        //改变的属性字段 第一次刷新的时候会刷新，也就第一次刷新会有spring.cloud.bootstrap.enabled
        //不过考虑一下要怎么拿到改变后的值
        Set<String> keys = event.getKeys();
        keys.forEach(k -> System.out.println(k));
        ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) event.getSource();
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        

    }
}
