package com.schilings.neiko.samples.starters.web.dto;

import com.schilings.neiko.common.core.desensitize.annotation.JsonRegexDesensitize;
import com.schilings.neiko.common.core.desensitize.annotation.JsonSimpleDesensitize;
import com.schilings.neiko.common.core.desensitize.annotation.JsonSlideDesensitize;
import com.schilings.neiko.common.core.desensitize.enums.RegexDesensitizationTypeEnum;
import com.schilings.neiko.common.core.desensitize.enums.SlideDesensitizationTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateDTO {

	@JsonSimpleDesensitize(condition = "@desensitizationController.check(#value)")
	private String username;

	@JsonSlideDesensitize(condition = "#value.length()==11", type = SlideDesensitizationTypeEnum.CUSTOM,
			leftPlainTextLen = 1, rightPlainTextLen = 2, maskString = "x")
	private String phone;

	@JsonRegexDesensitize(type = RegexDesensitizationTypeEnum.EMAIL)
	private String email;

}
