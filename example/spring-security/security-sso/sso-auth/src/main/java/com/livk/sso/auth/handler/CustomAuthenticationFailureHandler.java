package com.livk.sso.auth.handler;

import com.livk.commons.web.util.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.Map;

/**
 * @author livk
 */
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("异常：{}", exception.getMessage());
        exception.printStackTrace();
        WebUtils.out(response, Map.of("code", HttpStatus.BAD_REQUEST, "msg",
                "login failed, username or password is incorrect"));
    }
}
