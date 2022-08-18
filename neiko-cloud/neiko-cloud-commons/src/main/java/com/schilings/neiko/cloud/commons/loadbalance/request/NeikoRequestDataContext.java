package com.schilings.neiko.cloud.commons.loadbalance.request;

import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;
import org.springframework.http.HttpMethod;

/**
 * <pre>
 * <p>{@link RequestData}</p>
 * </pre>
 *
 * @author Schilings
 */
public class NeikoRequestDataContext extends RequestDataContext {

	@Override
	public HttpMethod method() {
		return ((RequestData) super.getClientRequest()).getHttpMethod();
	}

}
