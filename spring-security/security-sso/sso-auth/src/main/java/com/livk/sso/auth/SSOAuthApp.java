package com.livk.sso.auth;

import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * SSOAuthApp
 * </p>
 *
 * @author livk
 * @date 2022/4/11
 */
@SpringBootApplication
public class SSOAuthApp {

    public static void main(String[] args) {
        LivkSpring.run(SSOAuthApp.class, args);
    }
}
