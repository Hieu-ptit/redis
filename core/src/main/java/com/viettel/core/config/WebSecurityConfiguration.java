package com.viettel.core.config;

import com.viettel.core.exception.ExceptionController;
import com.viettel.core.security.XAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final XAuthenticationFilter authenticationFilter;
    private final ExceptionController commonsExceptionController;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/*", "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/*", "/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/*", "/**").permitAll()
                .antMatchers(HttpMethod.PATCH, "/*", "/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/*", "/**").permitAll()
                .antMatchers(HttpMethod.GET,
                        "/swagger-ui/**", "/swagger-resources", "/swagger-resources/**", "/v2/api-docs", "/actuator/**").permitAll()
                .anyRequest().authenticated();

        http.exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) ->
                        commonsExceptionController.handleAccessDeniedException(accessDeniedException, request, response))
                .authenticationEntryPoint((request, response, authException) ->
                        commonsExceptionController.handleAuthenticationException(authException, request, response));

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
