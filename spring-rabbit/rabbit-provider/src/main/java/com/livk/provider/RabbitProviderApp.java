package com.livk.provider;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * RabbitProviderApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class RabbitProviderApp {

    public static void main(String[] args) {
        SpringLauncher.run(RabbitProviderApp.class, args);
    }

}
