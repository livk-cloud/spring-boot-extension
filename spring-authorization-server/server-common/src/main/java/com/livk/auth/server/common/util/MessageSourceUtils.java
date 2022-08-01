package com.livk.auth.server.common.util;

import lombok.experimental.UtilityClass;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * <p>
 * MessageSourceUtils
 * </p>
 *
 * @author livk
 * @date 2022/8/1
 */
@UtilityClass
public class MessageSourceUtils {

    public MessageSource get() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:i18n/errors/messages");
        messageSource.setDefaultLocale(Locale.CHINA);
        return messageSource;
    }
}
