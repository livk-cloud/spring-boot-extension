package com.livk.autoconfigure.redisearch;

import com.livk.commons.function.Customizer;
import io.lettuce.core.RedisURI;

/**
 * The interface Redis uri customizer.
 *
 * @author livk
 */
@FunctionalInterface
public interface RedisURICustomizer extends Customizer<RedisURI> {
}
