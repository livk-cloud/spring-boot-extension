package com.livk.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The type Jackson util.
 */
@Slf4j
@UtilityClass
public class JacksonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * The constant JSON_EMPTY.
     */
    public static final String JSON_EMPTY = "{}";

    /**
     * json字符转Bean
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T toBean(String json, Class<T> clazz) {
        if (json == null || json.isEmpty() || clazz == null) {
            return null;
        }
        if (clazz.isInstance(json)) {
            return (T) json;
        }
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("[Jackson反序列化失败 json<{}> clazz<{}>]", json, clazz);
            e.printStackTrace();
        }
        return BeanUtils.instantiateClass(clazz);
    }

    public static <T> T toBean(InputStream inputStream, Class<T> clazz) {
        if (inputStream == null || clazz == null) {
            return null;
        }
        try {
            return MAPPER.readValue(inputStream, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BeanUtils.instantiateClass(clazz);
    }


    /**
     * 序列化
     *
     * @param obj
     * @return
     */
    public static String toJsonStr(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("[Jackson序列化失败 obj<{}>]", obj);
            e.printStackTrace();
        }
        return JSON_EMPTY;
    }

    /**
     * json to List
     *
     * @param json  json数组
     * @param clazz 类型
     * @param <T>   泛型
     * @return List<T>
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (json == null || json.isEmpty() || clazz == null) {
            return new ArrayList<>();
        }
        CollectionType collectionType = MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
        try {
            return MAPPER.readValue(json, collectionType);
        } catch (JsonProcessingException e) {
            log.error("[反序列化Json:<{}>-->List<Obj>:<{}>失败！]", json, clazz);
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * json反序列化Map
     *
     * @param json   json字符串
     * @param kClass K Class
     * @param vClass V Class
     * @return Map
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        if (!StringUtils.hasText(json) || kClass == null || vClass == null) {
            return Collections.emptyMap();
        }
        try {
            var mapType = MAPPER.getTypeFactory().constructMapType(Map.class, kClass, vClass);
            return MAPPER.readValue(json, mapType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

}
