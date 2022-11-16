package com.livk.video.example.support;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
 * <p>
 * VideoResourceHttpRequestHandler
 * </p>
 *
 * @author livk
 * @date 2022/11/16
 */
@Component
public class VideoResourceHttpRequestHandler extends ResourceHttpRequestHandler {

    public static final String ATTR = "VIDEO-FILE";

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    @Override
    protected Resource getResource(HttpServletRequest request) {
        String location = (String) request.getAttribute(ATTR);
        return resourceResolver.getResource(location);
    }
}
