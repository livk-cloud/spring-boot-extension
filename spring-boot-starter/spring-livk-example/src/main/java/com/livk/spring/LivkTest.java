package com.livk.spring;

import com.livk.starter01.AnnoTest;
import com.livk.starter01.LivkDemo;
import com.livk.starter01.LivkTestDemo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * <p>
 * LivkTest
 * </p>
 *
 * @author livk
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LivkTest {

    private final LivkDemo livkDemo;

    private final LivkTestDemo livkTestDemo;

    private final AnnoTest annoTest;

    private final WebClient webClient;

    private final RestTemplate restTemplate;

    @Value("${username.spring.github}")
    public String username;

    @PostConstruct
    public void show() {
        livkDemo.show();
        livkTestDemo.show();
        annoTest.show();
        log.info(username);
        String htmlWebClient = webClient.get()
                .uri("https://github.com/")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("{}", htmlWebClient.substring(0, 20));
        String htmlRestTemplate = restTemplate.getForObject("https://github.com/", String.class);
        log.info("{}", htmlRestTemplate.substring(0, 20));
    }

}
