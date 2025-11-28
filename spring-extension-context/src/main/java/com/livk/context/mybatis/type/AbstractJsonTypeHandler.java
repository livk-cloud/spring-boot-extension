/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.mybatis.type;

import com.livk.commons.jackson.support.JacksonOps;
import com.livk.commons.jackson.support.JacksonSupport;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.MapperBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Abstract json type handler.
 *
 * @author livk
 */
@RequiredArgsConstructor
public abstract class AbstractJsonTypeHandler implements TypeHandler<JsonNode> {

	/**
	 * The Jackson operations.
	 */
	protected final JacksonOps jacksonOps;

	/**
	 * Instantiates a new Abstract json type handler.
	 * @param builder the builder
	 */
	protected AbstractJsonTypeHandler(MapperBuilder<?, ?> builder) {
		ObjectMapper mapper = builder.build();
		jacksonOps = new JacksonSupport(mapper);
	}

	@Override
	public final void setParameter(PreparedStatement ps, int i, JsonNode parameter, JdbcType jdbcType)
			throws SQLException {
		String json = parameter.toString();
		setParameter(ps, i, json, jdbcType);
	}

	/**
	 * Sets parameter.
	 * @param ps the ps
	 * @param i the
	 * @param json the json
	 * @param jdbcType the jdbc type
	 * @throws SQLException the sql exception
	 */
	protected abstract void setParameter(PreparedStatement ps, int i, String json, JdbcType jdbcType)
			throws SQLException;

	@Override
	public final JsonNode getResult(ResultSet rs, String columnName) throws SQLException {
		String json = rs.getString(columnName);
		return jacksonOps.readTree(json);
	}

	@Override
	public final JsonNode getResult(ResultSet rs, int columnIndex) throws SQLException {
		String json = rs.getString(columnIndex);
		return jacksonOps.readTree(json);
	}

	@Override
	public final JsonNode getResult(CallableStatement cs, int columnIndex) throws SQLException {
		String json = cs.getString(columnIndex);
		return jacksonOps.readTree(json);
	}

}
