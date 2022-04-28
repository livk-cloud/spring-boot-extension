package com.livk.sso.auth.config;

import com.livk.sso.auth.entity.Role;
import com.livk.sso.auth.entity.User;
import com.livk.sso.util.JwtUtils;
import com.livk.util.JacksonUtils;
import com.livk.util.ResponseUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
public class TokenLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login", "POST");

    private final AuthenticationManager authenticationManager;
    private final RsaKeyProperties properties;

    public TokenLoginFilter(AuthenticationManager authenticationManager, RsaKeyProperties properties) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.authenticationManager = authenticationManager;
        this.properties = properties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            var user = JacksonUtils.toBean(request.getInputStream(), User.class);
            var authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            var map = Map.of("code", HttpServletResponse.SC_UNAUTHORIZED, "msg", "用户名或者密码错误！");
            ResponseUtils.out(response, map);
            throw new UsernameNotFoundException("用户名或者密码错误");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        var user = new User()
                .setUsername(authResult.getName())
                .setRoles((List<Role>) authResult.getAuthorities());
        var token = JwtUtils.generateTokenExpireInMinutes(user, properties.getPrivateKey(), 24 * 60);
        response.addHeader("Authorization", "Bearer ".concat(token));
        var map = Map.of("code", HttpServletResponse.SC_OK, "data", token);
        ResponseUtils.out(response, map);
    }
}
