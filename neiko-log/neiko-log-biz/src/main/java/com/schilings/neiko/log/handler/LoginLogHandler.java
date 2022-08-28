package com.schilings.neiko.log.handler;

import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.schilings.neiko.common.log.constants.LogConstant;
import com.schilings.neiko.common.log.operation.enums.LogStatusEnum;
import com.schilings.neiko.common.security.constant.SecurityConstants;
import com.schilings.neiko.common.util.ip.IpUtils;
import com.schilings.neiko.common.util.web.WebUtils;
import com.schilings.neiko.extend.sa.token.oauth2.event.authentication.AbstractAuthenticationFailureEvent;
import com.schilings.neiko.extend.sa.token.oauth2.event.authentication.AuthenticationSuccessEvent;
import com.schilings.neiko.extend.sa.token.oauth2.event.authentication.LogoutSuccessEvent;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.Authentication;
import com.schilings.neiko.log.enums.LoginEventTypeEnum;
import com.schilings.neiko.log.model.entity.LoginLog;
import com.schilings.neiko.log.service.LoginLogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.context.event.EventListener;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;

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
    @EventListener(AuthenticationSuccessEvent.class)
    public void onAuthenticationSuccessEvent(AuthenticationSuccessEvent event) {

        Authentication source = (Authentication) event.getSource();
        Object details = source.getTokenDetails();
        if (!(details instanceof HashMap)) {
            return;
        }
        // TODO 暂时只记录了 password 模式，其他的第三方登陆，等 spring-authorization-server 孵化后重构
        // https://github.com/spring-projects-experimental/spring-authorization-server
        if (SecurityConstants.GrantType.password.equals(((HashMap) details).get(SecurityConstants.Param.grant_type))) {
            LoginLog loginLog = prodLoginLog(source)
                    .setMsg("登陆成功")
                    .setClientId(Long.valueOf((String) ((HashMap) details).get(SecurityConstants.Param.client_id)))
                    .setStatus(LogStatusEnum.SUCCESS.getValue())
                    .setEventType(LoginEventTypeEnum.LOGIN.getValue());
            loginLogService.save(loginLog);
        }
    }


    /**
     * 注销成功事件
     * @param event
     */
    @EventListener(LogoutSuccessEvent.class)
    public void onLogoutSuccessEvent(LogoutSuccessEvent event) {
        Authentication source = (Authentication) event.getSource();
        LoginLog loginLog = prodLoginLog(source)
                .setMsg("登出成功")
                .setEventType(LoginEventTypeEnum.LOGOUT.getValue());
        loginLogService.save(loginLog);
    }

    /**
     * 监听鉴权失败事件
     * @param event the event
     */
    @EventListener(AbstractAuthenticationFailureEvent.class)
    public void onAuthenticationFailureEvent(AbstractAuthenticationFailureEvent event) {
        Authentication source = (Authentication) event.getSource();
        LoginLog loginLog = prodLoginLog(source)
                .setMsg(event.getException().getMessage())
                .setEventType(LoginEventTypeEnum.LOGIN.getValue())
                .setStatus(LogStatusEnum.FAIL.getValue());
        loginLogService.save(loginLog);
    }
    

    /**
     * 根据Authentication和请求信息产生一个登陆日志
     * @param source Authentication 
     * @return LoginLog 登陆日志
     */
    private LoginLog prodLoginLog(Authentication source) {
        // 获取 Request
        HttpServletRequest request = WebUtils.getRequest();
        LoginLog loginLog = new LoginLog()
                .setLoginTime(LocalDateTime.now())
                .setIp(IpUtils.getIpAddr(request))
                .setStatus(LogStatusEnum.SUCCESS.getValue())
                .setTraceId(MDC.get(LogConstant.TRACE_ID))
                .setUsername(source.getUserDetails().getUsername());
        // 根据 ua 获取浏览器和操作系统
        UserAgent ua = UserAgentUtil.parse(request.getHeader("user-agent"));
        if (ua != null) {
            loginLog.setBrowser(ua.getBrowser().getName()).setOs(ua.getOs().getName());
        }
        return loginLog;
    }
    
    
}
