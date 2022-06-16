package com.livk.consumer;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * RabbitConsumerApp
 * </p>
 *
 * @author livk
 * @date 2022/4/14
 */
@SpringBootApplication
public class RabbitConsumerApp {

    public static void main(String[] args) {
        LivkSpring.run(RabbitConsumerApp.class, args);
    }

}
