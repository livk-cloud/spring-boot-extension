package com.livk.batch;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * BatchApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class BatchApp {

    public static void main(String[] args) {
        SpringLauncher.run(BatchApp.class, args);
    }

}
