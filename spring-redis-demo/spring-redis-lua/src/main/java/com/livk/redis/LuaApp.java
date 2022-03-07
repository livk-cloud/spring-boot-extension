package com.livk.redis;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * LuaApp
 * </p>
 *
 * @author livk
 * @date 2022/3/7
 */
@SpringBootApplication
public class LuaApp {
    public static void main(String[] args) {
        LivkSpring.run(LuaApp.class, args);
    }
}
