package com.livk.redisson.lock;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * LocalLockExample
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class RedissonLockExample {
    public static void main(String[] args) {
        SpringLauncher.run(RedissonLockExample.class, args);
    }
}
