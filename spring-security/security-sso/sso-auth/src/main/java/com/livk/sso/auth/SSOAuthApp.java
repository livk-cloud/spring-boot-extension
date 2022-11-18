package com.livk.sso.auth;

import com.livk.spring.LivkSpring;
import com.livk.sso.auth.config.RsaKeyProperties;
import com.livk.sso.util.RSAUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

/**
 * <p>
 * SSOAuthApp
 * </p>
 *
 * @author livk
 * @date 2022/4/11
 */
@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class SSOAuthApp {

    public static void main(String[] args) throws FileNotFoundException {
        String path = ResourceUtils.getURL("classpath:")
                .getPath().replaceAll("/build/.*", "/src/main/resources");
        RSAUtils.generateFile(path, "livk", 1024);
//        LivkSpring.run(SSOAuthApp.class, args);
    }

}
