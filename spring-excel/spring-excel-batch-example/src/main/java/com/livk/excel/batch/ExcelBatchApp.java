package com.livk.excel.batch;

import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * ExcelApp
 * </p>
 *
 * @author livk
 * @date 2022/1/19
 */
@SpringBootApplication
public class ExcelBatchApp {

    public static void main(String[] args) {
        LivkSpring.run(ExcelBatchApp.class, args);
    }

}
