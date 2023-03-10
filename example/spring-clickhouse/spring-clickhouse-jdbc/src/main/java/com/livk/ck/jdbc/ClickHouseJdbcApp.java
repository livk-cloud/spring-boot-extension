package com.livk.ck.jdbc;

import com.livk.commons.spring.SpringLauncher;
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
        SpringLauncher.run(ClickHouseJdbcApp.class, args);
    }

}
