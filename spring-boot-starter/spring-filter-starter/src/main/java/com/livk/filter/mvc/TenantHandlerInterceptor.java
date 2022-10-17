package com.livk.filter.mvc;

import com.livk.filter.context.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

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
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler)
            throws Exception {
        TenantContextHolder.setTenantId(request.getHeader(TenantContextHolder.ATTRIBUTES));
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex) {
        TenantContextHolder.remove();
    }

}
