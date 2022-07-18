package com.livk.auth.server.common.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * <p>
 * ScopeException
 * </p>
 *
 * @author livk
 * @date 2022/7/18
 */
public class ScopeException extends OAuth2AuthenticationException {
    public ScopeException(String msg) {
        super(new OAuth2Error(msg), msg);
    }

    public ScopeException(String msg, Throwable cause) {
        super(new OAuth2Error(msg), cause);
    }
}
