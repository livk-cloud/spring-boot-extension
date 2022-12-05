package com.livk.redisson;

import com.livk.commons.spring.LivkSpring;
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
        LivkSpring.run(RedissonApp.class, args);
    }

}
