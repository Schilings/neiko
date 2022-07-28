package com.schilings.neiko.samples.starters.web.dto;


import com.schilings.neiko.common.core.validation.annotation.Greater;
import com.schilings.neiko.common.core.validation.annotation.Less;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class UserAddDTO {

    /**
     * 账号
     */
    @NotEmpty(message = "登陆账号不能为空")
    @Length(min = 5, max = 16, message = "账号长度为 5-16 位")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "账号格式为数字以及字母")
    private String username;
    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @Length(min = 4, max = 16, message = "密码长度为 4-16 位")
    private String password;

    @Greater(value = 0, equal = false, message = "数字要大于0")
    @Less(value = 10, equal = false, message = "数字要小于10")
    private BigDecimal amount;
    

}
