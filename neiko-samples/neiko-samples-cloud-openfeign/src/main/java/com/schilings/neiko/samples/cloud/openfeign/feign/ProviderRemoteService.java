package com.schilings.neiko.samples.cloud.openfeign.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "provide",url = "http://localhost:9999")
public interface ProviderRemoteService {
}
