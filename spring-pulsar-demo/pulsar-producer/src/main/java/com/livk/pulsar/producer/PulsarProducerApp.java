package com.livk.pulsar.producer;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>
 * PulsarProducerApp
 * </p>
 *
 * @author livk
 * @date 2022/4/27
 */
@EnableScheduling
@SpringBootApplication
public class PulsarProducerApp {
    public static void main(String[] args) {
        LivkSpring.run(PulsarProducerApp.class, args);
    }
}
