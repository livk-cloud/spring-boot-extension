package com.livk.autoconfigure.redisson;

import com.livk.function.Customizer;
import org.redisson.config.Config;

/**
 * <p>
 * Customize the RedissonClient configuration.
 * *
 * * @param configuration the {@link Config} to customize
 * </p>
 *
 * @author livk
 * @date 2022/9/16
 */
public interface RedissonAutoConfigurationCustomizer extends Customizer<Config> {

}
