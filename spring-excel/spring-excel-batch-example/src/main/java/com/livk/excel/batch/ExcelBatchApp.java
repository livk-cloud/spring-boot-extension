package com.livk.excel.batch;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * ExcelApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class ExcelBatchApp {

    public static void main(String[] args) {
        LivkSpring.run(ExcelBatchApp.class, args);
    }

}
