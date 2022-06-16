package com.livk.sso.auth;

import com.livk.common.LivkSpring;
import com.livk.sso.auth.config.RsaKeyProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

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

	public static void main(String[] args) {
		LivkSpring.run(SSOAuthApp.class, args);
	}

}
