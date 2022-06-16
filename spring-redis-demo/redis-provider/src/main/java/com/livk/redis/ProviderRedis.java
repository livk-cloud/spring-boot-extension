package com.livk.redis;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>
 * ProviderRedis
 * </p>
 *
 * @author livk
 * @date 2021/11/26
 */
@EnableScheduling
@SpringBootApplication
public class ProviderRedis {

    public static void main(String[] args) {
        LivkSpring.run(ProviderRedis.class, args);
    }

}
