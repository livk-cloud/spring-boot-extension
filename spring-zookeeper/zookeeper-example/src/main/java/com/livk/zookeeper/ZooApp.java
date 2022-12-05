package com.livk.zookeeper;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * ZooApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class ZooApp {

    public static void main(String[] args) {
        LivkSpring.run(ZooApp.class, args);
    }

}
