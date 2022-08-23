package com.schilings.neiko.auth.service;

import com.schilings.neiko.auth.model.dto.AuthClientDetails;
import com.schilings.neiko.auth.model.entity.AuthClient;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;

public interface AuthClientService extends ExtendService<AuthClient> {

    /**
     * 应通过ClientId获取客户端信息
     * @param clientId
     * @return
     */
    AuthClientDetails getClientDetails(String clientId);
    
}
