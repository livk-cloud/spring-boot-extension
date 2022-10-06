package com.livk.browscap;

import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * UserAgentWebApp
 * </p>
 *
 * @author livk
 * @date 2022/10/6
 */
@SpringBootApplication
public class UserAgentWebApp {
    public static void main(String[] args) {
        LivkSpring.run(UserAgentWebApp.class, args);
    }
}
