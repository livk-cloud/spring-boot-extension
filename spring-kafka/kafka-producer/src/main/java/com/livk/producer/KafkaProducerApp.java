package com.livk.producer;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>
 * KafkaProducerApp
 * </p>
 *
 * @author livk
 */
@EnableScheduling
@SpringBootApplication
public class KafkaProducerApp {

    public static void main(String[] args) {
        SpringLauncher.run(KafkaProducerApp.class, args);
    }

}
