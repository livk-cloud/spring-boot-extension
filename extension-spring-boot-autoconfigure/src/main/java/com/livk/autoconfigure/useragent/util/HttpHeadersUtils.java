package com.livk.autoconfigure.useragent.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * HttpHeadersUtils
 * </p>
 *
 * @author livk
 * @date 2022/12/29
 */
@UtilityClass
public class HttpHeadersUtils {
    public Map<String, String> headersConcat(HttpHeaders headers, CharSequence delimiter) {
        return headers.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> String.join(delimiter, entry.getValue())));
    }
}
