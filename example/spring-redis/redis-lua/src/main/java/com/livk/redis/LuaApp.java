package com.livk.redis;

import com.livk.commons.spring.SpringLauncher;
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
        SpringLauncher.run(LuaApp.class, args);
    }

}
