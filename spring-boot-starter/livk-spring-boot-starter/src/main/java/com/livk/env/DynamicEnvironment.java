package com.livk.env;

import com.livk.util.JacksonUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>
 * 实现EnvironmentPostProcessor需要通过SPI来进行注册
 * </p>
 *
 * @author livk
 * @date 2022/3/25
 */
@RequiredArgsConstructor
public class DynamicEnvironment implements EnvironmentPostProcessor {

    private static final String source = "env.json";

    private final Log log;

    private final ConfigurableBootstrapContext context;

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        log.info("context:" + context);
        MutablePropertySources propertySources = environment.getPropertySources();
        try {
            ClassPathResource resource = new ClassPathResource(source);
            if (resource.exists()) {
                InputStream inputStream = resource.getInputStream();
                Properties properties = JacksonUtils.toProperties(inputStream);
                PropertiesPropertySource livkSource = new PropertiesPropertySource("livkSource", properties);
                propertySources.addLast(livkSource);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
