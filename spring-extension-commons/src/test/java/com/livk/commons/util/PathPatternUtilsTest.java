package com.livk.commons.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
@SpringBootTest({
        "server.servlet.context-path=/web"
})
class PathPatternUtilsTest {

    @Autowired
    ServerProperties serverProperties;

    @Test
    void matches() {
        assertTrue(PathPatternUtils.matches("/api/{id}", "/api/12"));
        assertTrue(PathPatternUtils.matches("/api/{id}/{no}", "/api/12/12"));

        assertTrue(PathPatternUtils.matches("/app/api/{id}", "/app/api/12", "/app"));
        assertTrue(PathPatternUtils.matches("/app/api/{id}/{no}", "/app/api/12/12", "/app"));

        assertTrue(PathPatternUtils.matches("/web/api/{id}", "/web/api/12", serverProperties));
        assertTrue(PathPatternUtils.matches("/web/api/{id}/{no}", "/web/api/12/12", serverProperties));
    }
}
