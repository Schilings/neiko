package com.schilings.neiko.security.oauth2.authorization.server.customizer.formlogin;

import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.properties.OAuth2AuthorizationServerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

@Order(Ordered.LOWEST_PRECEDENCE - 1000)
@RequiredArgsConstructor
public class FormLoginRememberMeConfigurerCustomizer implements OAuth2AuthorizationServerConfigurerCustomizer {

	private static final String DEFAULT_LOGIN_URL = "/login";

	private final UserDetailsService userDetailsService;

	private boolean enabled = false;

	private String loginPage;

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer,
			HttpSecurity httpSecurity) throws Exception {
		if (enabled) {
			// 例如授权码模式请求时，未登录，开启表单登录，需要授权,
			// 注意，单独只有一个授权服务端的SecurityFilterChain，这个时候就不要自定义AuthenticationEntryPoint，不然DefaultLoginPageGeneratingFilter不注入
			// 记住！如果是通过这个SecurityFilterChain认证成功之后的，那么后续的请求如果不匹配到这个Chain
			// 那么其SecurityContextHolder就不会设置认证的Authentication
			// (SecurityContextHolderAwareRequestFilter)

			String loginPageUrl = StringUtils.hasText(loginPage) ? loginPage : DEFAULT_LOGIN_URL;
			// 操作RequestMatcherConfigurer，留给后面的Configurer操作空间
			httpSecurity.securityMatchers().requestMatchers(new AntPathRequestMatcher(loginPageUrl));
			// 避免嵌套重定向
			httpSecurity.authorizeHttpRequests().requestMatchers(new AntPathRequestMatcher(loginPageUrl)).permitAll();

			// Form Login
			// FormLoginConfigurer会装配一个LoginUrlAuthenticationEntryPoint ->
			// AbstractAuthenticationFilterConfigurer.registerAuthenticationEntryPoint()
			// 一定有一个LoginUrlAuthenticationEntryPoint(loginPage)注入为default的AuthenticationEntryPoint之一
			if (StringUtils.hasText(loginPage)) {
				httpSecurity.formLogin().loginPage(loginPageUrl);
			}
			else {
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
	}

	public FormLoginRememberMeConfigurerCustomizer enabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public FormLoginRememberMeConfigurerCustomizer loginPage(String loginPage) {
		this.loginPage = loginPage;
		return this;
	}

}
