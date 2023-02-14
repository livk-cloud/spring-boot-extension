package com.livk.autoconfigure.useragent.support;

import com.livk.commons.bean.Wrapper;
import org.springframework.http.HttpHeaders;

/**
 * <p>
 * UserAgentParse
 * </p>
 *
 * @param <T> the type parameter
 * @author livk
 */
public interface HttpUserAgentParser<T> {

    /**
     * Parse wrapper.
     *
     * @param headers the headers
     * @return the wrapper
     */
    Wrapper<T> parse(HttpHeaders headers);
}
