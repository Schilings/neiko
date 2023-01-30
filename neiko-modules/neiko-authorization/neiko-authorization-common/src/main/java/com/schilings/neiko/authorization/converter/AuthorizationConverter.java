package com.schilings.neiko.authorization.converter;

import com.schilings.neiko.authorization.model.entity.Authorization;
import com.schilings.neiko.authorization.model.vo.AuthorizationPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Set;

@Mapper
public interface AuthorizationConverter {

    AuthorizationConverter INSTANCE = Mappers.getMapper(AuthorizationConverter.class);


    @Mappings(value = {
            @Mapping(target = "authorizationCodeIssuedAt", expression = "java(stringToInstant(po.getAuthorizationCodeExpiresAt()))"),
            @Mapping(target = "authorizationCodeExpiresAt", expression = "java(stringToInstant(po.getAuthorizationCodeExpiresAt()))"),
            @Mapping(target = "accessTokenIssuedAt", expression = "java(stringToInstant(po.getAccessTokenIssuedAt()))"),
            @Mapping(target = "accessTokenExpiresAt", expression = "java(stringToInstant(po.getAccessTokenExpiresAt()))"),
            @Mapping(target = "refreshTokenIssuedAt", expression = "java(stringToInstant(po.getRefreshTokenIssuedAt()))"),
            @Mapping(target = "refreshTokenExpiresAt", expression = "java(stringToInstant(po.getRefreshTokenExpiresAt()))"),
            @Mapping(target = "oidcIdTokenIssuedAt", expression = "java(stringToInstant(po.getOidcIdTokenIssuedAt()))"),
            @Mapping(target = "oidcIdTokenExpiresAt", expression = "java(stringToInstant(po.getOidcIdTokenExpiresAt()))"),
            @Mapping(target = "accessTokenScopes", expression = "java(commaDelimitedListToSet(po.getAccessTokenScopes()))"),
            @Mapping(target = "createTime", expression = "java(po.getCreateTime())"),
            @Mapping(target = "updateTime", expression = "java(po.getUpdateTime())"),
    })
    AuthorizationPageVO poToPageVo(Authorization po);


    default Instant stringToInstant(String s) {
        try {
            return Instant.parse(s);
        } catch (Exception e) {
            return null;
        }
    }

    default Set<String> commaDelimitedListToSet(String str) {
        return StringUtils.commaDelimitedListToSet(str);
    }
}

