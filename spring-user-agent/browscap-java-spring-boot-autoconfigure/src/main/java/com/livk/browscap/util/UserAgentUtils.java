package com.livk.browscap.util;

import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.UserAgentParser;
import com.livk.support.SpringContextHolder;
import lombok.experimental.UtilityClass;

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

    private static final UserAgentParser PARSER;

    static {
        PARSER = SpringContextHolder.getBean(UserAgentParser.class);
    }

    public Capabilities parse(String userAgent) {
        return PARSER.parse(userAgent);
    }
}
