package com.livk.redis;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * ConsumerRedis
 * </p>
 *
 * @author livk
 * @date 2021/11/26
 */
@SpringBootApplication
public class ConsumerRedis {

	public static void main(String[] args) {
		LivkSpring.run(ConsumerRedis.class, args);
	}

}
