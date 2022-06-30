package com.livk.security.handler;


import com.livk.util.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * LivkAuthenticationEntryPoint
 * </p>
 *
 * @author livk
 * @date 2022/3/23
 */
@Slf4j
public class LivkAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.error("异常：{}", authException.getMessage());
        authException.printStackTrace();
        ResponseUtils.out(response, Map.of("code", HttpStatus.FORBIDDEN.value(), "msg", authException.getMessage()));
    }

}
