package com.livk.spring;

import com.livk.commons.spring.LivkSpring;
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
        LivkSpring.run(Hateoas.class, args);
    }

}
