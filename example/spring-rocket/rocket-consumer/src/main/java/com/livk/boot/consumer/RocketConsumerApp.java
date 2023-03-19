package com.livk.boot.consumer;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author laokou
 */
@SpringBootApplication
public class RocketConsumerApp {
    public static void main(String[] args) {
        SpringLauncher.run(RocketConsumerApp.class, args);
    }
}
