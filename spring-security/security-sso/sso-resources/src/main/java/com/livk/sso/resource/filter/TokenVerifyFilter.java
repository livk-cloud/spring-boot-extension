package com.livk.sso.resource.filter;

import com.livk.sso.commons.RsaKeyProperties;
import com.livk.sso.commons.entity.Payload;
import com.livk.sso.commons.entity.User;
import com.livk.sso.commons.util.JwtUtils;
import com.livk.util.WebUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

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
public class TokenVerifyFilter extends BasicAuthenticationFilter {

    private final RsaKeyProperties properties;

    public TokenVerifyFilter(AuthenticationManager authenticationManager, RsaKeyProperties properties) {
        super(authenticationManager);
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String authorization = request.getHeader("Authorization");
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
