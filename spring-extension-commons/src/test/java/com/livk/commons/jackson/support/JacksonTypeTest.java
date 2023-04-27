package com.livk.commons.jackson.support;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class JacksonTypeTest {

    @Test
    void builder() {
        assertTrue(JacksonType.JSON.builder().build() instanceof JsonMapper);
        assertTrue(JacksonType.YAML.builder().build() instanceof YAMLMapper);
        assertTrue(JacksonType.XML.builder().build() instanceof XmlMapper);
    }
}
