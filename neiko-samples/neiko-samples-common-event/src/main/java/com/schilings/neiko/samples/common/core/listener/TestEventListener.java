package com.schilings.neiko.samples.common.core.listener;

import com.schilings.neiko.common.event.annotation.NeikoEventHandler;
import com.schilings.neiko.common.event.annotation.NeikoEventListener;
import com.schilings.neiko.samples.common.core.Application;
import org.springframework.stereotype.Component;

@Component
@NeikoEventListener
public class TestEventListener {

	// @NeikoEventHandler(Object.class)
	public void demo2(Object o) {
		System.out.println("Test:" + o.toString());
	}

}
