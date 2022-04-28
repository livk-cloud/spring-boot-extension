package com.livk.security.handler;

import com.livk.security.support.AuthenticationContext;
import com.livk.security.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * LivkAuthenticationSuccessHandler
 * </p>
 *
 * @author livk
 * @date 2022/3/23
 */
@Slf4j
public class LivkAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("登录成功处理");
		var token = UUID.randomUUID().toString();
		AuthenticationContext.setTokenAndAuthentication(token, authentication);
		ResponseUtils.out(response, Map.of("code", 200, "data", token));
	}

}
