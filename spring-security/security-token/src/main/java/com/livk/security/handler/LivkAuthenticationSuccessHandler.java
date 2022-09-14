package com.livk.security.handler;

import com.livk.security.support.AuthenticationContext;
import com.livk.util.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * LivkAuthenticationSuccessHandler
 * </p>
 *
 * @author livk
 * @date 2022/3/23
 */
@Slf4j
public class LivkAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        log.info("登录成功处理");
        String token = UUID.randomUUID().toString();
        AuthenticationContext.setTokenAndAuthentication(token, authentication);
        ResponseUtils.out(response, Map.of("code", 200, "data", token));
    }

}
