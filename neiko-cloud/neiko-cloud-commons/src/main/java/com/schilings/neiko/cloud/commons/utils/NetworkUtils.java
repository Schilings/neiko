package com.schilings.neiko.cloud.commons.utils;

import com.schilings.neiko.common.util.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;

import java.net.URI;

@Slf4j
public class NetworkUtils {

	private static InetUtils inet;

	public NetworkUtils(InetUtils inetUtils) {
		this.inet = SpringUtils.getBean(InetUtils.class);
		Assert.notNull(inetUtils, "NetworkUtils can not be null");
	}

	/**
	 * 获取本地host
	 * @return
	 */
	public static String getLocalHostname() {
		return inet.findFirstNonLoopbackHostInfo().getHostname();
	}

	/**
	 * 获取ID地址
	 * @return
	 */
	public static String getIpAddress() {
		return inet.findFirstNonLoopbackHostInfo().getIpAddress();
	}

	/**
	 * 获取本地host
	 * @return
	 */
	public static String getLocalHostname(ConfigurableEnvironment environment) {
		return getFirstNonLoopbackHostInfo(environment).getHostname();
	}

	/**
	 * 获取ID地址
	 * @return
	 */
	public static String getIpAddress(ConfigurableEnvironment environment) {
		return getFirstNonLoopbackHostInfo(environment).getIpAddress();
	}

	/**
	 * 修改 URI 以将请求重定向到选择的服务实例
	 * @param serviceInstance – 要将请求重定向到的ServiceInstance
	 * @param original 原始请求的URI
	 * @return 修改后的URI
	 */
	public static URI reconstructURI(ServiceInstance serviceInstance, URI original) {
		return LoadBalancerUriTools.reconstructURI(serviceInstance, original);
	}

	public static InetUtils.HostInfo getFirstNonLoopbackHostInfo(ConfigurableEnvironment environment) {
		InetUtilsProperties target = new InetUtilsProperties();
		ConfigurationPropertySources.attach(environment);
		Binder.get(environment).bind(InetUtilsProperties.PREFIX, Bindable.ofInstance(target));
		try (InetUtils utils = new InetUtils(target)) {
			return utils.findFirstNonLoopbackHostInfo();
		}
	}

}
