package com.livk.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * RequestUtil
 * </p>
 *
 * @author livk
 * @date 2022/6/7
 */
@UtilityClass
public class RequestUtils {

    public HttpServletRequest getRequest() {
        var requestAttributes = RequestContextHolder.getRequestAttributes();
        var servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        Assert.notNull(servletRequestAttributes, "attributes not null!");
        return servletRequestAttributes.getRequest();
    }

    public HttpSession getSession() {
        return RequestUtils.getRequest().getSession();
    }

    public String getParameter(String name) {
        return RequestUtils.getRequest().getParameter(name);
    }

    public String getHeader(String headerName) {
        return RequestUtils.getRequest().getHeader(headerName);
    }

    public Map<String, String> getHeaders() {
        var request = RequestUtils.getRequest();
        var map = new LinkedHashMap<String, String>();
        Iterator<String> iterator = request.getHeaderNames().asIterator();
        while (iterator.hasNext()) {
            var key = iterator.next();
            var value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

}
