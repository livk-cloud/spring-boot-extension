package com.livk.autoconfigure.useragent.browscap.support;

import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.UserAgentParser;
import com.livk.autoconfigure.useragent.support.HttpUserAgentParser;
import com.livk.commons.bean.Wrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

/**
 * <p>
 * YauaaUserAgentParse
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public class BrowscapUserAgentParse implements HttpUserAgentParser<Capabilities> {

    private final UserAgentParser userAgentParser;

    @Override
    public Wrapper<Capabilities> parse(HttpHeaders headers) {
        String userAgent = headers.getFirst(HttpHeaders.USER_AGENT);
        Capabilities capabilities = userAgentParser.parse(userAgent);
        return Wrapper.of(capabilities);
    }
}
