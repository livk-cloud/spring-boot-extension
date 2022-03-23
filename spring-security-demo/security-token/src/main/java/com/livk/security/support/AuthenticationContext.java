package com.livk.security.support;

import org.springframework.security.core.Authentication;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * AuthenticationContext
 * </p>
 *
 * @author livk
 * @date 2022/3/23
 */
public class AuthenticationContext {

    private static final Map<String, Authentication> AUTHENTICATION_MAP = new ConcurrentHashMap<>();

    public static Authentication getAuthentication(String token) {
        return AUTHENTICATION_MAP.get(token);
    }

    public static void setTokenAndAuthentication(String token, Authentication authentication) {
        AUTHENTICATION_MAP.put(token, authentication);
    }

    public static void delete(String token) {
        AUTHENTICATION_MAP.remove(token);
    }
}
