package com.livk.excel.reactive;

import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * ReactiveExcelApp
 * </p>
 *
 * @author livk
 * @date 2022/1/19
 */
@SpringBootApplication
public class ReactiveExcelApp {

    public static void main(String[] args) {
        LivkSpring.run(ReactiveExcelApp.class, args);
    }
}
