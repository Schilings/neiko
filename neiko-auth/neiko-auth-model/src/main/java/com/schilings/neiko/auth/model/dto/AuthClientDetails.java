package com.schilings.neiko.auth.model.dto;


import com.schilings.neiko.common.security.pojo.ClientDetails;
import com.schilings.neiko.common.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * <p>Client信息Auth实现类</p>
 * 
 * @author Schilings
*/
@Data
@Schema(title = "客户端信息DTO")
public class AuthClientDetails implements ClientDetails {

    @Schema(title = "应用id")
    private String clientId;
    @Schema(title = "应用秘钥")
    private String clientSecret;

    @Schema(title = "应用允许授权的所有URL")
    private Set<String> urls;
    
    @Schema(title = "应用签约的所有权限")
    private Set<String> scope;

    @Schema(title = "应用被授权的授权类型")
    private Set<String> authorizedGrantTypes;

    @Schema(title = "单独配置此Client：Access-Token 保存的时间(单位秒)")
    private Long accessTokenTimeout;

    @Schema(title = "单独配置此Client：Refresh-Token 保存的时间(单位秒)")
    private Long refreshTokenTimeout;
    
    public void setScope(String scope) {
        if (StringUtils.isNotBlank(scope)) {
            this.scope = Arrays.stream(scope.split(",")).collect(Collectors.toSet());
        } else {
            this.scope = Collections.emptySet();
        } 
       
    }

    public void setUrls(String allowUrl) {
        if (StringUtils.isNotBlank(allowUrl)) {
            this.urls = Arrays.stream(allowUrl.split(",")).collect(Collectors.toSet());
        } else {
            this.urls = Collections.emptySet();
        } 
        
    }

    public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
        if (StringUtils.isNotBlank(authorizedGrantTypes)) {
            this.authorizedGrantTypes = Arrays.stream(authorizedGrantTypes.split(",")).collect(Collectors.toSet());
        } else {
            this.authorizedGrantTypes = Collections.emptySet();
        } 
    }
}
