package com.livk.springboot;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * SpringBootTestMain
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class SpringBootTestMain {
    public static void main(String[] args) {
        LivkSpring.run(SpringBootTestMain.class, args);
    }
}
