/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.mybatisplugins.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.github.pagehelper.Page;
import com.livk.commons.jackson.util.JsonNodeUtils;
import com.livk.commons.util.BeanLambda;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * 自定义分页信息
 *
 * @param <T> the type parameter
 * @author livk
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@JsonDeserialize(using = PageInfo.PageInfoJsonDeserializer.class)
public class PageInfo<T> implements Serializable {

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
	 * @param list list or page
	 */
	public PageInfo(List<T> list) {
		this(list, Function.identity());
	}

	/**
	 * 构建分页实体，同时使用{@link Function}转换list
	 * @param list the list
	 * @param function the function
	 * @param <R> r
	 */
	public <R> PageInfo(List<R> list, Function<List<R>, List<T>> function) {
		if (list instanceof Page<R> page) {
			this.list = function.apply(page.getResult().stream().toList());
			this.pageNum = page.getPageNum();
			this.pageSize = page.getPageSize();
			this.total = page.getTotal();
		}
		else {
			this.total = list.size();
			this.list = function.apply(list);
		}
	}

	/**
	 * 用于Jackson反序列化构建
	 * @param list the list
	 * @param pageNum the page num
	 * @param pageSize the page size
	 * @param total the total
	 */
	PageInfo(List<T> list, int pageNum, int pageSize, long total) {
		this.list = list;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.total = total;
	}

	/**
	 * {@link PageInfo} 反序列化器
	 */
	static class PageInfoJsonDeserializer extends StdScalarDeserializer<PageInfo<Object>>
			implements ContextualDeserializer {

		private JavaType javaType;

		/**
		 * PageInfoJsonDeserializer构造方法
		 */
		protected PageInfoJsonDeserializer() {
			super(PageInfo.class);
		}

		@Override
		public PageInfo<Object> deserialize(JsonParser p, DeserializationContext context) throws IOException {
			JsonNode jsonNode = context.readTree(p);
			ObjectMapper mapper = (ObjectMapper) p.getCodec();
			String listFieldName = BeanLambda.<PageInfo<Object>>fieldName(PageInfo::getList);
			CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, javaType);
			List<Object> list = JsonNodeUtils.findValue(jsonNode, listFieldName, collectionType, mapper);
			int pageNum = jsonNode.get(BeanLambda.<PageInfo<Object>>fieldName(PageInfo::getPageNum)).asInt();
			int pageSize = jsonNode.get(BeanLambda.<PageInfo<Object>>fieldName(PageInfo::getPageSize)).asInt();
			long total = jsonNode.get(BeanLambda.<PageInfo<Object>>fieldName(PageInfo::getTotal)).asLong();
			return new PageInfo<>(list, pageNum, pageSize, total);
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
