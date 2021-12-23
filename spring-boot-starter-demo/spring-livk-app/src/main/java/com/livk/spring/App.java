
package com.livk.spring;

import com.livk.common.LivkSpring;
import com.livk.starter01.AnnoTest;
import com.livk.starter01.EnableLivk;
import com.livk.starter01.LivkDemo;
import com.livk.starter01.LivkTestDemo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;


/**
 * <p>
 * App
 * </p>
 *
 * @author livk
 */
@EnableLivk
@SpringBootApplication(scanBasePackages = "com.livk")
public class App {

	public static void main(String[] args) {
		System.setProperty("server.port", "9099");
		LivkSpring.runServlet(App.class, args);
	}

}

@Component
@RequiredArgsConstructor
class LivkTest {

	private final LivkDemo livkDemo;

	private final LivkTestDemo livkTestDemo;

	private final AnnoTest annoTest;

	@PostConstruct
	public void show() {
		livkDemo.show();
		livkTestDemo.show();
		annoTest.show();
	}

}
