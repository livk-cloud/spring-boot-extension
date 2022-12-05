package com.livk.redis;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * LuaApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class LuaApp {

    public static void main(String[] args) {
        LivkSpring.run(LuaApp.class, args);
    }

}
