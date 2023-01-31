package com.livk.autoconfigure.useragent.yauaa.support;

import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;
import com.livk.commons.domain.Wrapper;
import lombok.RequiredArgsConstructor;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.http.HttpHeaders;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * YauaaUserAgentParse
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public class YauaaUserAgentParse implements HttpUserAgentParser<UserAgent> {

    private final UserAgentAnalyzer userAgentAnalyzer;

    @Override
    public Wrapper<UserAgent> parse(HttpHeaders headers) {
        Map<String, String> headersConcat = headers.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> String.join(",", entry.getValue())));
        UserAgent userAgent = userAgentAnalyzer.parse(headersConcat);
        return new Wrapper<>(userAgent);
    }
}
