package com.livk.retry.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p>
 * RemoteSupport
 * </p>
 *
 * @author livk
 * @date 2022/4/29
 */
@Slf4j
@Component
public class RemoteSupport {

    @Retryable(value = IllegalArgumentException.class,
            backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public String call(String username) {
        if ("fail".equals(username)) {
            log.info("Remote Fail time:{}", LocalDateTime.now());
            throw new IllegalArgumentException("Fail RPC");
        }
        return "SUC";
    }

    @Recover
    public String recover(IllegalArgumentException e) {
        log.error("remote exception msg:{}", e.getMessage());
        return "recover SUC";
    }

}
