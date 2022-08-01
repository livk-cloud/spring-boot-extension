package com.livk.auth.server.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * <p>
 * BadCaptchaException
 * </p>
 *
 * @author livk
 * @date 2022/8/1
 */
public class BadCaptchaException extends AuthenticationException {

    /**
     * Constructs a <code>BadCaptchaException</code> with the specified message.
     *
     * @param msg the detail message
     */
    public BadCaptchaException(String msg) {
        super(msg);
    }

    /**
     * Constructs a <code>BadCaptchaException</code> with the specified message and
     * root cause.
     *
     * @param msg   the detail message
     * @param cause root cause
     */
    public BadCaptchaException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
