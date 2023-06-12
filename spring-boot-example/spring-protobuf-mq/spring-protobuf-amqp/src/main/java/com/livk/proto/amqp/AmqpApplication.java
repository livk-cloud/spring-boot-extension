package com.livk.proto.amqp;

import com.livk.commons.spring.SpringLauncher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author livk
 */
@Slf4j
@SpringBootApplication
public class AmqpApplication {

    public static void main(String[] args) {
        SpringLauncher.run(args);
    }

}
