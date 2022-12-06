package com.livk.redisson.order;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * OrderExpiredExample
 * </p>
 *
 * @author livk
 * @date 2022/12/6
 */
@SpringBootApplication
public class OrderExpiredExample {
    public static void main(String[] args) {
        LivkSpring.run(OrderExpiredExample.class, args);
    }
}
