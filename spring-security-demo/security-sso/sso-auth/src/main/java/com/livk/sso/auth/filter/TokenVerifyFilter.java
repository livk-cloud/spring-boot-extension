package com.livk.sso.auth.filter;

import com.livk.sso.auth.config.RsaKeyProperties;
import com.livk.sso.auth.entity.User;
import com.livk.sso.util.JwtUtils;
import com.livk.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * TokenVerifyFilter
 * </p>
 *
 * @author livk
 * @date 2022/4/11
 */
@RequiredArgsConstructor
public class TokenVerifyFilter extends OncePerRequestFilter {

    private final RsaKeyProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        var authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            var token = authorization.replaceFirst("Bearer ", "");
            var payload = JwtUtils.getInfo(token, properties.getPublicKey(), User.class);
            var user = payload.getUserInfo();
            if (user != null) {
                var authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                chain.doFilter(request, response);
            } else {
                var map = Map.of("code", HttpServletResponse.SC_FORBIDDEN, "msg", "缺少用户信息");
                ResponseUtils.out(response, map);
            }
        } else {
            var map = Map.of("code", HttpServletResponse.SC_FORBIDDEN, "msg", "请登录！");
            ResponseUtils.out(response, map);
        }
    }
}
