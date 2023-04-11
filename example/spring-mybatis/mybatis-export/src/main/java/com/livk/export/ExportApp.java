package com.livk.export;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author livk
 */
@SpringBootApplication
public class ExportApp {
    public static void main(String[] args) {
        SpringLauncher.run(ExportApp.class, args);
    }
}
