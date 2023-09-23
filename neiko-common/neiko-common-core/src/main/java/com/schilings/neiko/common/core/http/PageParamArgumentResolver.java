package com.schilings.neiko.common.core.http;

import com.schilings.neiko.common.model.constants.PageableConstants;
import com.schilings.neiko.common.model.domain.PageParam;
import org.springframework.core.MethodParameter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.service.invoker.HttpRequestValues;
import org.springframework.web.service.invoker.HttpServiceArgumentResolver;

import java.util.List;

public class PageParamArgumentResolver implements HttpServiceArgumentResolver {

	private static final String EMPTY_STRING = "";

	private final String pageParameterName;

	private final String sizeParameterName;

	private final String sortParameterName;

	public PageParamArgumentResolver() {
		this.pageParameterName = PageableConstants.DEFAULT_PAGE_PARAMETER;
		this.sizeParameterName = PageableConstants.DEFAULT_SIZE_PARAMETER;
		this.sortParameterName = PageableConstants.DEFAULT_SORT_PARAMETER;
	}

	public PageParamArgumentResolver(String pageParameterName, String sizeParameterName, String sortParameterName) {
		this.pageParameterName = StringUtils.hasText(pageParameterName) ? pageParameterName
				: PageableConstants.DEFAULT_PAGE_PARAMETER;
		this.sizeParameterName = StringUtils.hasText(sizeParameterName) ? sizeParameterName
				: PageableConstants.DEFAULT_SIZE_PARAMETER;
		this.sortParameterName = StringUtils.hasText(sortParameterName) ? sortParameterName
				: PageableConstants.DEFAULT_SORT_PARAMETER;
	}

	@Override
	public boolean resolve(Object argument, MethodParameter parameter, HttpRequestValues.Builder requestValues) {

		if (!parameter.getParameterType().equals(PageParam.class)) {
			return false;
		}
		PageParam pageParam = null;
		if (argument instanceof PageParam param) {
			pageParam = param;
		}
		if (pageParam == null) {
			pageParam = new PageParam();
		}
		addPage(pageParam.getPage(), requestValues);
		addSize(pageParam.getSize(), requestValues);
		addSort(pageParam.getSort(), requestValues);
		return true;
	}

	private void addPage(long page, HttpRequestValues.Builder requestValues) {
		requestValues.addRequestParameter(pageParameterName, parseValueFromLoong(page));
	}

	private void addSize(long size, HttpRequestValues.Builder requestValues) {
		requestValues.addRequestParameter(sizeParameterName, parseValueFromLoong(size));
	}

	private void addSort(List<PageParam.Sort> sorts, HttpRequestValues.Builder requestValues) {
		if (CollectionUtils.isEmpty(sorts)) {
			return;
		}
		if (sorts.size() == 1) {
			requestValues.addRequestParameter(sortParameterName, sortToString(sorts.get(0)));
			return;
		}
		for (PageParam.Sort sort : sorts) {
			requestValues.addRequestParameter(sortParameterName, sortToString(sort));
		}
	}

	private String parseValueFromLoong(long value) {
		return String.valueOf(value);
	}

	String sortToString(PageParam.Sort sort) {
		if (!StringUtils.hasText(sort.getField())) {
			return EMPTY_STRING;
		}

		String field = sort.getField();
		String sortBy = sort.isAsc() ? PageableConstants.ASC.toLowerCase() : "desc";
		return field + "," + sortBy;
	}

}
