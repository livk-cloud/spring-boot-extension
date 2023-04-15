package com.livk.mybatis.example;

import com.livk.autoconfigure.mybatis.inject.annotation.EnableSqlPlugin;
import com.livk.autoconfigure.mybatis.monitor.annotation.EnableSqlMonitor;
import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * MybatisApp
 * </p>
 *
 * @author livk
 */
@EnableSqlMonitor
@EnableSqlPlugin
@SpringBootApplication
public class MybatisApp {

    public static void main(String[] args) {
        SpringLauncher.run(MybatisApp.class, args);
    }

}
