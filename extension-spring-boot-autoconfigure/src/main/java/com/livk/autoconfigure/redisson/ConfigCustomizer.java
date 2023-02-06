package com.livk.autoconfigure.redisson;

import com.livk.commons.function.Customizer;
import org.redisson.config.Config;

/**
 * <p>
 * Customize the RedissonClient configuration.
 * *
 * * @param configuration the {@link Config} to customize
 * </p>
 *
 * @author livk
 */
@FunctionalInterface
public interface ConfigCustomizer extends Customizer<Config> {

}
