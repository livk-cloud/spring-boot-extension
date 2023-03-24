package com.livk.redis.submit;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author livk
 */
@SpringBootApplication
public class RepeatedSubmit {
    public static void main(String[] args) {
        SpringLauncher.run(RepeatedSubmit.class, args);
    }
}
