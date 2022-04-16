package com.livk.security.handler;

import com.livk.security.support.AuthenticationContext;
import com.livk.security.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("退出");
        String token = request.getHeader("Authorization");
        AuthenticationContext.delete(token);
        ResponseUtils.out(response, Map.of("code", 200, "msg", "exit successfully"));
    }
}
