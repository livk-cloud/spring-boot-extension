package com.livk.security.handler;

import com.livk.security.support.AuthenticationContext;
import com.livk.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.Map;

/**
 * <p>
 * LivkLogoutSuccessHandler
 * </p>
 *
 * @author livk
 * @date 2022/3/23
 */
@Slf4j
public class LivkLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("退出");
        String token = request.getHeader("Authorization");
        AuthenticationContext.delete(token);
        WebUtils.out(response, Map.of("code", 200, "msg", "exit successfully"));
    }

}
