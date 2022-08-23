package com.schilings.neiko.auth.service.impl;


import com.schilings.neiko.auth.mapper.AuthClientMapper;
import com.schilings.neiko.auth.model.dto.AuthClientDetails;
import com.schilings.neiko.auth.model.entity.AuthClient;
import com.schilings.neiko.auth.service.AuthClientService;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthClientServiceImpl extends ExtendServiceImpl<AuthClientMapper, AuthClient> implements AuthClientService {

    /**
     * 应通过ClientId获取客户端信息
     * @param clientId
     * @return
     */
    @Override
    public AuthClientDetails getClientDetails(String clientId) {
        return baseMapper.getByClientId(clientId);
    }
    
}
