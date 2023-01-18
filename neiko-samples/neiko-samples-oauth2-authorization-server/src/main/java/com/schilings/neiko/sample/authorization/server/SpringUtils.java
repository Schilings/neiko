package com.schilings.neiko.sample.authorization.server;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;

@Component
public class SpringUtils extends SpringUtil {

    private static RegisteredClientRepository clientRepository;

    public static RegisteredClientRepository getRegisteredClientRepository() {
        if (clientRepository != null) {
            return clientRepository;
        }
        clientRepository = getApplicationContext().getBean(RegisteredClientRepository.class);
        return clientRepository;
    }
}
