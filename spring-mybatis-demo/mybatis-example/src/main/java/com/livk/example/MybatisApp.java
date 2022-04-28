package com.livk.example;

import com.livk.annotation.EnableSqlPlugin;
import com.livk.common.LivkSpring;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * MybatisApp
 * </p>
 *
 * @author livk
 * @date 2022/3/3
 */
@MapperScan("com.livk.example.mapper")
@EnableSqlPlugin
@SpringBootApplication
public class MybatisApp {

	public static void main(String[] args) {
		LivkSpring.run(MybatisApp.class, args);
	}

}
