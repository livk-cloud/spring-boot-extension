package com.livk.commons.jackson.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.livk.commons.bean.util.BeanUtils;
import lombok.RequiredArgsConstructor;

/**
 * The enum Object mapper type.
 *
 * @author livk
 */
@RequiredArgsConstructor
public enum JacksonType {

    /**
     * Json object mapper type.
     */
    JSON(JsonMapper.class, JsonMapper.Builder.class),

    /**
     * Yaml object mapper type.
     */
    YAML(YAMLMapper.class, YAMLMapper.Builder.class),

    /**
     * Xml object mapper type.
     */
    XML(XmlMapper.class, XmlMapper.Builder.class);

    private final Class<? extends ObjectMapper> type;

    private final Class<? extends MapperBuilder<? extends ObjectMapper, ?>> buildClass;

    public MapperBuilder<? extends ObjectMapper, ?> builder() {
        try {
            ObjectMapper mapper = BeanUtils.instantiateClass(type);
            return buildClass.getConstructor(type).newInstance(mapper);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
