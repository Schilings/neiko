package com.schilings.neiko.samples.common.core.listener;


import com.schilings.neiko.common.event.publisher.EventPublisherSupport;
import com.schilings.neiko.common.event.annotation.NeikoEventHandler;
import com.schilings.neiko.common.event.annotation.NeikoEventListener;
import com.schilings.neiko.common.event.publisher.EventPublisher;
import com.schilings.neiko.samples.common.core.Event;
import org.springframework.stereotype.Component;

@Component
@NeikoEventListener
public class DemoEventListener extends EventPublisherSupport {

    //在事件监听器中@Autowired 直接注入会造成循环依赖 EventPublisher 有 HandlerMapping 有 EventListener 又有 EventPublisher
    //private EventPublisher eventPublisher;

    @NeikoEventHandler(Event.class)
    public void demo(Event event) {
        System.out.println("DemoEventListener==" + event.getTarget());
        //不要贸然发布相同的事件，不然陷入事件递归
        getPublisher().publish("666666666666");
        
    }
    
    
}
