package com.livk.commons;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * SpringBootTestMain
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class SpringBootTestMain {
    public static void main(String[] args) {
        SpringLauncher.run(SpringBootTestMain.class, args);
    }
}
