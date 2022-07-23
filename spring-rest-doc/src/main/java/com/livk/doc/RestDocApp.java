package com.livk.doc;

import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * RestDocApp
 * </p>
 *
 * @author livk
 * @date 2022/7/23
 */
@SpringBootApplication
public class RestDocApp {
    public static void main(String[] args) {
        LivkSpring.run(RestDocApp.class, args);
    }
}
