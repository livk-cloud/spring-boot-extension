package com.livk.admin.server;

import com.livk.spring.LivkSpring;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * SpringServerApp
 * </p>
 *
 * @author livk
 * @date 2022/11/10
 */
@EnableAdminServer
@SpringBootApplication
public class SpringServerApp {
    public static void main(String[] args) {
        LivkSpring.run(SpringServerApp.class, args);
    }
}
