package com.schilings.neiko.cloud.commons.utils;


import com.schilings.neiko.common.util.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools;
import org.springframework.cloud.commons.util.InetUtils;
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
    public static String getLocalHost() {
        return inet.findFirstNonLoopbackHostInfo().getHostname();
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
        
    
}
