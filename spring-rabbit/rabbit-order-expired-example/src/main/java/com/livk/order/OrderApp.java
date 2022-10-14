package com.livk.order;

import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * OrderApp
 * </p>
 *
 * @author livk
 * @date 2022/10/13
 */
@SpringBootApplication
public class OrderApp {
    public static void main(String[] args) {
        LivkSpring.run(OrderApp.class, args);
    }
}
