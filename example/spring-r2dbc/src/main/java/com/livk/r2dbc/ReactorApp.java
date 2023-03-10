package com.livk.r2dbc;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * <p>
 * ReactorApp
 * </p>
 *
 * @author livk
 */
@EnableWebFlux
@SpringBootApplication
public class ReactorApp {

    public static void main(String[] args) {
        SpringLauncher.run(ReactorApp.class, args);
    }

}
