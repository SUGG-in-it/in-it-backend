package com.example.initbackend.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

public class OAuthConfig {
    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.oauth2Login()
                .authorizationEndpoint()
                .baseUri("/api/users/login/github");
        return http.build();
    }
}