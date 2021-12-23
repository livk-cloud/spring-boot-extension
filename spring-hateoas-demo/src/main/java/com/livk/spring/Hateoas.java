package com.livk.spring;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * Hateoas
 * </p>
 *
 * @author livk
 * @date 2021/11/24
 */
@SpringBootApplication
public class Hateoas {

	public static void main(String[] args) {
		LivkSpring.runServlet(Hateoas.class, args);
	}

}
