package com.livk.ip.support;

import com.livk.ip.config.IP;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>
 * IPMethodArgumentResolver
 * </p>
 *
 * @author livk
 * @date 2022/8/22
 */
public class IPMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameter().isAnnotationPresent(IP.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        var request = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.notNull(request, "request bot be null");
        String param = request.getParameter(parameter.getParameterName());
        return DNSForIp(param);
    }

    private String DNSForIp(String ip) {
        if (ip.contains(IP.Constant.HTTPS)) {
            ip = ip.replaceFirst(IP.Constant.HTTPS, "");
        } else if (ip.contains(IP.Constant.HTTP)) {
            ip = ip.replaceFirst(IP.Constant.HTTP, "");
        } else {
            //ip地址不解析
            return ip;
        }
        try {
            return InetAddress.getByName(ip).getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
