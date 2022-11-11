package com.livk.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.function.Function;
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
        return StreamUtils.of(request.getHeaderNames().asIterator())
                .collect(Collectors.toMap(Function.identity(), request::getHeader));
    }

    public Map<String, String> getParamMap(CharSequence delimiter) {
        return getRequest()
                .getParameterMap()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.join(delimiter, entry.getValue())));
    }

    public String getRealIp() {
        return getRealIp(getRequest());
    }

    public String getRealIp(HttpServletRequest request) {
        // 这个一般是Nginx反向代理设置的参数
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.hasText(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (!StringUtils.hasText(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!StringUtils.hasText(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多IP的情况（只取第一个IP）
        return ip != null && ip.contains(HTTP_IP_SPLIT) ? ip.split(HTTP_IP_SPLIT)[0] : ip;
    }

    public void out(Object data) {
        out(getResponse(), JacksonUtils.toJsonStr(data));
    }

    public void out(HttpServletResponse response, Object data) {
        out(response, JacksonUtils.toJsonStr(data), MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * 根据response写入返回值
     *
     * @param response    response
     * @param message     写入的信息
     * @param contentType contentType {@link MediaType}
     */
    public void out(HttpServletResponse response, String message, String contentType) {
        response.setContentType(contentType);
        try (PrintWriter out = response.getWriter()) {
            out.print(message);
            out.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
