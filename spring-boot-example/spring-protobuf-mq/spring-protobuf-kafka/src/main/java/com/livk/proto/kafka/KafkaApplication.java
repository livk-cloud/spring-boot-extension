package com.livk.proto.kafka;

import com.livk.commons.spring.SpringLauncher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author livk
 */
@Slf4j
@SpringBootApplication
public class KafkaApplication {

    public static void main(String[] args) {
        SpringLauncher.run(args);
    }
}
