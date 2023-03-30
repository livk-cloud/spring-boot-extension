package com.livk.crypto.webflux;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author livk
 */
@SpringBootApplication
public class CryptoWebFluxApp {
    public static void main(String[] args) {
        SpringLauncher.run(CryptoWebFluxApp.class, args);
    }
}
