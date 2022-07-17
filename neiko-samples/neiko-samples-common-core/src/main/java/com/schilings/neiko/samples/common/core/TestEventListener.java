package com.schilings.neiko.samples.common.core;


import com.schilings.neiko.common.event.annotation.NeikoEventHandler;
import com.schilings.neiko.common.event.annotation.NeikoEventListener;
import org.springframework.stereotype.Component;

@Component
@NeikoEventListener
public class TestEventListener {

    
    @NeikoEventHandler(Application.class)
    public void demo(Application application) {
        
        System.out.println("adsa");

    }
    @NeikoEventHandler(Object.class)
    public void demo2(Object o) {
        System.out.println("12132123" + o.toString());
    }
    
}
