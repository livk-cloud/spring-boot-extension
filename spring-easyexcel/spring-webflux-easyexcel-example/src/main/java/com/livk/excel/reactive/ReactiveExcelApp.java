package com.livk.excel.reactive;

import com.livk.commons.spring.LivkSpring;
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
        LivkSpring.run(ReactiveExcelApp.class, args);
    }
}
