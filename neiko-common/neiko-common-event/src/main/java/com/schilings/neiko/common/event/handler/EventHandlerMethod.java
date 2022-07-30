package com.schilings.neiko.common.event.handler;

import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <pre>{@code
 *
 * }
 * <p>可执行方法的封装类</p>
 * </pre>
 *
 * @author Schilings
 */
public class EventHandlerMethod extends HandlerMethod {

	private static final Object[] EMPTY_ARGS = new Object[0];

	public EventHandlerMethod(HandlerMethod handlerMethod) {
		super(handlerMethod);
	}

	public EventHandlerMethod(Object bean, Method method) {
		super(bean, method);
	}

	protected EventHandlerMethod(Object bean, Method method, @Nullable MessageSource messageSource) {
		super(bean, method, messageSource);
	}

	public EventHandlerMethod(Object bean, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
		super(bean, methodName, parameterTypes);
	}

	public void invokeForEvent(Object... providedArgs) throws Exception {
		doInvoke(providedArgs);
	}

	protected void doInvoke(Object... args) throws Exception {
		Method method = getBridgedMethod();
		try {
			method.invoke(getBean(), args);
		}
		catch (IllegalArgumentException ex) {
			assertTargetBean(method, getBean(), args);
			String text = (ex.getMessage() != null ? ex.getMessage() : "Illegal argument");
			throw new IllegalStateException(formatInvokeError(text, args), ex);
		}
		catch (InvocationTargetException ex) {
			// Unwrap for HandlerExceptionResolvers ...
			Throwable targetException = ex.getTargetException();
			if (targetException instanceof RuntimeException) {
				throw (RuntimeException) targetException;
			}
			else if (targetException instanceof Error) {
				throw (Error) targetException;
			}
			else if (targetException instanceof Exception) {
				throw (Exception) targetException;
			}
			else {
				throw new IllegalStateException(formatInvokeError("Invocation failure", args), targetException);
			}
		}
	}

}
