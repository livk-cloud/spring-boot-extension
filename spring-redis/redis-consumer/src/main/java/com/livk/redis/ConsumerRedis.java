package com.livk.redis;

import com.livk.commons.spring.LivkSpring;
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
        LivkSpring.run(ConsumerRedis.class, args);
    }

}
