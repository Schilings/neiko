package com.schilings.neiko.authorization.model.qo;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

@Data
@Schema(title = "授权用户同意信息")
@ParameterObject
@NoArgsConstructor
@Accessors(chain = true)
public class AuthorizationConsentQO {

    @Parameter(description = "客户端clientId")
    private String registeredClientId;

    @Parameter(description = "主体")
    private String principalName;

    @Parameter(description = "开始时间")
    @DateTimeFormat(pattern = NORM_DATETIME_PATTERN)
    private String startTime;

    @Parameter(description = "结束时间")
    @DateTimeFormat(pattern = NORM_DATETIME_PATTERN)
    private String endTime;
}
