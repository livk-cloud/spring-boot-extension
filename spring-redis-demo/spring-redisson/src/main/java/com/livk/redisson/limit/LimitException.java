package com.livk.redisson.limit;

import lombok.Getter;

import java.io.Serial;

/**
 * <p>
 * LimitException
 * </p>
 *
 * @author livk
 * @date 2022/3/22
 */
@Getter
public class LimitException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6965865940214673219L;

    private final int code;

    public LimitException(String message) {
        this(500, message);
    }

    public LimitException(int code, String message) {
        super(message);
        this.code = code;
    }

}
