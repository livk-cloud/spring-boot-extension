package com.livk.boot.producer;

import com.livk.boot.producer.controller.RocketProducer;
import com.livk.boot.producer.dto.RocketDTO;
import com.livk.boot.rocket.constant.RocketConstant;
import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Kou Shenahi
 */
@SpringBootApplication
public class RocketProducerApp {

    public static void main(String[] args) {
        SpringLauncher.run(RocketProducerApp.class, args);
    }

    @Bean
    public CommandLineRunner rocketCommandLineRunner(RocketProducer rocketProducer) {
        return args -> {
            String msg = "Java第一，老寇无敌。千秋万代，一统江湖。";
            RocketDTO dto = new RocketDTO();
            dto.setData(msg);
            rocketProducer.sendAsyncMessage(RocketConstant.LIVK_MESSAGE_TOPIC, dto);
            rocketProducer.sendOneMessage(RocketConstant.LIVK_MESSAGE_TOPIC, dto);
            rocketProducer.sendMessage(RocketConstant.LIVK_MESSAGE_TOPIC, dto);
        };
    }
}
