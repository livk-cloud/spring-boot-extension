package com.livk.quartz;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * QuartzApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class QuartzApp {

    public static void main(String[] args) {
        SpringLauncher.run(QuartzApp.class, args);
    }

}
