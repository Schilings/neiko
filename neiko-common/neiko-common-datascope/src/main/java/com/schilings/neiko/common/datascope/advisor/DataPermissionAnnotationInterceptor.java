package com.schilings.neiko.common.datascope.advisor;

import com.schilings.neiko.common.datascope.annotation.DataPermission;
import com.schilings.neiko.common.datascope.core.DataPermissionRule;
import com.schilings.neiko.common.datascope.core.DataPermissionRuleHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 
 * <p>DataPermission注解的拦截器，在执行方法前将当前方法的对应注解压栈，执行后弹出注解</p>
 * 
 * @author Schilings
*/
public class DataPermissionAnnotationInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// 当前方法
		Method method = invocation.getMethod();
		// 获取执行类
		Object invocationThis = invocation.getThis();
		Class<?> clazz = invocationThis != null ? invocationThis.getClass() : method.getDeclaringClass();
		// 寻找对应的 DataPermission 注解属性
		DataPermission dataPermission = DataPermissionFinder.findDataPermission(method, clazz);
		// 理论上这里是不会为空的
		if (dataPermission == null) {
			return invocation.proceed();
		}

		DataPermissionRuleHolder.push(new DataPermissionRule(dataPermission));
		try {
			return invocation.proceed();
		}
		finally {
			DataPermissionRuleHolder.poll();
		}
	}

}
