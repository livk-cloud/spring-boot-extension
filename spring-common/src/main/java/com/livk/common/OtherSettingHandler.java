package com.livk.common;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * <p>
 * OtherBannerHandler
 * </p>
 *
 * @author livk
 * @date 2022/5/26
 */
@Configuration
public class OtherSettingHandler implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final String MP_BANNER = "mybatis-plus.global-config.banner";

	private static final String PAGEHELPER_BANNER = "pagehelper.banner";

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		Environment environment = applicationContext.getEnvironment();
		boolean MyBatisPlusBannerEnable = Boolean
				.parseBoolean(System.getProperty(MP_BANNER, environment.getProperty(MP_BANNER)));
		System.setProperty(MP_BANNER, Boolean.toString(MyBatisPlusBannerEnable));

		boolean PageHelperBannerEnable = Boolean
				.parseBoolean(System.getProperty(MP_BANNER, environment.getProperty(PAGEHELPER_BANNER)));
		System.setProperty(PAGEHELPER_BANNER, Boolean.toString(PageHelperBannerEnable));
	}

}
