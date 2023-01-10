package com.schilings.neiko.sample.authorization.server.config;


import com.schilings.neiko.security.oauth2.authorization.server.HttpSecurityAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;



@EnableWebSecurity(debug = true)
@Configuration
public class DefaultSecurityConfig implements HttpSecurityAware {

	private HttpSecurity httpSecurity;
	
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password").roles("USER")
				.build();
		return new InMemoryUserDetailsManager(user);
	}
	

	@Override
	public void setHttpSecurity(HttpSecurity httpSecurity) {
		this.httpSecurity = httpSecurity;
	}

}
