package com.schilings.neiko.samples.authorization;

import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.resource.server.OAuth2ResourceServerConfigurerCustomizer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Collections;

@MapperScan({ "com.schilings.neiko.**.mapper" })
@ComponentScan({ "com.schilings.neiko.samples.authorization", "com.schilings.neiko.authorization" })
@SpringBootApplication
// @EnableWebSecurity(debug = true)
public class AuthorizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationApplication.class, args);
	}

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

	@Bean
	public OAuth2ResourceServerConfigurerCustomizer resourceServerConfigurerCustomizer() {
		return http -> {
			// 适配h2 console页面
			http.headers().frameOptions().disable();
		};
	}

}
