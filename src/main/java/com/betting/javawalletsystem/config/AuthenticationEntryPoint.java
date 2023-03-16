package com.betting.javawalletsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
    @Override
    public void commence (HttpServletRequest request, HttpServletResponse response,
                          AuthenticationException authenticationException) throws IOException {
        response.addHeader("WWW-Authenticate", "Basic Realm - " + getRealmName());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        writer.println("HTTP Status 401 - " + authenticationException.getMessage()); //todo: remove me
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("bettingapi");
        super.afterPropertiesSet();
    }
}
