package com.schilings.neiko.security.oauth2.authorization.server.configurer;

import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerExtensionConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

@Order(Ordered.HIGHEST_PRECEDENCE + 100)
public class FormLoginRememberMeConfigurer
		extends OAuth2AuthorizationServerExtensionConfigurer<FormLoginRememberMeConfigurer, HttpSecurity> {

	private static final String DEFAULT_LOGIN_URL = "/login";

	private String loginPageUrl;

	private final UserDetailsService userDetailsService;

	public FormLoginRememberMeConfigurer(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void init(HttpSecurity httpSecurity) throws Exception {
		// 例如授权码模式请求时，未登录，开启表单登录，需要授权,
		// 注意，单独只有一个授权服务端的SecurityFilterChain，这个时候就不要自定义AuthenticationEntryPoint，不然DefaultLoginPageGeneratingFilter不注入
		// 记住！如果是通过这个SecurityFilterChain认证成功之后的，那么后续的请求如果不匹配到这个Chain
		// 那么其SecurityContextHolder就不会设置认证的Authentication
		// (SecurityContextHolderAwareRequestFilter)

		String loginPage = StringUtils.hasText(this.loginPageUrl) ? this.loginPageUrl : DEFAULT_LOGIN_URL;
		// 操作RequestMatcherConfigurer，留给后面的Configurer操作空间
		httpSecurity.requestMatchers().antMatchers(loginPage);
		// 避免嵌套重定向
		httpSecurity.authorizeRequests().antMatchers(loginPage).permitAll();

		// FormLogin
		// FormLoginConfigurer会装配一个LoginUrlAuthenticationEntryPoint ->
		// AbstractAuthenticationFilterConfigurer.registerAuthenticationEntryPoint()
		// 一定有一个LoginUrlAuthenticationEntryPoint(loginPage)注入为default的AuthenticationEntryPoint之一
		if (StringUtils.hasText(this.loginPageUrl)) {
			httpSecurity.formLogin().loginPage(loginPage);
		} else {
			// 设置loginPage(loginPage)会导致不自动生成登录页面，DefaultLoginPageGeneratingFilter不注入
			httpSecurity.formLogin(Customizer.withDefaults());
		}

		// remember me
		httpSecurity.rememberMe(rememberMe -> {
			rememberMe.alwaysRemember(false);
			// rememberMe.rememberMeParameter("remember-me");
			// rememberMe.rememberMeCookieName("remember-me");
		});

		// 需要 userDetailsService 对应生成 DaoAuthenticationProvider
		httpSecurity.userDetailsService(userDetailsService);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		ExceptionHandlingConfigurer<HttpSecurity> exceptionHandling = http
				.getConfigurer(ExceptionHandlingConfigurer.class);
		if (exceptionHandling == null) {
			return;
		}
	}

	public void setLoginPageUrl(String loginPageUrl) {
		this.loginPageUrl = loginPageUrl;
	}

}
