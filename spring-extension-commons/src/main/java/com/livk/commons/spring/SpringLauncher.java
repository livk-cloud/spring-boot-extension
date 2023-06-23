/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.commons.spring;

import com.livk.commons.bean.util.ClassUtils;
import com.livk.commons.util.DateUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.SpringVersion;
import org.springframework.core.annotation.AnnotationConfigurationException;
import org.springframework.core.env.Environment;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * The type Spring launcher.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringLauncher {

	private static SpringApplication application;

	/**
	 * Springboot主启动
	 * <p>
	 * 自动推到当前类Class
	 *
	 * @param args the args
	 * @return the configurable application context
	 */
	@SneakyThrows
	public static ConfigurableApplicationContext run(String[] args) {
		Class<?> mainClass = Arrays.stream(Thread.currentThread().getStackTrace())
			.map(StackTraceElement::getClassName)
			.filter(StringUtils::isNotBlank)
			.map(name -> ClassUtils.resolveClassName(name, Thread.currentThread().getContextClassLoader()))
			.filter(type -> type.isAnnotationPresent(SpringBootApplication.class))
			.findFirst()
			.orElseThrow(() -> new AnnotationConfigurationException(" 缺少@" + SpringBootApplication.class.getName() + "注解"));
		return run(mainClass, args);
	}

	/**
	 * Springboot主启动
	 *
	 * @param targetClass the primary source to load
	 * @param args        the application arguments (usually passed from a Java main method)
	 * @return the configurable application context
	 */
	public static ConfigurableApplicationContext run(Class<?> targetClass, String[] args) {
		application = new SpringApplicationBuilder(targetClass)
			.banner(CloudBanner.create())
			.bannerMode(Banner.Mode.CONSOLE)
			.build(args);
		return application.run(args);
	}

	/**
	 * Application spring application.
	 *
	 * @return the spring application
	 */
	public static SpringApplication application() {
		return application;
	}

	@NoArgsConstructor(staticName = "create")
	private static class CloudBanner implements Banner {
		private static final String banner = """
			 ██       ██          ██         ██████   ██                       ██
			░██      ░░          ░██        ██░░░░██ ░██                      ░██
			░██       ██ ██    ██░██  ██   ██    ░░  ░██  ██████  ██   ██     ░██
			░██      ░██░██   ░██░██ ██   ░██        ░██ ██░░░░██░██  ░██  ██████
			░██      ░██░░██ ░██ ░████    ░██        ░██░██   ░██░██  ░██ ██░░░██
			░██      ░██ ░░████  ░██░██   ░░██    ██ ░██░██   ░██░██  ░██░██  ░██
			░████████░██  ░░██   ░██░░██   ░░██████  ███░░██████ ░░██████░░██████
			░░░░░░░░ ░░    ░░    ░░  ░░     ░░░░░░  ░░░  ░░░░░░   ░░░░░░  ░░░░░░
			""";

		private static String getVersion() {
			Package pkg = SpringLauncher.class.getPackage();
			return (pkg != null ? pkg.getImplementationVersion() : null);
		}

		@Override
		public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
			if (environment.getProperty("spring.banner.enabled", Boolean.class, true)) {
				out.println(banner);
				int max = banner.lines().mapToInt(String::length).max().orElse(0);
				new Format(max, out)
					.println(" Spring Version: " + SpringVersion.getVersion() + " ")
					.println(" Spring Boot Version: " + SpringBootVersion.getVersion() + " ")
					.println(" Spring Boot Extension Version: " + getVersion() + " ")
					.println(" Current time: " + DateUtils.format(LocalDateTime.now(), DateUtils.YMD_HMS) + " ")
					.println(" Current JDK Version: " + environment.getProperty("java.version") + " ")
					.println(" Operating System: " + environment.getProperty("os.name") + " ")
					.flush();
			}
		}
	}

	private static class Format {

		private final static char ch = '*';
		private final int n;
		private final PrintStream out;

		/**
		 * Instantiates a new Format.
		 *
		 * @param max the max
		 * @param out the out
		 */
		Format(int max, PrintStream out) {
			n = max % 2 == 0 ? max : max + 1;
			this.out = out;
		}

		/**
		 * Accept.
		 *
		 * @param str the str
		 * @return the format
		 */
		public Format println(String str) {
			int length = str.length();
			if (length < n) {
				int index = (n - length) >> 1;
				str = StringUtils.leftPad(str, length + index, ch);
				str = StringUtils.rightPad(str, n, ch);
			}
			out.println(str);
			return this;
		}

		/**
		 * Flush.
		 */
		public void flush() {
			out.flush();
		}
	}
}
