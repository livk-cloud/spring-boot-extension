package com.livk.boot.producer;

import com.livk.boot.producer.constant.RocketConstant;
import com.livk.boot.producer.controller.RocketProducer;
import com.livk.boot.producer.dto.RocketDTO;
import com.livk.commons.spring.LivkSpring;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Kou Shenahi
 */
@SpringBootApplication
@RequiredArgsConstructor
public class RocketProducerApp implements CommandLineRunner {

    private final RocketProducer rocketProducer;

    public static void main(String[] args) {
        LivkSpring.run(RocketProducerApp.class, args);
    }

    @Override
    public void run(String... args) {
        String msg = "Java第一，老寇无敌。千秋万代，一统江湖。";
        RocketDTO dto = new RocketDTO();
        dto.setData(msg);
        rocketProducer.sendAsyncMessage(RocketConstant.LIVK_MESSAGE_TOPIC, dto);
        rocketProducer.sendOneMessage(RocketConstant.LIVK_MESSAGE_TOPIC, dto);
        rocketProducer.sendMessage(RocketConstant.LIVK_MESSAGE_TOPIC, dto);
    }
}
