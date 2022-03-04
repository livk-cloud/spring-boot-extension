package com.livk.quartz;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * QuartzApp
 * </p>
 *
 * @author livk
 * @date 2021/10/25
 */
@SpringBootApplication
public class QuartzApp {

	public static void main(String[] args) {
		LivkSpring.run(QuartzApp.class, args);
	}

}
