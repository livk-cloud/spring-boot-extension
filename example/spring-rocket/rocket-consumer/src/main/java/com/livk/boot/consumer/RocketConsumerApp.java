package com.livk.boot.consumer;

import com.livk.commons.spring.SpringLauncher;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author laokou
 */
@SpringBootApplication
@Import({RocketMQAutoConfiguration.class})
public class RocketConsumerApp {
    public static void main(String[] args) {
        SpringLauncher.run(RocketConsumerApp.class, args);
    }
}
