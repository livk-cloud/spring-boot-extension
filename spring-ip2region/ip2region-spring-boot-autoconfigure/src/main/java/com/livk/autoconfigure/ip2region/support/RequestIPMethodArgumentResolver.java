package com.livk.autoconfigure.ip2region.support;

import com.livk.autoconfigure.ip2region.annotation.RequestIp;
import com.livk.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * <p>
 * RequestIPMethodArgumentResolver
 * </p>
 *
 * @author livk
 * @date 2022/11/1
 */
public class RequestIPMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestIp.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getParameterType().isAssignableFrom(String.class)) {
            HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
            Assert.notNull(request, "request not be null");
            return WebUtils.getRealIp(request);
        }
        throw new RuntimeException("param not support " + parameter.getParameterType());
    }
}
