package com.livk.auth.server.common.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * <p>
 * FormAuthenticationFailureHandler
 * </p>
 *
 * @author livk
 * @date 2022/7/18
 */
@Slf4j
public class FormAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.debug("表单登录失败:{}", exception.getLocalizedMessage());
        String url = String.format("/token/login?error=%s", exception.getMessage());
        response.sendRedirect(url);
    }
}
