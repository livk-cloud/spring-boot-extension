package com.livk.sso.resource;

import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * SSOResourceApp
 * </p>
 *
 * @author livk
 * @date 2022/4/11
 */
@SpringBootApplication
public class SSOResourceApp {

    public static void main(String[] args) {
        LivkSpring.run(SSOResourceApp.class, args);
    }

}
