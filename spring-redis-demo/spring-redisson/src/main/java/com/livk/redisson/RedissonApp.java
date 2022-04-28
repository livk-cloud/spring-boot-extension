package com.livk.redisson;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * RedissonApp
 * </p>
 *
 * @author livk
 * @date 2022/3/30
 */
@SpringBootApplication
public class RedissonApp {

	public static void main(String[] args) {
		LivkSpring.run(RedissonApp.class, args);
	}

}
