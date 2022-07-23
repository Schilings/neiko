package com.schilings.neiko.samples.redis;


import com.schilings.neiko.common.cache.annotation.NeikoCacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/demo")
public class DemoController {

    private static AtomicInteger atomicInteger = new AtomicInteger();
    
    @GetMapping("/test")
    @NeikoCacheable(key = "#p0")
    public DTO test(String username) {
        return new DTO(atomicInteger.incrementAndGet(), username, username);
    }
    
    
}
