package com.livk.sso.auth;

import com.livk.sso.util.RSAUtils;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ResourceUtils;

/**
 * <p>
 * RASFileInit
 * </p>
 *
 * @author livk
 * @date 2022/11/26
 */
public class RASFileInit implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @SneakyThrows
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        String path = ResourceUtils.getURL("classpath:")
                .getPath().replaceAll("/build/.*", "/src/main/resources");
        RSAUtils.generateFile(path, "livk", 1024);
    }
}
