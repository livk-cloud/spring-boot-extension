package com.livk.sso.auth.handler;

import com.livk.commons.web.util.WebUtils;
import com.livk.sso.commons.entity.User;
import com.livk.sso.commons.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Map;

/**
 * @author livk
 */
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String token = JwtUtils.generateToken(user);
        log.info("登录成功user:{} token:{}", user, token);
        Map<String, Object> map = Map.of("code", HttpServletResponse.SC_OK, "data", token);
        WebUtils.out(response, map);
    }
}
