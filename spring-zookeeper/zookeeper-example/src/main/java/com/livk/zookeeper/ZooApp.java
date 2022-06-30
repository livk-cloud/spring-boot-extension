package com.livk.zookeeper;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * ZooApp
 * </p>
 *
 * @author livk
 * @date 2022/4/6
 */
@SpringBootApplication
public class ZooApp {

    public static void main(String[] args) {
        LivkSpring.run(ZooApp.class, args);
    }

}
