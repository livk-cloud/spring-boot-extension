package com.livk.autoconfigure.useragent.servlet;

import com.livk.autoconfigure.useragent.annotation.UserAgentInfo;
import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;
import com.livk.commons.domain.Wrapper;
import com.livk.commons.web.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * <p>
 * AbstractUserAgentHandlerMethodArgumentResolver
 * </p>
 *
 * @param <T> the type parameter
 * @author livk
 */
@RequiredArgsConstructor
public abstract class AbstractUserAgentResolver<T> implements HandlerMethodArgumentResolver {

    private final HttpUserAgentParser<T> userAgentParse;

    @Override
    public final boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserAgentInfo.class);
    }

    @Override
    public final Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Wrapper<?> useragentWrapper = UserAgentContextHolder.getUserAgentContext();
        if (useragentWrapper == null) {
            HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
            Assert.notNull(request, "request not be null!");
            HttpHeaders headers = WebUtils.headers(request);
            useragentWrapper = userAgentParse.parse(headers);
            UserAgentContextHolder.setUserAgentContext(useragentWrapper);
        }
        return useragentWrapper.obj();
    }
}
