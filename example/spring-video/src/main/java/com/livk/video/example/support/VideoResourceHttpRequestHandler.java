package com.livk.video.example.support;

import com.livk.commons.io.ResourceUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
 * <p>
 * VideoResourceHttpRequestHandler
 * </p>
 *
 * @author livk
 */
@Component
public class VideoResourceHttpRequestHandler extends ResourceHttpRequestHandler {

    public static final String ATTR = "VIDEO-FILE";

    @Override
    protected Resource getResource(HttpServletRequest request) {
        String location = (String) request.getAttribute(ATTR);
        return ResourceUtils.getResource(location);
    }
}
