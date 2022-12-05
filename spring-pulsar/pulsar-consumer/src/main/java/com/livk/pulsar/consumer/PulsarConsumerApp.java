package com.livk.pulsar.consumer;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * PulsarConsumerApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class PulsarConsumerApp {

    public static void main(String[] args) {
        LivkSpring.run(PulsarConsumerApp.class, args);
    }

}
