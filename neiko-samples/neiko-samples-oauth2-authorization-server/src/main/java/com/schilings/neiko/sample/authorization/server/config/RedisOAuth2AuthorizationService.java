package com.schilings.neiko.sample.authorization.server.config;


import com.schilings.neiko.common.redis.RedisHelper;
import com.schilings.neiko.common.redis.core.serializer.CacheSerializer;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    public static final String AUTHORIZATIONS_PREFIX = RedisOAuth2AuthorizationService.class.getSimpleName() + ".authorizations";
    public static final String INITIALIZED_AUTHORIZATIONS_PREFIX = RedisOAuth2AuthorizationService.class.getName() + ".initializedAuthorizations";

    @Autowired
    private CacheSerializer cacheSerializer;


    private HashOperations<String, String, Object> getObjectHashOperations() {
        return RedisHelper.objectRedisTemplate().opsForHash();
    }

    
    private String getAuthorizationsPrefix(String id) {
        return AUTHORIZATIONS_PREFIX + "." + id;
    }
    
    private String getInitializedAuthorizationsPrefix(String id) {
        return INITIALIZED_AUTHORIZATIONS_PREFIX + "." + id;
    }

    private void saveAuthorization(OAuth2Authorization authorization) {
        getObjectHashOperations().put(getAuthorizationsPrefix("all"),getAuthorizationsPrefix(authorization.getId()), authorization);
    }    
    private void saveInitializedAuthorization(OAuth2Authorization authorization) {
        getObjectHashOperations().put(getInitializedAuthorizationsPrefix("all"), getInitializedAuthorizationsPrefix(authorization.getId()), authorization);
    }

    private void removeAuthorization(OAuth2Authorization authorization){
        getObjectHashOperations().delete(getAuthorizationsPrefix("all"),getAuthorizationsPrefix(authorization.getId()));
    }
    private void removeInitializedAuthorization(OAuth2Authorization authorization)  {
        getObjectHashOperations().delete(getInitializedAuthorizationsPrefix("all"), getInitializedAuthorizationsPrefix(authorization.getId()));
    }

    private OAuth2Authorization getAuthorization(String id)  {
        return (OAuth2Authorization) getObjectHashOperations().get(getAuthorizationsPrefix("all"),getAuthorizationsPrefix(id));
    }
    private OAuth2Authorization getInitializedAuthorization(String id) {
        return (OAuth2Authorization) getObjectHashOperations().get(getInitializedAuthorizationsPrefix("all"),getInitializedAuthorizationsPrefix(id));
    }

    private List<Object> getAuthorizations()  {
        return getObjectHashOperations().values(getAuthorizationsPrefix("all"));
    }
    
    private List<Object> getInitializedAuthorizations() {
        return getObjectHashOperations().values(getInitializedAuthorizationsPrefix("all"));
    }
    
    
    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        if (isComplete(authorization)) {
            RedisHelper.set("66666", "6666666");
            saveAuthorization(authorization);
        } else {
            saveInitializedAuthorization(authorization);
        }
    }
    

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        if (isComplete(authorization)) {
            removeAuthorization(authorization);
        } else {
            removeInitializedAuthorization(authorization);
        }
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        OAuth2Authorization authorization = getAuthorization(id);
        if (authorization != null) {
            return authorization;
        }
        OAuth2Authorization initializedAuthorization = getInitializedAuthorization(id);
        if (initializedAuthorization != null) {
            return authorization;     
        }
        return null;
    }

    @SneakyThrows
    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        List<OAuth2Authorization> authorizations = getAuthorizations().stream().map(a -> {
            return (OAuth2Authorization) a;
        }).collect(Collectors.toList());
        for (OAuth2Authorization authorization : authorizations) {
            if (hasToken(authorization, token, tokenType)) {
                return authorization;
            }
        }
        List<OAuth2Authorization> initializedAuthorizations = getInitializedAuthorizations().stream().map(a -> {
            return (OAuth2Authorization) a;
        }).collect(Collectors.toList());
        for (OAuth2Authorization authorization : initializedAuthorizations) {
            if (hasToken(authorization, token, tokenType)) {
                return authorization;
            }
        }
        return null;
    }

    

    private static boolean isComplete(OAuth2Authorization authorization) {
        return authorization.getAccessToken() != null;
    }

    private static boolean hasToken(OAuth2Authorization authorization, String token, @Nullable OAuth2TokenType tokenType) {
        if (tokenType == null) {
            return matchesState(authorization, token) ||
                    matchesAuthorizationCode(authorization, token) ||
                    matchesAccessToken(authorization, token) ||
                    matchesRefreshToken(authorization, token);
        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            return matchesState(authorization, token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            return matchesAuthorizationCode(authorization, token);
        } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            return matchesAccessToken(authorization, token);
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            return matchesRefreshToken(authorization, token);
        }
        return false;
    }

    private static boolean matchesState(OAuth2Authorization authorization, String token) {
        return token.equals(authorization.getAttribute(OAuth2ParameterNames.STATE));
    }

    private static boolean matchesAuthorizationCode(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                authorization.getToken(OAuth2AuthorizationCode.class);
        return authorizationCode != null && authorizationCode.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesAccessToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
                authorization.getToken(OAuth2AccessToken.class);
        return accessToken != null && accessToken.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesRefreshToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
                authorization.getToken(OAuth2RefreshToken.class);
        return refreshToken != null && refreshToken.getToken().getTokenValue().equals(token);
    }
}
