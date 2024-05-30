/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.commons;

import com.google.common.annotations.Beta;
import com.livk.commons.util.ClassUtils;
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
import org.springframework.core.env.Environment;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 自定义Spring启动器
 * <p>
 * 内嵌Banner图、以及各种版本
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpringLauncher {

	private volatile static SpringApplication application;

	/**
	 * Springboot主启动
	 * <p>
	 * 自动推导当前类Class
	 * @param args main方法参数
	 * @return ConfigurableApplicationContext
	 */
	@Beta
	@SneakyThrows
	public static ConfigurableApplicationContext run(String[] args) {
		Class<?>[] targetClassArray = Arrays.stream(Thread.currentThread().getStackTrace())
			.map(StackTraceElement::getClassName)
			.filter(StringUtils::isNotBlank)
			.map(ClassUtils::resolveClassName)
			.filter(type -> type.isAnnotationPresent(SpringBootApplication.class))
			.toArray(Class[]::new);
		return run(targetClassArray, args);
	}

	private static ConfigurableApplicationContext run(Class<?>[] targetClassArray, String[] args) {
		application = new SpringApplicationBuilder(targetClassArray).banner(CloudBanner.create())
			.bannerMode(Banner.Mode.CONSOLE)
			.build(args);
		return application.run(args);
	}

	/**
	 * Springboot主启动
	 * @param targetClass 主启动类class
	 * @param args main方法参数
	 * @return ConfigurableApplicationContext
	 */
	public static ConfigurableApplicationContext run(Class<?> targetClass, String[] args) {
		return run(new Class<?>[] { targetClass }, args);
	}

	/**
	 * 获取当前包版本
	 * @return the version
	 */
	public static String getVersion() {
		Package pkg = SpringLauncher.class.getPackage();
		return (pkg != null ? pkg.getImplementationVersion() : null);
	}

	/**
	 * 获取到SpringApplication
	 * @return SpringApplication
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

		@Override
		public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
			if (environment.getProperty("spring.banner.enabled", Boolean.class, true)) {
				out.println(banner);
				int max = banner.lines().mapToInt(String::length).max().orElse(0);
				new Format(max, out).println(" Spring Version: " + SpringVersion.getVersion() + " ")
					.println(" Spring Boot Version: " + SpringBootVersion.getVersion() + " ")
					.println(" Spring Boot Extension Version: " + getVersion() + " ")
					.println(" Current Time: " + DateUtils.format(LocalDateTime.now(), DateUtils.YMD_HMS) + " ")
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
		 * 构建Format
		 * @param max the max
		 * @param out the out
		 */
		Format(int max, PrintStream out) {
			n = max % 2 == 0 ? max : max + 1;
			this.out = out;
		}

		/**
		 * 输出字符串 前后使用*补全
		 * @param str the str
		 * @return the format
		 */
		public Format println(String str) {
			if (!str.contains("null")) {
				int length = str.length();
				if (length < n) {
					int index = (n - length) >> 1;
					str = StringUtils.leftPad(str, length + index, ch);
					str = StringUtils.rightPad(str, n, ch);
				}
				out.println(str);
			}
			return this;
		}

		/**
		 * 刷新输出
		 */
		public void flush() {
			out.flush();
		}

	}

}
