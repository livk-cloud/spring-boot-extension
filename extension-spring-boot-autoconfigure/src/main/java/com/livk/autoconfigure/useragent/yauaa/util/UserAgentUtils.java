package com.livk.autoconfigure.useragent.yauaa.util;

import com.livk.support.SpringContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * <p>
 * UserAgentUtils
 * </p>
 *
 * @author livk
 * @date 2022/9/30
 */
@UtilityClass
public class UserAgentUtils {
    private static final UserAgentAnalyzer ANALYZER;

    static {
        ANALYZER = SpringContextHolder.getBean(UserAgentAnalyzer.class);
    }

    public UserAgent parse(String userAgent) {
        return ANALYZER.parse(userAgent);
    }

    public UserAgent parse(HttpServletRequest request) {
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        return parse(userAgent);
    }

    public UserAgent parse(ServerHttpRequest request) {
        String userAgent = request.getHeaders().getFirst(HttpHeaders.USER_AGENT);
        return parse(userAgent);
    }
}
