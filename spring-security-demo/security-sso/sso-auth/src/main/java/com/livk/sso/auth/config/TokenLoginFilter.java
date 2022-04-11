package com.livk.sso.auth.config;

import com.livk.sso.auth.entity.Role;
import com.livk.sso.auth.entity.User;
import com.livk.sso.util.JwtUtils;
import com.livk.util.JacksonUtils;
import com.livk.util.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * TokenLoginFilter
 * </p>
 *
 * @author livk
 * @date 2022/4/11
 */
@RequiredArgsConstructor
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final RsaKeyProperties properties;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User user = JacksonUtils.toBean(request.getInputStream(), User.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            Map<String, ? extends Serializable> map = Map.of("code", HttpServletResponse.SC_UNAUTHORIZED, "msg", "用户名或者密码错误！");
            ResponseUtils.out(response, map);
            throw new UsernameNotFoundException("用户名或者密码错误");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = new User()
                .setUsername(authResult.getName())
                .setRoles((List<Role>) authResult.getAuthorities());
        String token = JwtUtils.generateTokenExpireInMinutes(user, properties.getPrivateKey(), 24 * 60);
        response.addHeader("Authorization", "Bearer ".concat(token));
        Map<String, ? extends Serializable> map = Map.of("code", HttpServletResponse.SC_OK, "data", token);
        ResponseUtils.out(response, map);
    }
}
