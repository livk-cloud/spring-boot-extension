package com.livk.pulsar.producer;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>
 * PulsarProducerApp
 * </p>
 *
 * @author livk
 */
@EnableScheduling
@SpringBootApplication
public class PulsarProducerApp {

    public static void main(String[] args) {
        SpringLauncher.run(PulsarProducerApp.class, args);
    }

}
