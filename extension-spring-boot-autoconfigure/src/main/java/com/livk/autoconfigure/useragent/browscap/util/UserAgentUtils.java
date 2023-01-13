package com.livk.autoconfigure.useragent.browscap.util;

import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.UserAgentParser;
import com.livk.commons.support.SpringContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * <p>
 * UserAgentUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class UserAgentUtils {
    private static final UserAgentParser PARSER;

    static {
        PARSER = SpringContextHolder.getBean(UserAgentParser.class);
    }

    /**
     * Parse capabilities.
     *
     * @param userAgent the user agent
     * @return the capabilities
     */
    public Capabilities parse(String userAgent) {
        return PARSER.parse(userAgent);
    }

    /**
     * Parse capabilities.
     *
     * @param request the request
     * @return the capabilities
     */
    public Capabilities parse(HttpServletRequest request) {
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        return parse(userAgent);
    }

    /**
     * Parse capabilities.
     *
     * @param request the request
     * @return the capabilities
     */
    public Capabilities parse(ServerHttpRequest request) {
        String userAgent = request.getHeaders().getFirst(HttpHeaders.USER_AGENT);
        return parse(userAgent);
    }
}
