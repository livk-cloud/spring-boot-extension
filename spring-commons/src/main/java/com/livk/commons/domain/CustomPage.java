package com.livk.commons.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.github.pagehelper.Page;
import com.livk.commons.jackson.JsonNodeUtils;
import com.livk.commons.util.ReflectionUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * LivkPage
 *
 * @param <T> the type parameter
 * @author livk
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@JsonDeserialize(using = CustomPage.CustomPageJsonDeserializer.class)
public class CustomPage<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 总数
     */
    private final long total;
    /**
     * 当前分页数据
     */
    private final List<T> list;
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 数量
     */
    private int pageSize;

    /**
     * {@link Page}
     *
     * @param list list or page
     */
    public CustomPage(List<T> list) {
        this(list, Function.identity());
    }

    /**
     * 构建分页实体，同时使用{@link Function}转换list
     *
     * @param list     the list
     * @param function the function
     * @param <R>      the r
     */
    public <R> CustomPage(List<R> list, Function<List<R>, List<T>> function) {
        if (list instanceof Page<R> page) {
            this.list = function.apply(page.getResult().stream().toList());
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
        } else {
            this.total = list.size();
            this.list = function.apply(list);
        }
    }

    CustomPage(List<T> list, int pageNum, int pageSize, long total) {
        this.list = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
    }

    static class CustomPageJsonDeserializer extends StdScalarDeserializer<CustomPage<Object>> implements ContextualDeserializer {

        private JavaType javaType;

        /**
         * Instantiates a new Pair json deserializer.
         */
        protected CustomPageJsonDeserializer() {
            super(CustomPage.class);
        }

        @Override
        public CustomPage<Object> deserialize(JsonParser p, DeserializationContext context) throws IOException {
            JsonNode jsonNode = context.readTree(p);
            ObjectMapper mapper = (ObjectMapper) p.getCodec();
            String listFieldName = ReflectionUtils.<CustomPage<Object>>getFieldName(CustomPage::getList);
            List<Object> list = JsonNodeUtils.findValue(jsonNode, listFieldName, javaType, mapper);
            int pageNum = jsonNode.get(ReflectionUtils.<CustomPage<Object>>getFieldName(CustomPage::getPageNum)).asInt();
            int pageSize = jsonNode.get(ReflectionUtils.<CustomPage<Object>>getFieldName(CustomPage::getPageSize)).asInt();
            long total = jsonNode.get(ReflectionUtils.<CustomPage<Object>>getFieldName(CustomPage::getTotal)).asLong();
            return new CustomPage<>(list, pageNum, pageSize, total);
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty property) {
            JavaType contextualType = context.getContextualType();
            TypeBindings bindings = contextualType.getBindings();
            javaType = bindings.getBoundType(0);
            return this;
        }
    }
}
