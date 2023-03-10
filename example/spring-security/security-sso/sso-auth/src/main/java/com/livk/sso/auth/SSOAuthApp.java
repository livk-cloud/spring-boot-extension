package com.livk.sso.auth;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * SSOAuthApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class SSOAuthApp {

    public static void main(String[] args) {
        SpringLauncher.run(SSOAuthApp.class, args);
    }
}
