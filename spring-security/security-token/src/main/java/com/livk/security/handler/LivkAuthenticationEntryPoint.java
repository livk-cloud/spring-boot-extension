package com.livk.security.handler;


import com.livk.commons.web.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.util.Map;

/**
 * <p>
 * LivkAuthenticationEntryPoint
 * </p>
 *
 * @author livk
 */
@Slf4j
public class LivkAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
        log.error("异常：{}", authException.getMessage());
        authException.printStackTrace();
        WebUtils.out(response, Map.of("code", HttpStatus.FORBIDDEN.value(), "msg", authException.getMessage()));
    }

}
