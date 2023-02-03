package com.livk.ck.jdbc;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * ClickHouseJdbcApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class ClickHouseJdbcApp {

    public static void main(String[] args) {
        LivkSpring.run(ClickHouseJdbcApp.class, args);
    }

}
