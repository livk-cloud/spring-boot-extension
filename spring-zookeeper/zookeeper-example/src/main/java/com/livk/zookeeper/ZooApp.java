package com.livk.zookeeper;

import com.livk.commons.spring.SpringLauncher;
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
        SpringLauncher.run(ZooApp.class, args);
    }

}
