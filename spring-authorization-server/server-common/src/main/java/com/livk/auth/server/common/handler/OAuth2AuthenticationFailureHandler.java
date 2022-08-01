package com.livk.auth.server.common.handler;

import com.livk.auth.server.common.constant.OAuth2Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
 * <p>
 * OAuth2AuthenticationFailureHandler
 * </p>
 *
 * @author livk
 * @date 2022/7/15
 */
@Slf4j
public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final MappingJackson2HttpMessageConverter errorHttpResponseConverter = new MappingJackson2HttpMessageConverter();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);

        String username = request.getParameter(OAuth2Constants.SMS_PARAMETER_NAME);

        if (OAuth2Constants.PASSWORD.equals(grantType)) {
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
