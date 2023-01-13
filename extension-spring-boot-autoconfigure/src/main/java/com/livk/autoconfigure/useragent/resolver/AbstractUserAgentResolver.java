package com.livk.autoconfigure.useragent.resolver;

import com.livk.autoconfigure.useragent.annotation.UserAgentInfo;
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
 * AbstractUserAgentHandlerMethodArgumentResolver
 * </p>
 *
 * @param <T> the type parameter
 * @author livk
 */
public abstract class AbstractUserAgentResolver<T> implements HandlerMethodArgumentResolver {

    @Override
    public final boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserAgentInfo.class);
    }

    @Override
    public final Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        T userAgent = getUserAgent();
        if (userAgent != null) {
            return userAgent;
        }
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.notNull(request, "request not be null!");
        return parseUserAgent(request);
    }

    /**
     * Gets user agent.
     *
     * @return the user agent
     */
    protected abstract T getUserAgent();

    /**
     * Parse user agent t.
     *
     * @param request the request
     * @return the t
     */
    protected abstract T parseUserAgent(HttpServletRequest request);
}
