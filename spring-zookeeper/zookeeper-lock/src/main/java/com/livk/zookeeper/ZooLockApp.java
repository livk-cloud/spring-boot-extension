package com.livk.zookeeper;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * ZooLockApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class ZooLockApp {

    public static void main(String[] args) {
        LivkSpring.run(ZooLockApp.class, args);
    }

}
