package com.livk.starter01;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * LivkDemo
 * </p>
 *
 * @author livk
 */
@Slf4j
public class LivkDemo {

	@Bean
	public LivkTestDemo livkTestDemo() {
		return new LivkTestDemo();
	}

	public void show() {
		log.info("this is my Spring Test Demo");
	}

}
