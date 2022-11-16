package com.livk.video.example;

import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * VideoExample
 * </p>
 *
 * @author livk
 * @date 2022/11/16
 */
@SpringBootApplication
public class VideoExample {
    public static void main(String[] args) {
        LivkSpring.run(VideoExample.class, args);
    }
}
