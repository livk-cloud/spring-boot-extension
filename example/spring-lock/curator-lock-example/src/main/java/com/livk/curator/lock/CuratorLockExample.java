package com.livk.curator.lock;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * LocalLockExample
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class CuratorLockExample {
    public static void main(String[] args) {
        SpringLauncher.run(CuratorLockExample.class, args);
    }
}
