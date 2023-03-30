package com.livk.crypto.mvc;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author livk
 */
@SpringBootApplication
public class CryptoMvcApp {
    public static void main(String[] args) {
        SpringLauncher.run(CryptoMvcApp.class, args);
    }
}
