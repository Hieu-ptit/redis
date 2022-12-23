package com.viettel.coreapi.config;


import com.viettel.coreapi.controller.ExceptionController;
import com.viettel.coreapi.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationFilter authenticationFilter;
    private final ExceptionController exceptionController;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth/login", "/accounts/activation/*", "/auth/forgot-password", "/auth/accounts").permitAll()
                .antMatchers(HttpMethod.PUT, "/auth/confirm-forgot-password").permitAll()
                .antMatchers(HttpMethod.GET, "/actuator/health/*").permitAll()
                .anyRequest().authenticated();
        http.exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) ->
                        exceptionController.handleAccessDeniedException(accessDeniedException, request, response))
                .authenticationEntryPoint((request, response, authException) ->
                        exceptionController.handleAuthenticationException(authException, request, response));
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTION", "PATCH", "HEAD", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Origin", "X-Requested-With", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
