package com.livk.util;

import lombok.experimental.UtilityClass;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import java.util.Collections;
import java.util.Map;

/**
 * <p>
 * EnvironmentUtils
 * </p>
 *
 * @author livk
 * @date 2022/10/12
 */
@UtilityClass
public class EnvironmentUtils {

    public Map<String, String> getSubProperties(Environment environment, String keyPrefix) {
        return Binder.get(environment).bind(keyPrefix, Bindable.mapOf(String.class, String.class))
                .orElseGet(Collections::emptyMap);
    }
}
