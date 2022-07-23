package com.schilings.neiko.samples.redis;


import com.schilings.neiko.common.cache.EnableNeikoCaching;
import com.schilings.neiko.common.cache.annotation.NeikoCacheable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@EnableNeikoCaching
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        Application bean = context.getBean(Application.class);
        System.out.println(bean.test3("11111"));
        System.out.println(bean.test3("22222"));
    }
    @NeikoCacheable(key = "'prefix:'+#p0",condition = "1+1 == 2",unless = "#p0.equals('11111')")
    public String test(String msg) {
        return msg;
    }

    @NeikoCacheable(key = "'prefix:'+#p0",condition = "#result.equals('11111')")
    public String test2(String msg) {
        return msg;
    }

    @NeikoCacheable(key = "'key'")
    public String test3(String msg) {
        return msg;
    }
    
}
