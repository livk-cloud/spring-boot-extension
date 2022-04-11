package com.livk.sso.resoure;

import com.livk.common.LivkSpring;
import com.livk.sso.resoure.config.RsaKeyProperties;
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
