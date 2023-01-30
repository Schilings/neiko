package com.schilings.neiko.samples.authorization.authentication;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class CustomUserDetailServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return User.withUsername(username).password("123456")
                .passwordEncoder(passwordEncoder::encode)
                .accountExpired(false).accountLocked(false).disabled(false).credentialsExpired(false)
                .authorities(
                        "ROLE_USER",
                        "ROLE_ADMIN",
                        "ROLE_CUSTOMER",
                        "authorization:*:*",
                        "sys:user:*",
                        "sys:user:add",
                        "sys:user:edit",
                        "sys:user:del",
                        "sys:user:read",
                        "sys:menu:*",
                        "sys:organization:*",
                        "sys:role:*",
                        "sys:log:*",
                        "sys:notify:*",
                        "sys:file:*"
                )
                .build();
    }
    
}
