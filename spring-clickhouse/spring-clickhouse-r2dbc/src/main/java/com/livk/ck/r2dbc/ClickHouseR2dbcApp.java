package com.livk.ck.r2dbc;

import com.livk.commons.spring.SpringLauncher;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcDataAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * ClickHouseR2dbcApp
 * </p>
 *
 * @author livk
 * @date 2023/2/3
 */
@SpringBootApplication(exclude = {
        R2dbcAutoConfiguration.class,
        R2dbcDataAutoConfiguration.class
})
public class ClickHouseR2dbcApp {
    public static void main(String[] args) {
        SpringLauncher.run(ClickHouseR2dbcApp.class, args);
    }

    @Bean
    public ConnectionFactory connectionFactory(@Value("${spring.r2dbc.url}") String url) {
        return ConnectionFactories.get(url);
    }
}
