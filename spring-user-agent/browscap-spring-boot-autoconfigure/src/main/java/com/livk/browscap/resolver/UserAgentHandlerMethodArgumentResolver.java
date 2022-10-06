package com.livk.browscap.resolver;

import com.blueconic.browscap.Capabilities;
import com.livk.browscap.annotation.UserAgent;
import com.livk.browscap.support.UserAgentContext;
import com.livk.browscap.util.UserAgentUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
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
        return parameter.hasParameterAnnotation(UserAgent.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Capabilities capabilities = UserAgentContext.get();
        if (capabilities != null) {
            return capabilities;
        }
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.notNull(request, "request not be null!");
        return UserAgentUtils.parse(request);
    }
}
