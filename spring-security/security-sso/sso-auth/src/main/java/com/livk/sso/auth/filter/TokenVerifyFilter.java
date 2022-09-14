package com.livk.sso.auth.filter;

import com.livk.sso.auth.config.RsaKeyProperties;
import com.livk.sso.auth.entity.User;
import com.livk.sso.entity.Payload;
import com.livk.sso.util.JwtUtils;
import com.livk.util.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.replaceFirst("Bearer ", "");
            Payload<User> payload = JwtUtils.getInfo(token, properties.getPublicKey(), User.class);
            User user = payload.getUserInfo();
            if (user != null) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),
                        user.getPassword(), user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                chain.doFilter(request, response);
            } else {
                Map<String, Object> map = Map.of("code", HttpServletResponse.SC_FORBIDDEN, "msg", "缺少用户信息");
                ResponseUtils.out(response, map);
            }
        } else {
            Map<String, Object> map = Map.of("code", HttpServletResponse.SC_FORBIDDEN, "msg", "请登录！");
            ResponseUtils.out(response, map);
        }
    }

}
