package com.livk.sso.commons.filter;

import com.livk.commons.web.util.WebUtils;
import com.livk.sso.commons.entity.Payload;
import com.livk.sso.commons.entity.User;
import com.livk.sso.commons.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

/**
 * @author livk
 */
public class TokenVerifyFilter extends BasicAuthenticationFilter {

    public TokenVerifyFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.replaceFirst("Bearer ", "");
            Payload payload = JwtUtils.parse(token);
            User user = payload.getUserInfo();
            if (user != null) {
                UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user,
                        "", user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticated);
                chain.doFilter(request, response);
            } else {
                Map<String, Object> map = Map.of("code", HttpServletResponse.SC_FORBIDDEN, "msg", "缺少用户信息");
                WebUtils.out(response, map);
            }
        } else {
            Map<String, Object> map = Map.of("code", HttpServletResponse.SC_FORBIDDEN, "msg", "请登录！");
            WebUtils.out(response, map);
        }
    }

}
