package com.schilings.neiko.authorization.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(title = "授权用户同意信息")
public class AuthorizationConsentDTO {

    @Schema(title = "id")
    private Long id;
    
    @NotEmpty(message = "registeredClientId不能为空")
    @Schema(title = "客户端clientId")
    private String registeredClientId;

    @NotEmpty(message = "principalName不能为空")
    @Schema(title = "主体")
    private String principalName;

    @Schema(title = "权限")
    private String authorities;
}
