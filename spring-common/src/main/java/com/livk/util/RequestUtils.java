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
import java.util.Set;
import java.util.stream.Collectors;

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

    private static final String UNKNOWN = "unknown";

    private static final String HTTP_IP_SPLIT = ",";

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

    public Map<String, String> getParamMap(CharSequence delimiter) {
        Set<Map.Entry<String, String[]>> entrySet = RequestUtils.getRequest()
                .getParameterMap()
                .entrySet();
        return entrySet.stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> String.join(delimiter, entry.getValue())));
    }

    public String getRealIp() {
        return getRealIp(getRequest());
    }

    public String getRealIp(HttpServletRequest request) {
        // 这个一般是Nginx反向代理设置的参数
        var ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多IP的情况（只取第一个IP）
        return ip != null && ip.contains(HTTP_IP_SPLIT) ? ip.split(HTTP_IP_SPLIT)[0] : ip;
    }
}
