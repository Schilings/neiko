package com.schilings.neiko.sample.authorization.server.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.schilings.neiko.common.redis.RedisHelper;

import com.schilings.neiko.security.oauth2.authorization.server.jackson.AuthorizationServerJackson2Module;
import com.schilings.neiko.security.oauth2.authorization.server.jackson.OAuth2AuthorizationMixin;
import lombok.SneakyThrows;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.lang.Nullable;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    public static final String AUTHORIZATIONS_PREFIX = RedisOAuth2AuthorizationService.class.getSimpleName() + ".authorizations";
    public static final String INITIALIZED_AUTHORIZATIONS_PREFIX = RedisOAuth2AuthorizationService.class.getName() + ".initializedAuthorizations";


    private final ObjectMapper objectMapper;

    private final HashMap<TokenKey, String> idTokenMap = new LinkedHashMap<>();
    
    public RedisOAuth2AuthorizationService() {
        objectMapper = new ObjectMapper();
        ClassLoader classLoader = RedisOAuth2AuthorizationService.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        objectMapper.registerModule(new AuthorizationServerJackson2Module());
    }

    public RedisOAuth2AuthorizationService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private HashOperations<String, String, String> getHashOperations() {
        return RedisHelper.getHash();
    }
    
    
    private String getAuthorizationsPrefix(String id) {
        return AUTHORIZATIONS_PREFIX + "." + id;
    }
    
    private String getInitializedAuthorizationsPrefix(String id) {
        return INITIALIZED_AUTHORIZATIONS_PREFIX + "." + id;
    }

    private void saveAuthorization(OAuth2Authorization authorization) throws JsonProcessingException {
        getHashOperations().put(getAuthorizationsPrefix("all"), getAuthorizationsPrefix(authorization.getId()), objectMapper.writeValueAsString(authorization));
    }    
    private void saveInitializedAuthorization(OAuth2Authorization authorization) throws JsonProcessingException {
        getHashOperations().put(
                getInitializedAuthorizationsPrefix("all"), 
                getInitializedAuthorizationsPrefix(authorization.getId()), objectMapper.writeValueAsString(authorization));
    }

    private void removeAuthorization(OAuth2Authorization authorization){
        getHashOperations().delete(getAuthorizationsPrefix("all"),getAuthorizationsPrefix(authorization.getId()));
    }
    private void removeInitializedAuthorization(OAuth2Authorization authorization)  {
        getHashOperations().delete(getInitializedAuthorizationsPrefix("all"), getInitializedAuthorizationsPrefix(authorization.getId()));
    }

    private OAuth2Authorization getAuthorization(String id) throws JsonProcessingException {
        return objectMapper.readValue(getHashOperations().get(getAuthorizationsPrefix("all"), getAuthorizationsPrefix(id)), OAuth2Authorization.class);
    }
    
    @SneakyThrows
    private OAuth2Authorization getInitializedAuthorization(String id) {
        return objectMapper.readValue(getHashOperations().get(getInitializedAuthorizationsPrefix("all"), getInitializedAuthorizationsPrefix(id)), OAuth2Authorization.class);

    }

    private List<OAuth2Authorization> getAuthorizations()  {
        return getHashOperations().values(getAuthorizationsPrefix("all")).stream().map(s -> {
            try {
                return objectMapper.readValue(s, OAuth2Authorization.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }
    
    private List<OAuth2Authorization> getInitializedAuthorizations() {
        return getHashOperations().values(getInitializedAuthorizationsPrefix("all")).stream().map(s -> {
            try {
                return objectMapper.readValue(s, OAuth2Authorization.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }
    
    
    @SneakyThrows
    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        if (isComplete(authorization)) {
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

    @SneakyThrows
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
        List<OAuth2Authorization> authorizations = getAuthorizations();
        for (OAuth2Authorization authorization : authorizations) {
            if (hasToken(authorization, token, tokenType)) {
                return authorization;
            }
        }
        List<OAuth2Authorization> initializedAuthorizations = getInitializedAuthorizations();
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

    
    public static class TokenKey implements Comparable<TokenKey> {
        
        private final String tokenValue;
        
        @Nullable
        private final OAuth2TokenType tokenType;

        public TokenKey(String tokenValue, OAuth2TokenType tokenType) {
            this.tokenValue = tokenValue;
            this.tokenType = tokenType;
        }
        

        @Override
        public boolean equals(@Nullable Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof TokenKey)) {
                return false;
            }
            TokenKey otherKey = (TokenKey) other;
            return (this.tokenValue.equals(otherKey.tokenValue) &&
                    ObjectUtils.nullSafeEquals(this.tokenType, otherKey.tokenType));
        }

        @Override
        public int hashCode() {
            return this.tokenValue.hashCode() + (this.tokenType != null ? this.tokenType.hashCode() * 29 : 0);
        }

        @Override
        public String toString() {
            return this.tokenValue + (this.tokenType != null ? " is " + this.tokenType : "");
        }

        @Override
        public int compareTo(TokenKey other) {
            int result = this.tokenValue.compareTo(other.tokenValue);
            if (result == 0 && this.tokenType != null) {
                if (other.tokenType == null) {
                    return 1;
                }
                result = this.tokenType.getValue().compareTo(other.tokenType.getValue());
            }
            return result;
        }
    }
}
