package com.livk.ip2region.mvc.example;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * Ip2RegionApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class MvcIp2RegionExample {
    public static void main(String[] args) {
        LivkSpring.run(MvcIp2RegionExample.class, args);
    }
}
