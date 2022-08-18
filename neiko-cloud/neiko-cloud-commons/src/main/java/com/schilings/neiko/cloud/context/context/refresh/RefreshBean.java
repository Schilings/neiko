package com.schilings.neiko.cloud.context.context.refresh;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
public class RefreshBean implements InitializingBean, DisposableBean {

	/**
	 * 刷新作用域就会调用
	 * @throws Exception
	 */
	@Override
	public void destroy() throws Exception {
		System.out.println("demo-controller===>destroy");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("demo-controller===>afterPropertiesSet");
	}

}
