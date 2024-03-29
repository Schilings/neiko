package com.schilings.neiko.log.handler;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.schilings.neiko.authorization.common.constant.UserAttributeNameConstants;
import com.schilings.neiko.authorization.common.event.OAuth2AccessTokenAuthenticationSuccessEvent;
import com.schilings.neiko.authorization.common.event.OAuth2TokenRevocationAuthenticationSuccessEvent;
import com.schilings.neiko.authorization.common.userdetails.User;
import com.schilings.neiko.authorization.common.util.SecurityUtils;
import com.schilings.neiko.common.log.constants.LogConstant;
import com.schilings.neiko.common.log.operation.enums.LogStatusEnum;
import com.schilings.neiko.common.util.ip.IpUtils;
import com.schilings.neiko.common.util.web.WebUtils;
import com.schilings.neiko.log.enums.LoginEventTypeEnum;
import com.schilings.neiko.log.model.entity.LoginLog;
import com.schilings.neiko.log.service.LoginLogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 登陆成功监听器
 *
 */
@RequiredArgsConstructor
public class LoginLogHandler {

	private final LoginLogService loginLogService;

	/**
	 * 登陆成功时间监听 记录用户登录日志
	 * @param event 登陆成功 event
	 */
	@Async
	@EventListener(OAuth2AccessTokenAuthenticationSuccessEvent.class)
	public void onAuthenticationSuccessEvent(OAuth2AccessTokenAuthenticationSuccessEvent event) {
		LoginLog loginLog = prodLoginLog(event).setMsg("登陆成功").setStatus(LogStatusEnum.SUCCESS.getValue())
				.setEventType(LoginEventTypeEnum.LOGIN.getValue());
		loginLogService.save(loginLog);
	}

	/**
	 * 注销成功事件
	 * @param event
	 */
	@Async
	@EventListener(OAuth2TokenRevocationAuthenticationSuccessEvent.class)
	public void onLogoutSuccessEvent(OAuth2TokenRevocationAuthenticationSuccessEvent event) {
		LoginLog loginLog = prodLogoutLog(event).setMsg("登出成功").setEventType(LoginEventTypeEnum.LOGOUT.getValue());
		loginLogService.save(loginLog);
	}

	/**
	 * 根据Authentication和请求信息产生一个登陆日志
	 * @param event AuthenticationEvent
	 * @return LoginLog 登陆日志
	 */
	private LoginLog prodLoginLog(OAuth2AccessTokenAuthenticationSuccessEvent event) {
		// 获取 Request
		// HttpServletRequest request = WebUtils.getRequest();
		Authentication authentication = event.getAuthentication();
		LoginLog loginLog = new LoginLog().setLoginTime(LocalDateTime.now())
				.setIp(IpUtils.getIpAddr(event.getRequest())).setStatus(LogStatusEnum.SUCCESS.getValue())
				.setTraceId(MDC.get(LogConstant.TRACE_ID)).setUsername(authentication.getName());
		// clientId
		loginLog.setClientId(event.getClientId());
		UserAgent ua = UserAgentUtil.parse(event.getRequest().getHeader("user-agent"));
		if (ua != null) {
			loginLog.setBrowser(ua.getBrowser().getName()).setOs(ua.getOs().getName());
		}
		return loginLog;
	}

	private LoginLog prodLogoutLog(OAuth2TokenRevocationAuthenticationSuccessEvent event) {
		// 获取 Request
		// HttpServletRequest request = WebUtils.getRequest();
		Authentication authentication = event.getAuthentication();
		LoginLog loginLog = new LoginLog().setLoginTime(LocalDateTime.now())
				.setIp(IpUtils.getIpAddr(event.getRequest())).setStatus(LogStatusEnum.SUCCESS.getValue())
				.setTraceId(MDC.get(LogConstant.TRACE_ID)).setUsername(authentication.getName());
		// 根据 ua 获取浏览器和操作系统
		UserAgent ua = UserAgentUtil.parse(event.getRequest().getHeader("user-agent"));
		if (ua != null) {
			loginLog.setBrowser(ua.getBrowser().getName()).setOs(ua.getOs().getName());
		}
		return loginLog;
	}

}
