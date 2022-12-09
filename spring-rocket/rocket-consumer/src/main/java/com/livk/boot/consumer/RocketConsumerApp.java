package com.livk.boot.consumer;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Kou Shenhai
 */
@SpringBootApplication
public class RocketConsumerApp {
    public static void main(String[] args) {
        LivkSpring.run(RocketConsumerApp.class, args);
    }
}
