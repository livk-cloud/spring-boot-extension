package com.livk.rsocket;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * RSocketServerApp
 * </p>
 *
 * @author livk
 * @date 2022/5/23
 */
@SpringBootApplication
public class RSocketServerApp {

	public static void main(String[] args) {
		LivkSpring.run(RSocketServerApp.class, args);
	}

}
