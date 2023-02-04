package com.livk.consumer;

import com.livk.commons.spring.SpringLauncher;
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
        SpringLauncher.run(RabbitConsumerApp.class, args);
    }

}
