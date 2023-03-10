package com.livk.admin.server;

import com.livk.commons.spring.SpringLauncher;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * SpringServerApp
 * </p>
 *
 * @author livk
 */
@EnableAdminServer
@SpringBootApplication
public class SpringServerApp {
    public static void main(String[] args) {
        SpringLauncher.run(SpringServerApp.class, args);
    }
}
