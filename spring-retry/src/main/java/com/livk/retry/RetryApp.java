package com.livk.retry;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * <p>
 * RetryApp
 * </p>
 *
 * @author livk
 * @date 2022/4/29
 */
@EnableRetry
@SpringBootApplication
public class RetryApp {

    public static void main(String[] args) {
        LivkSpring.run(RetryApp.class, args);
    }

}
