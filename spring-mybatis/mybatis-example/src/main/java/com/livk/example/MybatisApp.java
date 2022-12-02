package com.livk.example;

import com.livk.autoconfigure.mybatis.annotation.EnableSqlPlugin;
import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * MybatisApp
 * </p>
 *
 * @author livk
 * @date 2022/3/3
 */
@EnableSqlPlugin
@SpringBootApplication
public class MybatisApp {

    public static void main(String[] args) {
        LivkSpring.run(MybatisApp.class, args);
    }

}
