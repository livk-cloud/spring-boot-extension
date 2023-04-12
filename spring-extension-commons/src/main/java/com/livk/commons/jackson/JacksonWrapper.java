package com.livk.commons.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.livk.commons.bean.GenericWrapper;
import com.livk.commons.spring.context.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * The type Jackson wrapper.
 *
 * @author livk
 */
@Slf4j
@RequiredArgsConstructor
public class JacksonWrapper implements GenericWrapper<ObjectMapper> {

    /**
     * The constant BEAN_NAME.
     */
    public static final String BEAN_NAME = "com.livk.commons.jackson.JacksonWrapper";

    private final ObjectMapper mapper;

    /**
     * Unwrap of context object mapper.
     *
     * @return the object mapper
     */
    public static ObjectMapper unwrapOfContext() {
        GenericWrapper<ObjectMapper> wrapper = null;
        try {
            if (SpringContextHolder.getApplicationContext().containsBean(JacksonWrapper.BEAN_NAME)) {
                wrapper = SpringContextHolder.getBean(JacksonWrapper.BEAN_NAME, JacksonWrapper.class);
            } else {
                throw new NoSuchBeanDefinitionException(JacksonWrapper.BEAN_NAME);
            }
        } catch (Exception e) {
            log.debug("Building 'ObjectMapper'");
        }
        return wrapper == null ? JsonMapper.builder().build() : wrapper.unwrap();
    }

    @Override
    public ObjectMapper unwrap() {
        return mapper;
    }
}
