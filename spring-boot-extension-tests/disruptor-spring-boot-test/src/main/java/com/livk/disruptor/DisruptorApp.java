package com.livk.disruptor;

import com.livk.autoconfigure.disruptor.annotation.EnableDisruptor;
import com.livk.commons.SpringLauncher;
import com.livk.disruptor.event.MessageModel;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author livk
 */
@EnableDisruptor(basePackageClasses = MessageModel.class)
@SpringBootApplication
public class DisruptorApp {
	public static void main(String[] args) {
		SpringLauncher.run(args);
	}
}
