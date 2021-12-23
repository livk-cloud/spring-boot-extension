package com.livk.r2dbc;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * <p>
 * ReactorApp
 * </p>
 *
 * @author livk
 * @date 2021/12/6
 */
@EnableWebFlux
@SpringBootApplication
public class ReactorApp {
    public static void main(String[] args) {
        LivkSpring.runReactive(ReactorApp.class, args);
    }
}
