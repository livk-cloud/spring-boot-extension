package com.livk.auth.server;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * AuthServerApp
 * </p>
 *
 * @author livk
 * @date 2022/4/24
 */
@SpringBootApplication
public class AuthServerApp {

	public static void main(String[] args) {
		LivkSpring.run(AuthServerApp.class, args);
	}

}
