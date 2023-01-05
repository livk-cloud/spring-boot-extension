package com.livk.sso.auth.filter;

import com.livk.commons.web.util.WebUtils;
import com.livk.sso.commons.RsaKeyProperties;
import com.livk.sso.commons.entity.Payload;
import com.livk.sso.commons.entity.User;
import com.livk.sso.commons.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
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
 */
@RequiredArgsConstructor
public class TokenVerifyFilter extends OncePerRequestFilter {

    private final RsaKeyProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws IOException, ServletException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.replaceFirst("Bearer ", "");
            Payload payload = JwtUtils.parse(token, properties.rsaKey());
            User user = payload.getUserInfo();
            if (user != null) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),
                        user.getPassword(), user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
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
