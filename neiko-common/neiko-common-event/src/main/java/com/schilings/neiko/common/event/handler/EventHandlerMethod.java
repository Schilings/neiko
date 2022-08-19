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

	// 表示Void无返回结果
	private static final Object NO_RESULT = new Object();

	// 表示无参数
	private static final Object[] EMPTY_ARGS = new Object[0];

	// 方法返回结果
	private Object result;

	public Object getResult() {
		return result;
	}

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
		// 如果是不是Void，则需要返回结果
		if (!isVoid()) {
			this.result = doInvoke(providedArgs);
			return;
		}
		doInvoke(providedArgs);
		this.result = NO_RESULT;

	}

	protected Object doInvoke(Object... args) throws Exception {
		Method method = getBridgedMethod();
		try {
			return method.invoke(getBean(), args);
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
