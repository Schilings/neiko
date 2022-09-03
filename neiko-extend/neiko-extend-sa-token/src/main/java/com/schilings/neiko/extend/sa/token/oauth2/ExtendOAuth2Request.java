package com.schilings.neiko.extend.sa.token.oauth2;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.servlet.model.SaRequestForServlet;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.hutool.http.ContentType;
import com.schilings.neiko.common.core.request.wrapper.RepeatBodyRequestWrapper;
import com.schilings.neiko.common.core.request.wrapper.RepeatJsonBodyRequestWrapper;
import com.schilings.neiko.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * <p>
 * </p>
 *
 * @author Schilings
 */
@Slf4j
public class ExtendOAuth2Request implements SaRequest {

	private HttpServletRequestWrapper requestWrapper = null;

	private boolean parsedJsonBody = false;

	public ExtendOAuth2Request(SaRequest req) {
		if (req instanceof SaRequestForServlet) {
			HttpServletRequest source = (HttpServletRequest) req.getSource();
			// Json
			if (source instanceof RepeatJsonBodyRequestWrapper) {
				requestWrapper = (RepeatJsonBodyRequestWrapper) source;
				parsedJsonBody = true;
			}
			else if (source instanceof RepeatBodyRequestWrapper) {
				requestWrapper = new RepeatJsonBodyRequestWrapper((RepeatBodyRequestWrapper) source);
				parsedJsonBody = true;
			}
			else {
				// 不Json
				if (source instanceof HttpServletRequestWrapper) {
					requestWrapper = (HttpServletRequestWrapper) source;
					parsedJsonBody = false;
				}
				else {
					requestWrapper = new HttpServletRequestWrapper(source);
					parsedJsonBody = false;
				}
				log.debug("ExtendOauth2Request 不做Json处理");
			}
		}
	}

	/**
	 * 从JsonBody取出参数值
	 * @param name
	 * @return
	 */
	public String getFromJsonBody(String name) {
		if (parsedJsonBody) {
			return (String) ((RepeatJsonBodyRequestWrapper) requestWrapper).getJsonMap().get(name);
		}
		return null;
	}

	/**
	 * 获取底层源对象
	 */
	@Override
	public Object getSource() {
		return requestWrapper.getRequest();
	}

	/**
	 * 在 [请求体] 里获取一个值
	 */
	@Override
	public String getParam(String name) {
		// 首先考虑QueryParam
		String parameter = requestWrapper.getParameter(name);
		if (StringUtils.isBlank(parameter) && requestWrapper.getContentType().equals(ContentType.JSON.getValue())) {
			// 再考虑JsonBody("application/json")
			parameter = getFromJsonBody(name);
		}
		return parameter;
	}

	/**
	 * 在 [请求头] 里获取一个值
	 */
	@Override
	public String getHeader(String name) {
		return requestWrapper.getHeader(name);
	}

	/**
	 * 在 [Cookie作用域] 里获取一个值
	 */
	@Override
	public String getCookieValue(String name) {
		Cookie[] cookies = requestWrapper.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie != null && name.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * 返回当前请求path (不包括上下文名称)
	 */
	@Override
	public String getRequestPath() {
		return requestWrapper.getServletPath();
	}

	/**
	 * 返回当前请求的url，例：http://xxx.com/test
	 * @return see note
	 */
	@Override
	public String getUrl() {
		String currDomain = SaManager.getConfig().getCurrDomain();
		if (SaFoxUtil.isEmpty(currDomain) == false) {
			return currDomain + this.getRequestPath();
		}
		return requestWrapper.getRequestURL().toString();
	}

	/**
	 * 返回当前请求的类型
	 */
	@Override
	public String getMethod() {
		return requestWrapper.getMethod();
	}

	/**
	 * 转发请求
	 */
	@Override
	public Object forward(String path) {
		try {
			HttpServletResponse response = (HttpServletResponse) SaManager.getSaTokenContextOrSecond().getResponse()
					.getSource();
			requestWrapper.getRequestDispatcher(path).forward(requestWrapper, response);
			return null;
		}
		catch (ServletException | IOException e) {
			throw new SaTokenException(e);
		}
	}

}
