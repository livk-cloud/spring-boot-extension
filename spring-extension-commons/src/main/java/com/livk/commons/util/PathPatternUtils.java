/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
