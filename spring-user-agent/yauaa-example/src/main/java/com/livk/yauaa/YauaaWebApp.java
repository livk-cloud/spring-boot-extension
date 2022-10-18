package com.livk.yauaa;

import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * YauaaWebApp
 * </p>
 *
 * @author livk
 * @date 2022/10/6
 */
@SpringBootApplication
public class YauaaWebApp {
    public static void main(String[] args) {
        LivkSpring.run(YauaaWebApp.class, args);
    }
}
