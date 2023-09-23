package com.schilings.neiko.sample.authorization.server.config;

import com.schilings.neiko.security.oauth2.authorization.server.HttpSecurityAware;
import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerConfigurerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Collections;

@EnableWebSecurity(debug = true)
@Configuration
public class DefaultSecurityConfig implements HttpSecurityAware {

	private HttpSecurity httpSecurity;

	@Bean
	public UserDetailsService userDetailsService() {
		return username -> {
			return User.withDefaultPasswordEncoder().username(username).password("123456").authorities("ROLE_USER")// ,"ROLE_ADMIN","neiko:*:*"
					.build();
		};
	}

	@Override
	public void setHttpSecurity(HttpSecurity httpSecurity) {
		this.httpSecurity = httpSecurity;
	}

	/**
	 * 解决接口文档调用跨域问题
	 * @return
	 */
	@Bean
	public OAuth2AuthorizationServerConfigurerCustomizer authorizationServerConfigurerCustomizer() {
		return (configuer, http) -> {
			// 解决测试跨域， 开启Spring Security 对 CORS 的支持,逻辑简单，点进去看看
			// 跨域问题：https://blog.csdn.net/Hongyu_Liu/article/details/118930061,
			// 解决方法：https://blog.csdn.net/xu_hui123/article/details/128231972
			http.securityMatchers().requestMatchers(new AntPathRequestMatcher("/oauth2/**", HttpMethod.OPTIONS.name()));
			http.cors().configurationSource(corsConfigurationSource());

		};
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
		configuration.setAllowedMethods(Collections.singletonList("*"));
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		configuration.setMaxAge(Duration.ofHours(1));
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
