package com.livk.spring;

import com.livk.starter01.AnnoTest;
import com.livk.starter01.LivkDemo;
import com.livk.starter01.LivkTestDemo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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
