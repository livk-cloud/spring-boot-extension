package com.livk.spring;

import com.livk.starter01.AnnoTest;
import com.livk.starter01.LivkDemo;
import com.livk.starter01.LivkTestDemo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>
 * LivkTest
 * </p>
 *
 * @author livk
 * @date 2022/5/10
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LivkTest {

	private final LivkDemo livkDemo;

	private final LivkTestDemo livkTestDemo;

	private final AnnoTest annoTest;

	@Value("${username.spring.github}")
	public String username;

	@PostConstruct
	public void show() {
		livkDemo.show();
		livkTestDemo.show();
		annoTest.show();
		log.info(username);
	}

}
