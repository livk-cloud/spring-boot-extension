/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.socket.session.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <p>
 * ServerConfigurator
 * </p>
 *
 * @author livk
 */
@Slf4j
public class ServerConfigurator extends ServerEndpointConfig.Configurator {

	@Override
	public boolean checkOrigin(String originHeaderValue) {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
			.getRequestAttributes();
		Assert.notNull(servletRequestAttributes, "servletRequestAttributes not be null!");
		HttpServletRequest request = servletRequestAttributes.getRequest();
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		if ("livk123".equals(token)) {
			return super.checkOrigin(originHeaderValue);
		}
		else {
			log.info("缺少参数!");
			return false;
		}
	}

}
