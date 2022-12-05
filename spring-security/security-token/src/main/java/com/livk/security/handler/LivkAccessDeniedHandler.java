package com.livk.security.handler;


import com.livk.commons.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.Map;

/**
 * <p>
 * LivkAccessDeniedHandler
 * </p>
 *
 * @author livk
 */
@Slf4j
public class LivkAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {
        log.error("exception:{}", accessDeniedException.getMessage());
        accessDeniedException.printStackTrace();
        WebUtils.out(response, Map.of("code", 403, "msg", accessDeniedException.getMessage()));
    }

}
