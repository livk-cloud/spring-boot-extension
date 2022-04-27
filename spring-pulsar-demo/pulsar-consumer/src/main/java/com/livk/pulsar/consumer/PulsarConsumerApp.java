package com.livk.pulsar.consumer;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * PulsarConsumerApp
 * </p>
 *
 * @author livk
 * @date 2022/4/27
 */
@SpringBootApplication
public class PulsarConsumerApp {
    public static void main(String[] args) {
        LivkSpring.run(PulsarConsumerApp.class, args);
    }
}
