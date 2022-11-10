package com.livk.admin.client;

import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * SpringClientApp
 * </p>
 *
 * @author livk
 * @date 2022/11/10
 */
@SpringBootApplication
public class SpringClientApp {
    public static void main(String[] args) {
        LivkSpring.run(SpringClientApp.class, args);
    }
}
