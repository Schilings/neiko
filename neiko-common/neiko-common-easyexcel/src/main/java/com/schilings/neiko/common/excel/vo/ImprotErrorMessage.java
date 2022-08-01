package com.schilings.neiko.common.excel.vo;

import cn.hutool.extra.validation.BeanValidationResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class ImprotErrorMessage {

	/**
	 * 行号
	 */
	private Long lineNum;

	private BeanValidationResult errorResult;

	private Set<String> messages;

	public ImprotErrorMessage(Long lineNum, BeanValidationResult result) {
		this.lineNum = lineNum;
		this.errorResult = result;
		this.messages = errorResult.getErrorMessages().stream().map(BeanValidationResult.ErrorMessage::getMessage)
				.collect(Collectors.toSet());

	}

}
