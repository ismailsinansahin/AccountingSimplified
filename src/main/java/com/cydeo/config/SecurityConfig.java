package com.cydeo.config;

import com.cydeo.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    private final SecurityService securityService;
    private final AuthSuccessHandler authSuccessHandler;

    public SecurityConfig(SecurityService securityService, AuthSuccessHandler authSuccessHandler) {
        this.securityService = securityService;
        this.authSuccessHandler = authSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeRequests()
                    .antMatchers("/users/**").hasAnyAuthority("Root User","Admin")
                    .antMatchers("/companies/**").hasAnyAuthority("Root User")
                    .antMatchers("/", "/login", "fragments", "/assets/**", "/img/**")
                    .permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .successHandler(authSuccessHandler)
                    .failureUrl("/login?error=true")
                    .permitAll()
                .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                .and()
                .rememberMe()
                    .tokenValiditySeconds(86400)
                    .key("cydeo")
                    .userDetailsService(securityService)
                .and().build();
    }

}
