package com.livk.commons.http;

import com.livk.commons.http.annotation.EnableHttpClient;
import com.livk.commons.http.annotation.EnableWebClient;
import com.livk.commons.spring.context.SpringContextHolder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author livk
 */
@SpringBootTest("spring.main.web-application-type=servlet")
@EnableHttpClient
@EnableWebClient
public class SpringHttpTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WebClient webClient;

    @Test
    public void test() {
        assertNotNull(restTemplate);
        assertNotNull(webClient);
        assertEquals(SpringContextHolder.getBean(RestTemplate.class), restTemplate);
        assertEquals(SpringContextHolder.getBean(WebClient.class), webClient);
    }
}
