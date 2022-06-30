package com.livk.auth.resource;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * AuthResourceApp
 * </p>
 *
 * @author livk
 * @date 2022/4/24
 */
@SpringBootApplication
public class AuthResourceApp {

    public static void main(String[] args) {
        LivkSpring.run(AuthResourceApp.class, args);
    }

}
