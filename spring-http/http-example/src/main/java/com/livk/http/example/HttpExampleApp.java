package com.livk.http.example;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * HttpExampleApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class HttpExampleApp {

    public static void main(String[] args) {
        LivkSpring.run(HttpExampleApp.class, args);
    }

}
