package com.livk.commons.web;

import com.livk.commons.collect.util.StreamUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author livk
 */
public class RequestHeaderWrapper extends HttpServletRequestWrapper {

    private final HttpHeaders headers = new HttpHeaders();

    public RequestHeaderWrapper(HttpServletRequest request) {
        super(request);
    }

    public void addHeader(String name, String value) {
        headers.add(name, value);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = super.getHeader(name);
        if (StringUtils.hasText(headerValue)) {
            return headerValue;
        }
        return headers.getFirst(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Enumeration<String> superHeaderNames = super.getHeaderNames();
        Set<String> set = headers.keySet();
        Set<String> headerNames = Stream.concat(StreamUtils.convert(superHeaderNames), set.stream())
                .collect(Collectors.toSet());
        return Collections.enumeration(headerNames);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> headerValue = Collections.list(super.getHeaders(name));
        List<String> list = headers.get(name);
        if (!CollectionUtils.isEmpty(list)) {
            headerValue.addAll(list);
        }
        return Collections.enumeration(headerValue);
    }
}
