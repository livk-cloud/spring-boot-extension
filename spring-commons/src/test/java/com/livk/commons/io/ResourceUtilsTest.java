package com.livk.commons.io;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
class ResourceUtilsTest {

    @Test
    void getResource() throws IOException {
        String fileName = "input.json";
        Resource resource = ResourceUtils.getResource(ResourceUtils.CLASSPATH_URL_PREFIX + fileName);

        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + fileName);

        assertEquals(file, resource.getFile());

        Resource[] resources = ResourceUtils.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + fileName);
        for (Resource resourceObj : resources) {
            assertEquals(file, resourceObj.getFile());
        }
    }
}
