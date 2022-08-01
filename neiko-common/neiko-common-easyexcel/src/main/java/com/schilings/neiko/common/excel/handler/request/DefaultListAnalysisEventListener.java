package com.schilings.neiko.common.excel.handler.request;

import cn.hutool.extra.validation.BeanValidationResult;
import com.alibaba.excel.context.AnalysisContext;
import com.schilings.neiko.common.core.validation.Validators;
import com.schilings.neiko.common.excel.vo.ImprotErrorMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * <p>默认实现</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public class DefaultListAnalysisEventListener extends ListAnalysisEventListener<Object> {

	private List<Object> list = new ArrayList<>();

	private List<ImprotErrorMessage> errors = new ArrayList<>();

	private long lineNum = 1L;

	/**
	 * 拦截
	 * @param data
	 * @param context
	 */
	@Override
	public void invoke(Object data, AnalysisContext context) {
		BeanValidationResult result = Validators.warpValidate(data);
		// 校验不通过
		if (!result.isSuccess()) {
			errors.add(new ImprotErrorMessage(lineNum++, result));
		}
		else {
			list.add(data);
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		log.debug("Excel read analysed");
	}

	@Override
	public List<Object> getList() {
		return list;
	}

	@Override
	public List<ImprotErrorMessage> getErrors() {
		return errors;
	}

}
