package com.livk.redis;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>
 * ProviderRedis
 * </p>
 *
 * @author livk
 */
@EnableRedisRepositories
@EnableScheduling
@SpringBootApplication
public class ProviderRedis {

    public static void main(String[] args) {
        SpringLauncher.run(ProviderRedis.class, args);
    }

}
