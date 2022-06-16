package com.livk.shardingsphere.jdbc;

import com.livk.common.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * 分页存在问题
 * </p>
 *
 * @author livk
 * @date 2022/4/19
 */
@SpringBootApplication
public class ShardingsphereApp {

    public static void main(String[] args) {
        LivkSpring.run(ShardingsphereApp.class, args);
    }

}
