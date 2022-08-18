package com.schilings.neiko.cloud.commons.serviceregistry;

import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

/**
 *
 * <p>
 * {@link com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration}
 * </p>
 *
 * @author Schilings
 */
public class CustomAutoServiceRegistration extends AbstractAutoServiceRegistration<Registration> {

	// 里面关于注册中心的信息和自身服务的信息
	// 调用register()会被ServiceRegistry拿去传入到register(Registration registration)使用
	// 去注册实例
	private Registration registration;

	protected CustomAutoServiceRegistration(ServiceRegistry<Registration> serviceRegistry,
			AutoServiceRegistrationProperties properties, Registration registration) {
		super(serviceRegistry, properties);
		this.registration = registration;
	}

	@Override
	protected Object getConfiguration() {
		return null;
	}

	@Override
	protected boolean isEnabled() {
		return true;
	}

	@Override
	protected Registration getRegistration() {
		return registration;
	}

	@Override
	protected Registration getManagementRegistration() {
		return null;
	}

}
