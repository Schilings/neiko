package com.schilings.neiko.common.event;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.ThreadFactory;

public class NeikoEventThreadFactory extends DefaultThreadFactory {


	public NeikoEventThreadFactory(Class<?> poolType) {
		super(poolType);
	}

	public NeikoEventThreadFactory(String poolName) {
		super(poolName);
	}
	

}
