package com.livk.order;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * OrderApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class OrderApp {
    public static void main(String[] args) {
        LivkSpring.run(OrderApp.class, args);
    }
}
