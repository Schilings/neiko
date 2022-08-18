package com.schilings.neiko.cloud.gateway.route;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.gateway.route.CachingRouteLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.util.Assert;

@Slf4j
public class RouteLocatorDelegate implements InitializingBean {

	private final RouteDefinitionRepository routeDefinitionRepository;

	private final RouteDefinitionLocator routeDefinitionLocator;

	private CachingRouteLocator cachingRouteLocator;

	public RouteLocatorDelegate(RouteDefinitionRepository routeDefinitionRepository,
			RouteDefinitionLocator routeDefinitionLocator, RouteLocator routeLocator) {

		if (routeLocator instanceof CachingRouteLocator) {
			this.cachingRouteLocator = (CachingRouteLocator) routeLocator;
		}
		Assert.notNull(cachingRouteLocator, "CachingRouteLocator must not be null.");
		this.routeDefinitionLocator = routeDefinitionLocator;
		this.routeDefinitionRepository = routeDefinitionRepository;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

}
