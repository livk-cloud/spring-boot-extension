package com.livk.excel.batch;

import com.livk.commons.spring.SpringLauncher;
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
        SpringLauncher.run(ExcelBatchApp.class, args);
    }

}
