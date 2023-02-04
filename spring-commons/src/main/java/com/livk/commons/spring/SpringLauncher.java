package com.livk.commons.spring;

import com.livk.commons.util.DateUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
     * Run configurable application context.
     *
     * @param <T>         the type parameter
     * @param targetClass the target class
     * @param args        the args
     * @return the configurable application context
     */
    public static <T> ConfigurableApplicationContext run(Class<T> targetClass, String[] args) {
        if (!targetClass.isAnnotationPresent(SpringBootApplication.class)) {
            throw new AnnotationConfigurationException("must use @SpringBootApplication in start class");
        }
        SpringApplicationBuilder builder = new SpringApplicationBuilder(targetClass)
                .banner(CloudBanner.create())
                .bannerMode(Banner.Mode.CONSOLE);
        application = builder.application();
        return builder.run(args);
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

        @SneakyThrows
        @Override
        public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
            out.println(banner);
            int max = Arrays.stream(banner.split("\n")).mapToInt(String::length).max().orElse(0);
            max = max % 2 == 0 ? max : max + 1;
            Format format = Format.of(max, out);
            format.accept(" Spring Version: " + SpringVersion.getVersion() + " ");
            format.accept(" Spring Boot Version: " + SpringBootVersion.getVersion() + " ");
            format.accept(" Current time: " + DateUtils.format(LocalDateTime.now(), DateUtils.YMD_HMS) + " ");
            format.accept(" Current JDK Version: " + System.getProperty("java.version") + " ");
            format.accept(" Operating System: " + System.getProperty("os.name") + " ");
            out.flush();
        }
    }

    @RequiredArgsConstructor(staticName = "of")
    private static class Format {

        private final static char ch = '*';
        private final int n;
        private final PrintStream out;

        /**
         * Accept.
         *
         * @param str the str
         */
        public void accept(String str) {
            int length = str.length();
            if (length < n) {
                int index = (n - length) >> 1;
                str = StringUtils.leftPad(str, length + index, ch);
                str = StringUtils.rightPad(str, n, ch);
            }
            out.println(str);
        }
    }
}
