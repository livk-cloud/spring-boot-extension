package com.livk.dynamic.example;

import com.livk.autoconfigure.dynamic.annotation.EnableDynamicDatasource;
import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * DynamicExample
 * </p>
 *
 * @author livk
 */
@EnableDynamicDatasource
@SpringBootApplication
public class DynamicExample {
    public static void main(String[] args) {
        SpringLauncher.run(DynamicExample.class, args);
    }
}
