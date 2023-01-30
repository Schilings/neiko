package com.schilings.neiko.security.oauth2.core;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.util.Map;

/**
 * 修改parameterMap
 *
 */
public class ModifyParamMapRequestWrapper extends HttpServletRequestWrapper {

	private final Map<String, String[]> parameterMap;

	public ModifyParamMapRequestWrapper(HttpServletRequest request, Map<String, String[]> parameterMap) {
		super(request);
		this.parameterMap = parameterMap;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return this.parameterMap;
	}

}