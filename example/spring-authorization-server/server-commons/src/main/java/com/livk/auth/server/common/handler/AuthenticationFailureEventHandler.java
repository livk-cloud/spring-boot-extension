package com.livk.auth.server.common.handler;

import com.livk.auth.server.common.constant.SecurityConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * @author livk
 */
@Slf4j
public class AuthenticationFailureEventHandler implements AuthenticationFailureHandler {

    private final MappingJackson2HttpMessageConverter errorHttpResponseConverter = new MappingJackson2HttpMessageConverter();

    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     *                  request.
     */
    @Override
    @SneakyThrows
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {

        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);

        String username = request.getParameter(SecurityConstants.SMS_PARAMETER_NAME);

        if (SecurityConstants.PASSWORD.equals(grantType)) {
            username = request.getParameter(OAuth2ParameterNames.USERNAME);
        }

        log.info("用户：{} 登录失败，异常：{}", username, exception.getLocalizedMessage());

        // 写出错误信息
        sendErrorResponse(request, response, exception);
    }

    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();

        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

        httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);

        this.errorHttpResponseConverter.write(ResponseEntity.status(403).body(error.getDescription()), MediaType.APPLICATION_JSON, httpResponse);
    }

}
