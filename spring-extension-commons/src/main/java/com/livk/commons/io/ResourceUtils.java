package com.livk.commons.io;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
 * @author livk
 */
@UtilityClass
public class ResourceUtils extends org.springframework.util.ResourceUtils {

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    /**
     * Gets resource.
     *
     * @param location the location
     * @return the resource
     */
    public Resource getResource(String location) {
        return resourceResolver.getResource(location);
    }

    /**
     * Get resources resource [ ].
     *
     * @param location the location
     * @return the resource [ ]
     * @throws IOException the io exception
     */
    public Resource[] getResources(String location) throws IOException {
        return resourceResolver.getResources(location);
    }
}
