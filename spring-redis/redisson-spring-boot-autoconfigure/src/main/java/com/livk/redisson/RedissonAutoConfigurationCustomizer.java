package com.livk.redisson;

import org.redisson.config.Config;

/**
 * <p>
 * RedissonAutoConfigurationCustomizer
 * </p>
 *
 * @author livk
 * @date 2022/9/16
 */
@FunctionalInterface
public interface RedissonAutoConfigurationCustomizer {

    /**
     * Customize the RedissonClient configuration.
     *
     * @param configuration the {@link Config} to customize
     */
    void customize(final Config configuration);
}
