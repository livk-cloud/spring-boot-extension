package com.livk.autoconfigure.useragent.yauaa.util;

import com.livk.autoconfigure.useragent.util.HttpHeadersUtils;
import com.livk.commons.support.SpringContextHolder;
import com.livk.commons.web.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Map;

/**
 * <p>
 * UserAgentUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class UserAgentUtils {
    private static final UserAgentAnalyzer ANALYZER;

    static {
        ANALYZER = SpringContextHolder.getBean(UserAgentAnalyzer.class);
    }

    private UserAgent parse(HttpHeaders headers) {
        Map<String, String> headersConcat = HttpHeadersUtils.headersConcat(headers, ",");
        return ANALYZER.parse(headersConcat);
    }

    /**
     * Parse user agent.
     *
     * @param request the request
     * @return the user agent
     */
    public UserAgent parse(HttpServletRequest request) {
        return parse(WebUtils.headers(request));
    }

    /**
     * Parse user agent.
     *
     * @param request the request
     * @return the user agent
     */
    public UserAgent parse(ServerHttpRequest request) {
        return parse(request.getHeaders());
    }
}
