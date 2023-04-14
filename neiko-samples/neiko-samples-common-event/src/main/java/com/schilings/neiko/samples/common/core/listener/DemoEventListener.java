package com.schilings.neiko.samples.common.core.listener;

import com.schilings.neiko.common.event.publisher.EventBusSupport;

import com.schilings.neiko.common.event.annotation.NeikoEventHandler;
import com.schilings.neiko.common.event.annotation.NeikoEventListener;

import com.schilings.neiko.samples.common.core.DTO;
import com.schilings.neiko.samples.common.core.Event;
import org.springframework.stereotype.Component;

@Component
@NeikoEventListener
public class DemoEventListener extends EventBusSupport {

	@NeikoEventHandler(Event.class)
	public String demo1(DTO event) {
		System.out.println(Thread.currentThread());
		return "DTo";
	}

	@NeikoEventHandler(Event.class)
	public void demo2(DTO event) {
		System.out.println(Thread.currentThread());
	}

}
