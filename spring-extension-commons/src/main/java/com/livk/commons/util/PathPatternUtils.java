package com.livk.commons.util;

import lombok.experimental.UtilityClass;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.server.RequestPath;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.net.URI;

/**
 * The type Path pattern utils.
 *
 * @author livk
 */
@UtilityClass
public class PathPatternUtils {

    /**
     * Matches boolean.
     *
     * @param pathPattern the path pattern
     * @param uri         the uri
     * @param contextPath the context path
     * @return the boolean
     */
    public boolean matches(String pathPattern, String uri, String contextPath) {
        PathPattern pattern = PathPatternParser.defaultInstance.parse(pathPattern);
        return pattern.matches(RequestPath.parse(URI.create(uri), contextPath));
    }

    /**
     * Matches boolean.
     *
     * @param pathPattern the path pattern
     * @param uri         the uri
     * @return the boolean
     */
    public boolean matches(String pathPattern, String uri) {
        return matches(pathPattern, uri, "");
    }

    /**
     * Matches boolean.
     *
     * @param pathPattern the path pattern
     * @param uri         the uri
     * @param properties  the properties
     * @return the boolean
     */
    public boolean matches(String pathPattern, String uri, ServerProperties properties) {
        String contextPath = properties.getServlet().getContextPath();
        return matches(pathPattern, uri, contextPath);
    }
}
