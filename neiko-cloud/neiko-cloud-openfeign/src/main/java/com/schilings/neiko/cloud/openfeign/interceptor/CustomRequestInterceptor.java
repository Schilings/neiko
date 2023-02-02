package com.schilings.neiko.cloud.openfeign.interceptor;

import feign.*;

/**
 * <p>
 * 实现可以读取、删除或以其他方式改变请求模板的任何部分。
 * </p>
 * <p>
 * 可以配置零个或多个RequestInterceptors用于向所有请求添加标头等目的。
 * </p>
 * <p>
 * 不保证应用拦截器的顺序。
 * </p>
 * <p>
 * 一旦应用了拦截器，就会调用{@link Target#apply(RequestTemplate)}来创建通过
 * {@link Client#execute(Request, Request.Options)}发送的不可变 http 请求。
 * </p>
 *
 * <p>
 * RequestInterceptors是通过{@link Feign.Builder.requestInterceptors}配置的。
 * </p>
 *
 * <p>
 * 不要在apply(RequestTemplate)的实现中添加参数，例如/path/{foo}/bar 。
 * 拦截器在模板的参数被resolved后应用。这是为了确保您可以实现签名是拦截器
 * </p>
 *
 * @author Schilings
 */
public class CustomRequestInterceptor implements RequestInterceptor {

	/**
	 * <p>
	 * 为每个请求调用。使用提供的RequestTemplate上的方法添加数据
	 * </p>
	 * @param requestTemplate
	 */
	@Override
	public void apply(RequestTemplate requestTemplate) {
		requestTemplate.header("X-Auth", "currentToken");
	}

}
