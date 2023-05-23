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

package com.livk.autoconfigure.useragent.servlet;

import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;
import com.livk.commons.bean.Wrapper;
import com.livk.commons.web.util.WebUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * <p>
 * AbstractUserAgentFilter
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public class UserAgentFilter extends OncePerRequestFilter {

    private final HttpUserAgentParser userAgentParse;

    @Override
    protected final void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpHeaders headers = WebUtils.headers(request);
        Wrapper useragentWrapper = userAgentParse.parse(headers);
        UserAgentContextHolder.setUserAgentContext(useragentWrapper);
        filterChain.doFilter(request, response);
        UserAgentContextHolder.cleanUserAgentContext();
    }
}
