package com.livk.security.handler;

import com.livk.security.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * LivkAuthenticationFailureHandler
 * </p>
 *
 * @author livk
 * @date 2022/3/23
 */
@Slf4j
public class LivkAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("异常：{}", exception.getMessage());
        exception.printStackTrace();
        ResponseUtils.out(response, Map.of("code", HttpStatus.BAD_REQUEST.value(),"msg","login failed, username or password is incorrect"));
    }
}
