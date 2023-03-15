package com.livk.commons.web;

import com.livk.commons.collect.util.StreamUtils;
import com.livk.commons.util.ObjectUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * RequestWrapper
 * </p>
 *
 * @author livk
 */
public final class RequestParameterWrapper extends HttpServletRequestWrapper {

    private final Map<String, String[]> parameter = new LinkedHashMap<>(16);

    /**
     * Instantiates a new Request parameter wrapper.
     *
     * @param request the request
     */
    public RequestParameterWrapper(HttpServletRequest request) {
        super(request);
        parameter.putAll(request.getParameterMap());
    }

    @Override
    public String getParameter(String name) {
        String[] values = getParameterValues(name);
        return ObjectUtils.isEmpty(values) ? null : values[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameter;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(parameter.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        return parameter.get(name);
    }

    /**
     * Put.
     *
     * @param name   the name
     * @param values the values
     */
    public void put(String name, String[] values) {
        parameter.merge(name, values,
                (oldValues, newValues) -> StreamUtils.concat(oldValues, newValues).distinct().toArray(String[]::new));
    }

    /**
     * Put.
     *
     * @param name  the name
     * @param value the value
     */
    public void put(String name, String value) {
        put(name, new String[]{value});
    }
}
