package com.livk.boot.producer;
import com.livk.commons.spring.SpringLauncher;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author laokou
 */
@SpringBootApplication
@Import({RocketMQAutoConfiguration.class})
public class RocketProducerApp {

    public static void main(String[] args) {
        SpringLauncher.run(RocketProducerApp.class, args);
    }

}
