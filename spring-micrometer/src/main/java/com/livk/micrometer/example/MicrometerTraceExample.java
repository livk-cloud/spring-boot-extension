package com.livk.micrometer.example;

import com.livk.commons.spring.SpringLauncher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * MicrometerTraceExample
 * </p>
 *
 * @author livk
 * @date 2022/12/16
 */
@Slf4j
@RestController
@SpringBootApplication
public class MicrometerTraceExample {

    public static void main(String[] args) {
        SpringLauncher.run(MicrometerTraceExample.class, args);
    }

    @GetMapping("home")
    public String home() {
        log.info("home() has been called");
        return "Hello World!";
    }
}
