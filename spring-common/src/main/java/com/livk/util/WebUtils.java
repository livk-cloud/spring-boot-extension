package com.livk.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * WebUtils
 * </p>
 *
 * @author livk
 * @date 2022/9/19
 */
@UtilityClass
public class WebUtils extends org.springframework.web.util.WebUtils {

    private static final String UNKNOWN = "unknown";

    private static final String HTTP_IP_SPLIT = ",";

    public ServletRequestAttributes getAttributes() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        Assert.notNull(servletRequestAttributes, "attributes not null!");
        return servletRequestAttributes;
    }

    public HttpServletRequest getRequest() {
        return getAttributes().getRequest();
    }

    public HttpServletResponse getResponse() {
        return getAttributes().getResponse();
    }

    public HttpSession getSession() {
        return getRequest().getSession();
    }

    public String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    public String getHeader(String headerName) {
        return getRequest().getHeader(headerName);
    }

    public Map<String, String> getHeaders() {
        HttpServletRequest request = getRequest();
        Map<String, String> map = new LinkedHashMap<>();
        Iterator<String> iterator = request.getHeaderNames().asIterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    public Map<String, String> getParamMap(CharSequence delimiter) {
        Set<Map.Entry<String, String[]>> entrySet = getRequest()
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
        String ip = request.getHeader("X-Real-IP");
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

    public void out(Object data) {
        HttpServletResponse response = getResponse();
        Assert.notNull(response, "response not null!");
        out(response, JacksonUtils.toJsonStr(data));
    }

    public void out(HttpServletResponse response, Object data) {
        out(response, JacksonUtils.toJsonStr(data));
    }

    /**
     * 根据response写入返回值
     *
     * @param response response
     * @param message  写入的信息
     */
    public void out(HttpServletResponse response, String message) {
        response.setContentType("application/json;charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(message);
            out.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
