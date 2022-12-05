package com.livk.consumer;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * RabbitConsumerApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class RabbitConsumerApp {

    public static void main(String[] args) {
        LivkSpring.run(RabbitConsumerApp.class, args);
    }

}
