package com.cydeo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Configuration
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("Root User")) {
            response.sendRedirect("/companies/list");
        } else if (roles.contains("Admin")) {
            response.sendRedirect("/users/list");
        } else if (roles.contains("Manager")) {
            response.sendRedirect("/dashboard");
        } else if (roles.contains("Employee")) {
            response.sendRedirect("/dashboard");
        }
    }

}
