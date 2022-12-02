package com.livk.spring;

import com.livk.commons.annotation.EnableHttpClient;
import com.livk.commons.annotation.EnableWebClient;
import com.livk.commons.spring.LivkSpring;
import com.livk.selector.LivkImport;
import com.livk.starter01.EnableLivk;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * App
 * </p>
 *
 * @author livk
 */
@LivkImport
@EnableLivk
@EnableWebClient
@EnableHttpClient
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        System.setProperty("server.port", "9099");
        LivkSpring.run(App.class, args);
    }

}
