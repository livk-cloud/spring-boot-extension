package com.livk.autoconfigure.ip2region.support;

import com.livk.commons.web.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

/**
 * <p>
 * RequestIpContextHolder
 * </p>
 *
 * @author livk
 * @date 2023/1/2
 */
public class RequestIpContextHolder {
    private static final ThreadLocal<String> context = new ThreadLocal<>();

    private static final ThreadLocal<String> inheritableContext = new InheritableThreadLocal<>();

    public static void set(String ip) {
        context.set(ip);
        inheritableContext.set(ip);
    }

    public static void set(HttpServletRequest request) {
        String ip = WebUtils.realIp(request);
        set(ip);
    }

    public synchronized static String computeIfAbsent(Supplier<String> supplier) {
        String ip = get();
        if (StringUtils.hasText(ip)) {
            return ip;
        }
        String supplierIp = supplier.get();
        set(supplierIp);
        return supplierIp;
    }

    public static String get() {
        String ip = context.get();
        if (!StringUtils.hasText(ip)) {
            ip = inheritableContext.get();
        }
        return ip;
    }

    public static void remove() {
        context.remove();
        inheritableContext.remove();
    }
}
