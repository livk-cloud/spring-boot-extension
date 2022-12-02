package com.livk.commons.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.*;

/**
 * The type Jackson util.
 */
@UtilityClass
public class JacksonUtils {

    /**
     * The constant JSON_EMPTY.
     */
    public static final String JSON_EMPTY = "{}";

    private static final ObjectMapper MAPPER = JsonMapper.builder().build();

    /**
     * json字符转Bean
     *
     * @param json  json string
     * @param clazz class
     * @param <T>   type
     * @return T
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

    @SneakyThrows
    public static <T> T toBean(InputStream inputStream, Class<T> clazz) {
        return (inputStream == null || clazz == null) ? null :
                MAPPER.readValue(inputStream, clazz);
    }

    /**
     * 序列化
     *
     * @param obj obj
     * @return json
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
     * @param json  json数组
     * @param clazz 类型
     * @param <T>   泛型
     * @return List<T>
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
     * @param json       json字符串
     * @param keyClass   K Class
     * @param valueClass V Class
     * @return Map<K, V>
     */
    @SneakyThrows
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (check(json, keyClass, valueClass)) {
            return Collections.emptyMap();
        }
        MapType mapType = MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
        return MAPPER.readValue(json, mapType);
    }

    @SneakyThrows
    public Properties toProperties(InputStream inputStream) {
        if (inputStream == null) {
            return new Properties();
        }
        JavaType javaType = MAPPER.getTypeFactory().constructType(Properties.class);
        return MAPPER.readValue(inputStream, javaType);
    }

    @SneakyThrows
    public <T> T toBean(String json, TypeReference<T> typeReference) {
        return check(json, typeReference) ? null :
                MAPPER.readValue(json, typeReference);
    }

    @SneakyThrows
    public JsonNode readTree(String json) {
        return MAPPER.readTree(json);
    }

    /**
     * 获取json字符串的第一个子节点 从json串中自顶向下依次查找第一个出现的节点
     *
     * @param jsonNode json
     * @param nodeName node
     * @return str node first
     * @example { "c": "1", "a": "2", "b": {"c": 3} } getNodeFirst(json,"c")==>1
     * <p>
     * { "c": "1", "a": "2", "b": {"c": 3,"d":4}, "d": 5 } getNodeFirst(json,"d")==>4
     */
    public JsonNode findNodeFirst(JsonNode jsonNode, String nodeName) {
        if (!StringUtils.hasText(nodeName)) {
            return null;
        }
        if (jsonNode.isArray()) {
            Iterator<JsonNode> elements = jsonNode.elements();
            while (elements.hasNext()) {
                JsonNode result = findNodeFirst(elements.next(), nodeName);
                if (result != null) {
                    return result;
                }
            }
        } else {
            Iterator<String> iterator = jsonNode.fieldNames();
            while (iterator.hasNext()) {
                String node = iterator.next();
                if (node.equals(nodeName)) {
                    return jsonNode.get(nodeName);
                } else {
                    JsonNode child = jsonNode.get(node);
                    if (child.isContainerNode()) {
                        JsonNode result = findNodeFirst(child, nodeName);
                        if (result != null) {
                            return result;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取所有节点为nodeName的数据
     *
     * @param jsonNode json
     * @param nodeName name
     * @return list
     */
    public List<JsonNode> findNodeAll(JsonNode jsonNode, String nodeName) {
        if (!StringUtils.hasText(nodeName)) {
            return Collections.emptyList();
        }
        List<JsonNode> jsonNodeList = new ArrayList<>();
        if (jsonNode.isArray()) {
            Iterator<JsonNode> elements = jsonNode.elements();
            while (elements.hasNext()) {
                List<JsonNode> firstAll = findNodeAll(elements.next(), nodeName);
                jsonNodeList.addAll(firstAll);
            }
        } else {
            Iterator<String> iterator = jsonNode.fieldNames();
            while (iterator.hasNext()) {
                String node = iterator.next();
                if (node.equals(nodeName)) {
                    jsonNodeList.add(jsonNode.get(nodeName));
                } else {
                    JsonNode child = jsonNode.get(node);
                    if (child.isContainerNode()) {
                        List<JsonNode> firstAll = findNodeAll(child, nodeName);
                        jsonNodeList.addAll(firstAll);
                    }
                }
            }
        }
        return jsonNodeList;
    }

    /**
     * 获取json字符串的节点
     *
     * @param jsonNode json
     * @param nodePath node(节点之间以.隔开)
     * @return node node
     * @example { "c": "1", "a": "2", "b": { "c": 3, "d": { "ab": 6 } } } getNode(json,
     * "b"))==>{"c":3,"d":{"ab":6}} getNode(json, "b.c"))==>3 getNode(json,
     * "b.d"))==>{"ab":6} getNode(json, "b.d.ab"))==>6 getNode(json, "d"))==>null
     */
    public JsonNode findNode(JsonNode jsonNode, String nodePath) {
        if (!StringUtils.hasText(nodePath)) {
            return null;
        }
        int index = nodePath.indexOf(".");
        if (jsonNode.isArray()) {
            int range = Integer.parseInt(nodePath.substring(0, index));
            Iterator<JsonNode> elements = jsonNode.elements();
            JsonNode node = StreamUtils.of(elements).toList().get(range);
            return findNode(node, nodePath.substring(index + 1));
        } else {
            Iterator<String> iterator = jsonNode.fieldNames();
            while (iterator.hasNext()) {
                String node = iterator.next();
                if (index <= 0) {
                    if (node.equals(nodePath)) {
                        return jsonNode.get(nodePath);
                    }
                } else {
                    String parentNode = nodePath.substring(0, index);
                    if (node.equals(parentNode)) {
                        JsonNode child = jsonNode.get(node);
                        if (child.isContainerNode()) {
                            String childNode = nodePath.substring(index + 1);
                            JsonNode result = findNode(child, childNode);
                            if (result != null) {
                                return result;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean check(String json, Object... checkObj) {
        return json == null || json.isEmpty() || ObjectUtils.anyChecked(Objects::isNull, checkObj);
    }
}
