package com.livk.event;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>
 * SentServer
 * </p>
 *
 * @author livk
 * @date 2022/9/22
 */
@EnableScheduling
@SpringBootApplication
public class SentServer {
    public static void main(String[] args) {
        LivkSpring.run(SentServer.class, args);
    }
}
