/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.filter.mvc;

import com.livk.filter.FilterAutoConfiguration;
import com.livk.filter.context.TenantContextHolder;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import org.springframework.core.annotation.Order;

/**
 * <p>
 * 使用bean方式{@link FilterAutoConfiguration#filterRegistrationBean()}
 * </p>
 * <p>
 * 使用注解{@link WebFilter}和{@link org.springframework.boot.web.servlet.ServletComponentScan}
 * </p>
 *
 * @author livk
 */
@Order(1)
@WebFilter(filterName = "tenantFilter", urlPatterns = "/*")
public class TenantFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		TenantContextHolder.setTenantId(((HttpServletRequest) request).getHeader(TenantContextHolder.ATTRIBUTES));
		HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) request);
		chain.doFilter(requestWrapper, response);
		TenantContextHolder.remove();
	}

}
