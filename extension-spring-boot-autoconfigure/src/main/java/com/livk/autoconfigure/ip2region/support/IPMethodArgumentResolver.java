package com.livk.autoconfigure.ip2region.support;

import com.livk.autoconfigure.ip2region.annotation.IP;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.net.InetAddress;
import java.net.URL;

/**
 * <p>
 * IPMethodArgumentResolver
 * </p>
 *
 * @author livk
 *
 */
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

    private String DNSForIp(String ip) throws Exception {
        if (ip.startsWith(IP.Constant.HTTPS) || ip.startsWith(IP.Constant.HTTP)) {
            ip = new URL(ip).getHost();
        }
        return InetAddress.getByName(ip).getCanonicalHostName();
    }
}
