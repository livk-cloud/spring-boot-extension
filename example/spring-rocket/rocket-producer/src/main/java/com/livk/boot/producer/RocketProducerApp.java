package com.livk.boot.producer;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author laokou
 */
@SpringBootApplication
public class RocketProducerApp {

    public static void main(String[] args) {
        SpringLauncher.run(RocketProducerApp.class, args);
    }

}
