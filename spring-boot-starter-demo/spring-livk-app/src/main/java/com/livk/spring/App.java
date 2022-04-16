
package com.livk.spring;

import com.livk.common.LivkSpring;
import com.livk.starter01.AnnoTest;
import com.livk.starter01.EnableLivk;
import com.livk.starter01.LivkDemo;
import com.livk.starter01.LivkTestDemo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * <p>
 * App
 * </p>
 *
 * @author livk
 */
@EnableLivk
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        System.setProperty("server.port", "9099");
         LivkSpring.run(App.class, args);
    }

}

@Slf4j
@Component
@RequiredArgsConstructor
class LivkTest {

    @Value("${username.spring.github}")
    public String username;

    private final LivkDemo livkDemo;

    private final LivkTestDemo livkTestDemo;

    private final AnnoTest annoTest;

    @PostConstruct
    public void show() {
        livkDemo.show();
        livkTestDemo.show();
        annoTest.show();
        log.info(username);
    }

}
