package com.livk.autoconfigure.useragent.browscap.resolver;

import com.blueconic.browscap.Capabilities;
import com.livk.autoconfigure.useragent.annotation.UserAgentInfo;
import com.livk.autoconfigure.useragent.browscap.support.UserAgentContextHolder;
import com.livk.autoconfigure.useragent.browscap.util.UserAgentUtils;
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
 * UserAgent
 * </p>
 *
 * @author livk
 * @date 2022/9/30
 */
public class UserAgentHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserAgentInfo.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Capabilities capabilities = UserAgentContextHolder.get();
        if (capabilities != null) {
            return capabilities;
        }
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.notNull(request, "request not be null!");
        return UserAgentUtils.parse(request);
    }
}
