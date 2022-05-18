package com.livk.filter.mvc;

import com.livk.filter.context.TenantContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * TenantHandlerInterceptor
 * </p>
 *
 * @author livk
 * @date 2022/5/10
 */
public class TenantHandlerInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		TenantContext.setTenantId(request.getHeader(TenantContext.ATTRIBUTES));
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		TenantContext.remove();
	}

}
