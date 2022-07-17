package com.schilings.neiko.samples.common.core;


import com.schilings.neiko.common.event.annotation.NeikoEventHandler;
import com.schilings.neiko.common.event.annotation.NeikoEventListener;
import org.springframework.stereotype.Component;

@Component
@NeikoEventListener
public class DemoEventListener {

    @NeikoEventHandler(DTO.class)
    public void demo(DTO dto) {
        System.out.println(dto.toString());
    }
    
    
}
