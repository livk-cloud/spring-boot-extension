package com.livk.video.example;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * VideoExample
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class VideoExample {
    public static void main(String[] args) {
        LivkSpring.run(VideoExample.class, args);
    }
}
