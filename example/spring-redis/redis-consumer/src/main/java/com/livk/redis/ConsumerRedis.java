package com.livk.redis;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * ConsumerRedis
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class ConsumerRedis {

    public static void main(String[] args) {
        SpringLauncher.run(ConsumerRedis.class, args);
    }

}
