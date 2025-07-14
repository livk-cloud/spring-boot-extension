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

package com.livk.context.mybatis.type.mysql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livk.commons.jackson.support.JacksonSupport;
import com.livk.context.mybatis.type.AbstractJsonTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <p>
 * JsonTypeHandler
 * </p>
 *
 * @author livk
 */
@MappedTypes(JsonNode.class)
public class MysqlJsonTypeHandler extends AbstractJsonTypeHandler implements TypeHandler<JsonNode> {

	/**
	 * Instantiates a new Mysql json type handler.
	 * @param mapper the mapper
	 */
	public MysqlJsonTypeHandler(ObjectMapper mapper) {
		super(new JacksonSupport(mapper));
	}

	@Override
	protected void setParameter(PreparedStatement ps, int i, String json, JdbcType jdbcType) throws SQLException {
		ps.setString(i, json);
	}

}
