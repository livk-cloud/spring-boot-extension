package com.livk.commons.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.livk.commons.jackson.support.JacksonSupport;
import com.livk.commons.jackson.support.JacksonType;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * The type Json mapper utils.
 *
 * @author livk
 */
@UtilityClass
public class JsonMapperUtils {

    private static final JacksonSupport<JsonMapper> JSON;

    static {
        Function<MapperBuilder<? extends ObjectMapper, ?>, MapperBuilder<? extends ObjectMapper, ?>> function =
                mapperBuilder -> mapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL);
        JSON = JacksonSupport.create(JacksonType.JSON, function);
    }

    /**
     * Read value t.
     *
     * @param <T>  the type parameter
     * @param obj  the obj
     * @param type the type
     * @return the t
     */
    public static <T> T readValue(Object obj, Class<T> type) {
        return JSON.readValue(obj, type);
    }

    /**
     * Read value t.
     *
     * @param <T>  the type parameter
     * @param obj  the obj
     * @param type the type
     * @return the t
     */
    public static <T> T readValue(Object obj, JavaType type) {
        return JSON.readValue(obj, type);
    }

    /**
     * Read value t.
     *
     * @param <T>           the type parameter
     * @param obj           the obj
     * @param typeReference the type reference
     * @return the t
     */
    public static <T> T readValue(Object obj, TypeReference<T> typeReference) {
        return JSON.readValue(obj, typeReference);
    }

    /**
     * json序列化
     *
     * @param obj obj
     * @return json string
     */
    public static String writeValueAsString(Object obj) {
        return JSON.writeValueAsString(obj);
    }

    /**
     * Write value as bytes byte [ ].
     *
     * @param obj the obj
     * @return the byte [ ]
     */
    public static byte[] writeValueAsBytes(Object obj) {
        return JSON.writeValueAsBytes(obj);
    }

    /**
     * json反序列化成List
     * <p>也可以看看{@link JacksonSupport#readValue(Object, TypeReference)} ,
     * <p> {@link JacksonSupport#convertValue(Object, JavaType)}
     *
     * @param <T>  泛型
     * @param obj  the obj
     * @param type 类型
     * @return the list
     */
    public static <T> List<T> readValueList(Object obj, Class<T> type) {
        CollectionType collectionType = JacksonSupport.collectionType(type);
        return JSON.readValue(obj, collectionType);
    }

    /**
     * json反序列化成Map
     * <p>也可以看看{@link JacksonSupport#readValue(Object, TypeReference)} ,
     * <p> {@link JacksonSupport#convertValue(Object, JavaType)}
     *
     * @param <K>        the type parameter
     * @param <V>        the type parameter
     * @param obj        the obj
     * @param keyClass   K Class
     * @param valueClass V Class
     * @return the map
     */
    public static <K, V> Map<K, V> readValueMap(Object obj, Class<K> keyClass, Class<V> valueClass) {
        MapType mapType = JacksonSupport.mapType(keyClass, valueClass);
        return JSON.readValue(obj, mapType);
    }

    /**
     * 将json转化成JsonNode
     *
     * @param obj the obj
     * @return the json node
     */
    public static JsonNode readTree(Object obj) {
        return JSON.readTree(obj);
    }

    /**
     * Convert object.
     *
     * @param <T>       the type parameter
     * @param fromValue the  value
     * @param type      the type
     * @return the object
     */
    public static <T> T convertValue(Object fromValue, Class<T> type) {
        return JSON.convertValue(fromValue, type);
    }

    /**
     * Convert value t.
     *
     * @param <T>           the type parameter
     * @param fromValue     the value
     * @param typeReference the type reference
     * @return the t
     */
    public static <T> T convertValue(Object fromValue, TypeReference<T> typeReference) {
        return JSON.convertValue(fromValue, typeReference);
    }

    /**
     * Convert object.
     *
     * @param <T>       the type parameter
     * @param fromValue the  value
     * @param javaType  the java type
     * @return the object
     */
    public static <T> T convertValue(Object fromValue, JavaType javaType) {
        return JSON.convertValue(fromValue, javaType);
    }

    /**
     * Object to map map.
     *
     * @param <K>        the type parameter
     * @param <V>        the type parameter
     * @param fromValue  the fromValue
     * @param keyClass   the key class
     * @param valueClass the value class
     * @return the map
     */
    public static <K, V> Map<K, V> convertValueMap(Object fromValue, Class<K> keyClass, Class<V> valueClass) {
        MapType mapType = JacksonSupport.mapType(keyClass, valueClass);
        return JSON.convertValue(fromValue, mapType);
    }
}
