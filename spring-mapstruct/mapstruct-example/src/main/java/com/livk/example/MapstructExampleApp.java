package com.livk.example;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * MapstructExampleApp
 * </p>
 *
 * @author livk
 * @date 2022/5/11
 */
@SpringBootApplication
public class MapstructExampleApp {

    public static void main(String[] args) {
        LivkSpring.run(MapstructExampleApp.class, args);
    }

}
