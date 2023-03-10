package com.livk.redisson;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * RedissonApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class RedissonApp {

    public static void main(String[] args) {
        SpringLauncher.run(RedissonApp.class, args);
    }

}
