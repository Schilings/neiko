package com.schilings.neiko.sample.oauth2.config;




import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity(debug = true)
@Configuration
public class DefaultSecurityConfig{
	
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> {
			return User.withDefaultPasswordEncoder().username(username).password("123456")
					.authorities("ROLE_USER","ROLE_ADMIN","neiko:*:*")
					.build();
		};
	}
	
}
