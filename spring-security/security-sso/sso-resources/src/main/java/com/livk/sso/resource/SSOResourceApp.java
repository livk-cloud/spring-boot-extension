package com.livk.sso.resource;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * SSOResourceApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class SSOResourceApp {

    public static void main(String[] args) {
        SpringLauncher.run(SSOResourceApp.class, args);
    }

}
