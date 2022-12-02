package com.livk.ip2region.mvc.example;

import com.livk.commons.spring.LivkSpring;
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
public class MvcIp2RegionExample {
    public static void main(String[] args) {
        LivkSpring.run(MvcIp2RegionExample.class, args);
    }
}
