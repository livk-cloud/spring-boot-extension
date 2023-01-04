package com.livk.example;

import com.livk.autoconfigure.mybatis.annotation.EnableSqlPlugin;
import com.livk.autoconfigure.mybatis.monitor.EnableSqlMonitor;
import com.livk.commons.spring.LivkSpring;
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
        LivkSpring.run(MybatisApp.class, args);
    }

}
