package com.livk.consumer;

import com.livk.common.constant.KafkaConstant;
import com.livk.commons.spring.SpringLauncher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * <p>
 * KafkaConsumerApp
 * </p>
 *
 * @author livk
 */
@Slf4j
@SpringBootApplication
public class KafkaConsumerApp {

    public static void main(String[] args) {
        SpringLauncher.run(KafkaConsumerApp.class, args);
    }

    @KafkaListener(id = "livk-id", topics = KafkaConstant.TOPIC)
    public void consumer(String input) {
        log.info("data:{}", input);
    }

}
