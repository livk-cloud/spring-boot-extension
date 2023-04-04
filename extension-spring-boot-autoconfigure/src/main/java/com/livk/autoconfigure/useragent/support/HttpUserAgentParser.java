package com.livk.autoconfigure.useragent.support;

import com.livk.commons.bean.Wrapper;
import org.springframework.http.HttpHeaders;

/**
 * <p>
 * UserAgentParse
 * </p>
 *
 * @author livk
 */
public interface HttpUserAgentParser {

    /**
     * Parse wrapper.
     *
     * @param headers the headers
     * @return the wrapper
     */
    Wrapper parse(HttpHeaders headers);
}
