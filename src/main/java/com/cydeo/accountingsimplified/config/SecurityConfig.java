package com.cydeo.accountingsimplified.config;

import com.cydeo.accountingsimplified.service.SecurityService;
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
//                    .antMatchers("/users/**").hasAnyAuthority("Root User","Admin")
//                    .antMatchers("/companies/**").hasAnyAuthority("Root User")
//                    .antMatchers("/category/**").hasAnyAuthority("Admin", "Manager","Employee")
//                    .antMatchers("/product/**").hasAnyAuthority("Admin", "Manager","Employee")
//                    .antMatchers("/invoice/**").hasAnyAuthority("Admin", "Manager","Employee")
//                    .antMatchers("/report/**").hasAnyAuthority("Admin", "Manager")
//                    .antMatchers("/payment/**").hasAnyAuthority("Admin")
//                    .antMatchers("/pdf/**").hasAnyAuthority("Root User","Admin")
                .antMatchers("/", "/login", "/fragments", "/assets/**", "/img/**")
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
                    .tokenValiditySeconds(120)
                    .key("cydeo")
                    .userDetailsService(securityService)
                .and().build();
    }

}
