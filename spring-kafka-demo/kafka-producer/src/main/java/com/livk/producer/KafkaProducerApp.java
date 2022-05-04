package com.livk.producer;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>
 * KafkaProducerApp
 * </p>
 *
 * @author livk
 * @date 2022/5/4
 */
@EnableScheduling
@SpringBootApplication
public class KafkaProducerApp {

	public static void main(String[] args) {
		LivkSpring.run(KafkaProducerApp.class, args);
	}

}
