package com.schilings.neiko.autoconfigure.web.api.condition;

import com.schilings.neiko.common.util.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;

/**
 * 见RequestMappingInfo，这里面有多个RequestCondition实现类，借鉴借鉴
 */
public class ApiVesrsionCondition implements RequestCondition<ApiVesrsionCondition> {

	private int apiVersion;

	private String version;

	public ApiVesrsionCondition() {
		super();
	}

	public ApiVesrsionCondition(int apiVersion, String version) {
		Assert.state(StringUtils.isNotBlank(version) && version.contains("."),
				"The ApiVersion format can not be allowed.");
		this.apiVersion = apiVersion;
		this.version = version;
	}

	/**
	 * 将不同的筛选条件合并,这里采用的覆盖，即后来的规则生效
	 * @param other
	 * @return
	 */
	@Override
	public ApiVesrsionCondition combine(ApiVesrsionCondition other) {
		return new ApiVesrsionCondition(other.getApiVersion(), other.getVersion());
	}

	/**
	 * 根据request查找匹配到的筛选条件
	 * @param request
	 * @return
	 */
	@Override
	public ApiVesrsionCondition getMatchingCondition(HttpServletRequest request) {
		String versionStr = request.getParameter("version");
		// 如果请求中没有传入版本号，默认适配最大版本
		if (StringUtils.isBlank(versionStr)) {
			return this;
		}
		// 如果请求的版本号大于配置版本号， 则满足，即与请求的
		int version = ApiVesrsionCondition.versionStrToNum(versionStr);
		if (version >= this.apiVersion) {
			return this;
		}
		// 否则返回404，找不到适配的接口
		return null;
	}

	public static int versionStrToNum(String versionStr) {
		Assert.state(StringUtils.isNotBlank(versionStr) && versionStr.contains("."),
				"The ApiVersion format can not be allowed.");
		String[] arr = versionStr.split("\\.");
		StringBuilder sb = new StringBuilder();
		for (String num : arr) {
			String str = String.format("%03d", Integer.valueOf(num));
			sb.append(str);
		}
		int version = Integer.valueOf(sb.toString());
		return version;
	}

	/**
	 * 排序选出最佳匹配的时候用到 实现不同条件类的比较，从而实现优先级排序
	 * @param other
	 * @param request
	 * @return
	 */
	@Override
	public int compareTo(ApiVesrsionCondition other, HttpServletRequest request) {
		return other.getApiVersion() - this.apiVersion;
	}

	public int getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(int apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
