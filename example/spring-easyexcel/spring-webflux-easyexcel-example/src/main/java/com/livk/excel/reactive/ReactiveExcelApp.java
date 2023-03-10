package com.livk.excel.reactive;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * ReactiveExcelApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class ReactiveExcelApp {

    public static void main(String[] args) {
        SpringLauncher.run(ReactiveExcelApp.class, args);
    }
}
