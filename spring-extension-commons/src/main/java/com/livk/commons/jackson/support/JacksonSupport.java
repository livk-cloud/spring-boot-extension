package com.livk.commons.jackson.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.core.ResolvableType;

import java.io.DataInput;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * The type Jackson support.
 *
 * @author livk
 */
public class JacksonSupport<M extends ObjectMapper> {

    private final M mapper;

    private JacksonSupport(M mapper) {
        this.mapper = mapper;
        this.mapper.registerModules(new JavaTimeModule());
    }

    /**
     * Create jackson support.
     *
     * @param mapper the mapper
     * @return the jackson support
     */
    public static <M extends ObjectMapper> JacksonSupport<M> create(M mapper) {
        return new JacksonSupport<>(mapper);
    }

    /**
     * Create jackson support.
     *
     * @param type the type
     * @return the jackson support
     */
    @SuppressWarnings("unchecked")
    public static <M extends ObjectMapper> JacksonSupport<M> create(JacksonType type) {
        M objectMapper = (M) type.builder().build();
        return new JacksonSupport<>(objectMapper);
    }

    /**
     * Create jackson support.
     *
     * @param type the type
     * @return the jackson support
     */
    @SuppressWarnings("unchecked")
    public static <M extends ObjectMapper> JacksonSupport<M> create(JacksonType type,
                                                                    Function<MapperBuilder<? extends ObjectMapper, ?>, MapperBuilder<? extends ObjectMapper, ?>> function) {
        M objectMapper = (M) function.apply(type.builder()).build();
        return new JacksonSupport<>(objectMapper);
    }

    /**
     * 获取当前MAPPER的TypeFactory
     *
     * @return the type factory
     */
    public static TypeFactory typeFactory() {
        return TypeFactory.defaultInstance();
    }

    /**
     * 构建一个JavaType
     *
     * @param targetClass the target class
     * @return the java type
     */
    public static JavaType javaType(Class<?> targetClass) {
        return typeFactory().constructType(targetClass);
    }

    /**
     * 构建一个含有泛型的JavaType
     *
     * @param targetClass the target class
     * @param generics    the generics
     * @return the java type
     */
    public static JavaType javaType(Class<?> targetClass, Class<?>... generics) {
        return typeFactory().constructParametricType(targetClass, generics);
    }

    /**
     * ResolvableType转成Jackson JavaType
     *
     * @param resolvableType the resolvable type
     * @return the java type
     * @see ResolvableType
     */
    public static JavaType javaType(ResolvableType resolvableType) {
        Class<?> rawClass = resolvableType.getRawClass();
        if (resolvableType.getType() instanceof ParameterizedType parameterizedType) {
            JavaType[] javaTypes = Arrays.stream(parameterizedType.getActualTypeArguments())
                    .map(type -> javaType(ResolvableType.forType(type)))
                    .toArray(JavaType[]::new);
            return typeFactory().constructParametricType(rawClass, javaTypes);
        }
        return javaType(rawClass);
    }

    /**
     * 构建一个CollectionType
     *
     * @param <T>         the type parameter
     * @param targetClass the target class
     * @return the java type
     * @see CollectionType
     */
    public static <T> CollectionType collectionType(Class<T> targetClass) {
        return typeFactory().constructCollectionType(Collection.class, targetClass);
    }

    /**
     * 构建一个MapType
     *
     * @param <K>        the type parameter
     * @param <V>        the type parameter
     * @param keyClass   the key class
     * @param valueClass the value class
     * @return the java type
     * @see MapType
     */
    public static <K, V> MapType mapType(Class<K> keyClass, Class<V> valueClass) {
        return typeFactory().constructMapType(Map.class, keyClass, valueClass);
    }

    /**
     * Read value t.
     *
     * @param <T>  the type parameter
     * @param obj  the obj
     * @param type the type
     * @return the t
     */
    @SneakyThrows
    public <T> T readValue(Object obj, Class<T> type) {
        return readValue(obj, javaType(type));
    }

    /**
     * Read value t.
     *
     * @param <T>  the type parameter
     * @param obj  the obj
     * @param type the type
     * @return the t
     */
    @SneakyThrows
    public <T> T readValue(Object obj, JavaType type) {
        if (obj instanceof JsonParser jsonParser) {
            return mapper.readValue(jsonParser, type);
        } else if (obj instanceof File file) {
            return mapper.readValue(file, type);
        } else if (obj instanceof URL url) {
            return mapper.readValue(url, type);
        } else if (obj instanceof String json) {
            return mapper.readValue(json, type);
        } else if (obj instanceof Reader reader) {
            return mapper.readValue(reader, type);
        } else if (obj instanceof InputStream inputStream) {
            return mapper.readValue(inputStream, type);
        } else if (obj instanceof byte[] bytes) {
            return mapper.readValue(bytes, type);
        } else if (obj instanceof DataInput dataInput) {
            return mapper.readValue(dataInput, type);
        }
        return mapper.convertValue(obj, type);
    }

    /**
     * Read value t.
     *
     * @param <T>           the type parameter
     * @param obj           the obj
     * @param typeReference the type reference
     * @return the t
     */
    @SneakyThrows
    public <T> T readValue(Object obj, TypeReference<T> typeReference) {
        return readValue(obj, mapper.getTypeFactory().constructType(typeReference));
    }

    /**
     * json序列化
     *
     * @param obj obj
     * @return json string
     */
    @SneakyThrows
    public String writeValueAsString(Object obj) {
        if (obj instanceof String str) {
            return str;
        }
        return mapper.writeValueAsString(obj);
    }

    /**
     * Write value as bytes byte [ ].
     *
     * @param obj the obj
     * @return the byte [ ]
     */
    @SneakyThrows
    public byte[] writeValueAsBytes(Object obj) {
        if (obj instanceof String str) {
            return str.getBytes();
        }
        return mapper.writeValueAsBytes(obj);
    }

    /**
     * 将json转化成JsonNode
     *
     * @param obj the obj
     * @return the json node
     */
    @SneakyThrows
    public JsonNode readTree(Object obj) {
        if (obj instanceof JsonParser jsonParser) {
            return mapper.readTree(jsonParser);
        } else if (obj instanceof File file) {
            return mapper.readTree(file);
        } else if (obj instanceof URL url) {
            return mapper.readTree(url);
        } else if (obj instanceof String json) {
            return mapper.readTree(json);
        } else if (obj instanceof Reader reader) {
            return mapper.readTree(reader);
        } else if (obj instanceof InputStream inputStream) {
            return mapper.readTree(inputStream);
        } else if (obj instanceof byte[] bytes) {
            return mapper.readTree(bytes);
        }
        return null;
    }

    /**
     * Convert object.
     *
     * @param <T>       the type parameter
     * @param fromValue the  value
     * @param type      the type
     * @return the object
     */
    public <T> T convertValue(Object fromValue, Class<T> type) {
        return mapper.convertValue(fromValue, type);
    }

    /**
     * Convert value t.
     *
     * @param <T>           the type parameter
     * @param fromValue     the value
     * @param typeReference the type reference
     * @return the t
     */
    public <T> T convertValue(Object fromValue, TypeReference<T> typeReference) {
        return mapper.convertValue(fromValue, typeReference);
    }

    /**
     * Convert object.
     *
     * @param <T>       the type parameter
     * @param fromValue the  value
     * @param javaType  the java type
     * @return the object
     */
    public <T> T convertValue(Object fromValue, JavaType javaType) {
        return mapper.convertValue(fromValue, javaType);
    }
}
