package com.schilings.neiko.auth.model.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.schilings.neiko.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * <p>认证客户端表</p>
 * 
 * @author Schilings
*/
@Data
@TableName("nk_auth_client")
@Schema(title = "认证客户端表")
public class AuthClient implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @TableId
    @Column(comment = "主键ID")
    @Schema(title = "主键ID")
    private Long id;

    /**
     * 应用id 
     */
    @Column(comment = "应用id")
    @Schema(title = "应用id")
    private String clientId;

    /**
     * 应用秘钥 
     */
    @Column(comment = "应用秘钥")
    @Schema(title = "应用秘钥")
    private String clientSecret;

    /**
     * 应用签约的所有权限, 多个用逗号隔开 
     */
    @Column(comment = "应用签约的所有权限, 多个用逗号隔开 ")
    @Schema(title = "应用签约的所有权限, 多个用逗号隔开 ")
    private String scope;

    /**
     * 应用允许授权的所有URL, 多个用逗号隔开 
     */
    @Column(comment = "应用允许授权的所有URL, 多个用逗号隔开 ")
    @Schema(title = "应用允许授权的所有URL, 多个用逗号隔开 ")
    private String allowUrl;

    /**
     * 此客户端被授权的授权类型。
     */
    @Column(comment = "应用被授权的授权类型, 多个用逗号隔开 ")
    @Schema(title = "应用被授权的授权类型, 多个用逗号隔开 ")
    private String authorizedGrantTypes;


    /**
     * 此客户端在“authorization_code”访问授权期间使用的预定义重定向 URI
     */
    @Column(comment = "此客户端在“authorization_code”访问授权期间使用的预定义重定向 URI ")
    @Schema(title = "此客户端在“authorization_code”访问授权期间使用的预定义重定向 URI")
    private String webServerRedirectUri;

    /** 单独配置此Client：Access-Token 保存的时间(单位秒)  [默认取全局配置] */
    @Column(comment = "单独配置此Client：Access-Token 保存的时间(单位秒) ")
    @Schema(title = "单独配置此Client：Access-Token 保存的时间(单位秒)")
    private Long accessTokenTimeout;

    /** 单独配置此Client：Refresh-Token 保存的时间(单位秒) [默认取全局配置] */
    @Column(comment = "单独配置此Client：Refresh-Token 保存的时间(单位秒)")
    @Schema(title = "单独配置此Client：Refresh-Token 保存的时间(单位秒)")
    private Long refreshTokenTimeout;
    
    @TableField(jdbcType = JdbcType.VARCHAR,typeHandler = JacksonTypeHandler.class)
    @Column(comment = "附加属性值",type = MySqlTypeConstant.VARCHAR)
    @Schema(title = "附加属性值")
    private Map<String, Object> attributes;

    
}
