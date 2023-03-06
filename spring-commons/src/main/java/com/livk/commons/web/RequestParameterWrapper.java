package com.livk.commons.web;

import com.livk.commons.util.ObjectUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

/**
 * <p>
 * RequestWrapper
 * </p>
 *
 * @author livk
 */
public final class RequestParameterWrapper extends HttpServletRequestWrapper {

    private final Hashtable<String, String[]> parameter = new Hashtable<>();

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
        return parameter.keys();
    }

    @Override
    public String[] getParameterValues(String name) {
        return parameter.get(name);
    }

    /**
     * Put.
     *
     * @param name  the name
     * @param value the value
     */
    public void put(String name, String[] value) {
        parameter.put(name, value);
    }

    /**
     * Put.
     *
     * @param name  the name
     * @param value the value
     */
    public void put(String name, String value) {
        String[] values = getParameterValues(name);
        if (ObjectUtils.isEmpty(values)) {
            put(name, new String[]{value});
        } else {
            put(name, ObjectUtils.addObjectToArray(values, value));
        }
    }
}
