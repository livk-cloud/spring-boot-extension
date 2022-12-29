package com.livk.autoconfigure.useragent.yauaa.util;

import com.livk.commons.support.SpringContextHolder;
import com.livk.commons.util.WebUtils;
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

    public UserAgent parse(HttpServletRequest request) {
        Map<String, String> headers = WebUtils.headersConcat(request, ",");
        return ANALYZER.parse(headers);
    }

    public UserAgent parse(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        Map<String, String> headersConcat = WebUtils.headersConcat(headers, ",");
        return ANALYZER.parse(headersConcat);
    }
}
