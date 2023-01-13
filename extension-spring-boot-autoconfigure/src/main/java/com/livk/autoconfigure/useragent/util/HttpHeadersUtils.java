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
 */
@UtilityClass
public class HttpHeadersUtils {
    /**
     * Headers concat map.
     *
     * @param headers   the headers
     * @param delimiter the delimiter
     * @return the map
     */
    public Map<String, String> headersConcat(HttpHeaders headers, CharSequence delimiter) {
        return headers.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> String.join(delimiter, entry.getValue())));
    }
}
