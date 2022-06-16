package com.livk.zookeeper;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * ZooLockApp
 * </p>
 *
 * @author livk
 * @date 2022/4/7
 */
@SpringBootApplication
public class ZooLockApp {

    public static void main(String[] args) {
        LivkSpring.run(ZooLockApp.class, args);
    }

}
