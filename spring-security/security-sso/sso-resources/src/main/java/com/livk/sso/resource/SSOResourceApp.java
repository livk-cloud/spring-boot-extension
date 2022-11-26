package com.livk.sso.resource;

import com.livk.spring.LivkSpring;
import com.livk.sso.resource.config.RsaKeyProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * <p>
 * SSOResourceApp
 * </p>
 *
 * @author livk
 * @date 2022/4/11
 */
@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class SSOResourceApp {

    public static void main(String[] args) {
        LivkSpring.run(SSOResourceApp.class, args);
    }

}
