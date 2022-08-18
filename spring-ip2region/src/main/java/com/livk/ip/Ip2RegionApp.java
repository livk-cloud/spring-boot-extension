package com.livk.ip;

import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * Ip2RegionApp
 * </p>
 *
 * @author livk
 * @date 2022/8/18
 */
@SpringBootApplication
public class Ip2RegionApp {
    public static void main(String[] args) {
        LivkSpring.run(Ip2RegionApp.class, args);
    }
}
