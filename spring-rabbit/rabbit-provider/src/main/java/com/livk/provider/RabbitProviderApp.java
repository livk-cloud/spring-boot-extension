package com.livk.provider;

import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * RabbitProviderApp
 * </p>
 *
 * @author livk
 * @date 2022/4/14
 */
@SpringBootApplication
public class RabbitProviderApp {

    public static void main(String[] args) {
        LivkSpring.run(RabbitProviderApp.class, args);
    }

}
