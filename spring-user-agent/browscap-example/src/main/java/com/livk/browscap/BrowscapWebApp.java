package com.livk.browscap;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * BrowscapWebApp
 * </p>
 *
 * @author livk
 * @date 2022/10/6
 */
@SpringBootApplication
public class BrowscapWebApp {
    public static void main(String[] args) {
        LivkSpring.run(BrowscapWebApp.class, args);
    }
}
