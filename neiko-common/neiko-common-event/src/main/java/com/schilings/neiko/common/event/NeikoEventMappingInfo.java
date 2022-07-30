package com.schilings.neiko.common.event;

import com.schilings.neiko.common.event.handler.EventHandler;
import com.schilings.neiko.common.event.handlermapping.EventMappingInfo;
import com.schilings.neiko.common.event.annotation.NeikoEventHandler;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * <pre>{@code
 *
 * }
 * <p>Neiko自定义映射信息</p>
 * </pre>
 *
 * @author Schilings
 */
@Data
public class NeikoEventMappingInfo implements EventMappingInfo {

	private final Class<?> userClass;

	private final Method method;

	private NeikoEventHandler neiko;

	private Class<?> eventClz;

	public NeikoEventMappingInfo(Class userClass, Method method) {
		this.userClass = userClass;
		this.method = method;
	}

	public void setNeikoEventHandler(NeikoEventHandler neiko) {
		this.neiko = neiko;
		setEventClz(neiko.value());

	}

	@Override
	public Class<?> getUserClass() {
		return userClass;
	}

	@Override
	public Method getMethod() {
		return method;
	}

	@Override
	public String getUniqueName() {
		return eventClz.getName();
	}

}
