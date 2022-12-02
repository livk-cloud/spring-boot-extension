package com.livk.ck;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * ClickHouseApp
 * </p>
 *
 * @author livk
 * @date 2022/4/24
 */
@SpringBootApplication
public class ClickHouseApp {

    public static void main(String[] args) {
        LivkSpring.run(ClickHouseApp.class, args);
    }

}
