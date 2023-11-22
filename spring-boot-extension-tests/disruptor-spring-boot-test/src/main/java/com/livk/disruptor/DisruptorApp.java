package com.livk.disruptor;

import com.livk.commons.SpringLauncher;
import com.livk.core.disruptor.EnableDisruptor;
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
