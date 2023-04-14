package com.livk.redisson.limit;

import com.livk.autoconfigure.limit.annotation.EnableLimit;
import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author livk
 */
@EnableLimit
@SpringBootApplication
public class RedissonLimitApp {
    public static void main(String[] args) {
        SpringLauncher.run(RedissonLimitApp.class, args);
    }
}
