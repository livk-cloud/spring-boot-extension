package com.livk.commons.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.livk.commons.domain.Wrapper;
import com.livk.commons.function.Present;
import com.livk.commons.support.SpringContextHolder;
import com.livk.commons.util.ObjectUtils;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Jackson一些基本序列化与反序列化
 * 自定义Mapper，请注册{@link Wrapper}到IOC
 */
@Slf4j
@UtilityClass
public class JacksonUtils {

    private static final ObjectMapper MAPPER;

    static {
        Wrapper<ObjectMapper> wrapper = null;
        AtomicReference<ObjectMapper> atomicReference = new AtomicReference<>();
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(Wrapper.class, ObjectMapper.class);
        try {
            wrapper = SpringContextHolder.<Wrapper<ObjectMapper>>getBeanProvider(resolvableType).getIfUnique();
        } catch (NullPointerException e) {
            log.debug(JacksonUtils.class + "在非Spring环境中使用!");
        } catch (Exception e) {
            log.debug("spring ioc缺少type:" + resolvableType.getType() + " 的Bean");
        }
        Present.handler(wrapper, Objects::nonNull)
                .present(mapperWrapper -> atomicReference.set(mapperWrapper.obj())
                        , () -> atomicReference.set(JsonMapper.builder().build()));
        MAPPER = atomicReference.get();
        MAPPER.registerModules(new JavaTimeModule());
    }

    /**
     * json字符转Bean
     *
     * @param <T>   type
     * @param json  json string
     * @param clazz class
     * @return T t
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> T toBean(String json, Class<T> clazz) {
        if (check(json, clazz)) {
            return null;
        }
        if (clazz.isInstance(json)) {
            return (T) json;
        }
        return MAPPER.readValue(json, clazz);

    }

    /**
     * To bean t.
     *
     * @param <T>         the type parameter
     * @param inputStream the input stream
     * @param clazz       the clazz
     * @return the t
     */
    @SneakyThrows
    public static <T> T toBean(InputStream inputStream, Class<T> clazz) {
        return (inputStream == null || clazz == null) ? null :
                MAPPER.readValue(inputStream, clazz);
    }

    /**
     * 序列化
     *
     * @param obj obj
     * @return json string
     */
    @SneakyThrows
    public static String toJsonStr(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        return MAPPER.writeValueAsString(obj);
    }

    /**
     * json to List
     *
     * @param <T>   泛型
     * @param json  json数组
     * @param clazz 类型
     * @return the list
     */
    @SneakyThrows
    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (check(json, clazz)) {
            return new ArrayList<>();
        }
        CollectionType collectionType = MAPPER.getTypeFactory()
                .constructCollectionType(List.class, clazz);
        return MAPPER.readValue(json, collectionType);
    }

    /**
     * json反序列化Map
     *
     * @param <K>        the type parameter
     * @param <V>        the type parameter
     * @param json       json字符串
     * @param keyClass   K Class
     * @param valueClass V Class
     * @return the map
     */
    @SneakyThrows
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (check(json, keyClass, valueClass)) {
            return Collections.emptyMap();
        }
        MapType mapType = MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
        return MAPPER.readValue(json, mapType);
    }

    /**
     * To properties properties.
     *
     * @param inputStream the input stream
     * @return the properties
     */
    @SneakyThrows
    public Properties toProperties(InputStream inputStream) {
        if (inputStream == null) {
            return new Properties();
        }
        JavaType javaType = MAPPER.getTypeFactory().constructType(Properties.class);
        return MAPPER.readValue(inputStream, javaType);
    }

    /**
     * To bean t.
     *
     * @param <T>           the type parameter
     * @param json          the json
     * @param typeReference the type reference
     * @return the t
     */
    @SneakyThrows
    public <T> T toBean(String json, TypeReference<T> typeReference) {
        return check(json, typeReference) ? null :
                MAPPER.readValue(json, typeReference);
    }

    /**
     * Read tree json node.
     *
     * @param json the json
     * @return the json node
     */
    @SneakyThrows
    public JsonNode readTree(String json) {
        return MAPPER.readTree(json);
    }

    /**
     * Object to map map.
     *
     * @param <K>        the type parameter
     * @param <V>        the type parameter
     * @param datum      the datum
     * @param keyClass   the key class
     * @param valueClass the value class
     * @return the map
     */
    public static <K, V> Map<K, V> objectToMap(Object datum, Class<K> keyClass, Class<V> valueClass) {
        MapType mapType = MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
        return MAPPER.convertValue(datum, mapType);
    }

    private boolean check(String json, Object... checkObj) {
        return json == null || json.isEmpty() || ObjectUtils.anyChecked(Objects::isNull, checkObj);
    }
}
