package com.schilings.neiko.authorization.model.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.schilings.neiko.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("nk_authorization_consent")
@Schema(title = "授权用户同意信息")
public class AuthorizationConsent {

    @TableId
    @Schema(title = "id")
    private Long id;

    @Schema(title = "客户端clientId")
    private String registeredClientId;

    @Schema(title = "主体")
    private String principalName;

    @Schema(title = "权限")
    private String authorities;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(title = "更新时间")
    private LocalDateTime updateTime;
}
