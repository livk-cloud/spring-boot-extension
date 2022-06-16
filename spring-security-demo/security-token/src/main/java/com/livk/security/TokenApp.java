package com.livk.security;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * ToeknApp
 * </p>
 *
 * @author livk
 * @date 2022/3/23
 */
@SpringBootApplication
public class TokenApp {

	public static void main(String[] args) {
		LivkSpring.run(TokenApp.class, args);
	}

}
