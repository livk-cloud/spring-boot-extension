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

package com.livk.autoconfigure.ip2region.support;

import com.livk.autoconfigure.ip2region.annotation.IP;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.xbill.DNS.Address;

import java.net.UnknownHostException;

/**
 * <p>
 * IPMethodArgumentResolver
 * </p>
 *
 * @author livk
 */
@Slf4j
public class IPMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        IP ip = parameter.getParameterAnnotation(IP.class);
        return ip != null && ip.dns();
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.notNull(request, "request not be null");
        String param = request.getParameter(parameter.getParameterName());
        return DNSForIp(param);
    }

    private String DNSForIp(String ip) {
        try {
            return Address.getByName(ip).getHostAddress();
        } catch (UnknownHostException e) {
            log.error("域名解析失败:{} msg:{}", ip, e.getMessage(), e);
            return ip;
        }
    }
}
