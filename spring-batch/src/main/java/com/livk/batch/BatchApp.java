package com.livk.batch;

import com.livk.commons.spring.LivkSpring;
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
        LivkSpring.run(BatchApp.class, args);
    }

}
