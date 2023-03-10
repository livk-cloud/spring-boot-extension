package com.livk.excel.mvc;

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
public class ExcelApp {

    public static void main(String[] args) {
        SpringLauncher.run(ExcelApp.class, args);
    }

}
