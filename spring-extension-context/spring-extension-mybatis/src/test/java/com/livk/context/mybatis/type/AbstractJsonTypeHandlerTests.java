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

import com.livk.commons.jackson.support.JacksonSupport;
import org.apache.ibatis.type.JdbcType;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author livk
 */
class AbstractJsonTypeHandlerTests {

	@Test
	void setParameterWritesJsonString() throws SQLException {
		AtomicReference<String> written = new AtomicReference<>();
		AbstractJsonTypeHandler handler = new AbstractJsonTypeHandler(new JacksonSupport(new JsonMapper())) {
			@Override
			protected void setParameter(PreparedStatement ps, int i, String json, JdbcType jdbcType) {
				written.set(json);
			}
		};
		PreparedStatement ps = mock(PreparedStatement.class);
		JsonNode node = new JsonMapper().readTree("{\"x\":1}");

		handler.setParameter(ps, 1, node, JdbcType.VARCHAR);

		assertThat(written.get()).isEqualTo("{\"x\":1}");
	}

	@Test
	void getResultByColumnNameAndIndexAndCallable() throws SQLException {
		AbstractJsonTypeHandler handler = new AbstractJsonTypeHandler(JsonMapper.builder()) {
			@Override
			protected void setParameter(PreparedStatement ps, int i, String json, JdbcType jdbcType) {
			}
		};
		ResultSet rs = mock(ResultSet.class);
		CallableStatement cs = mock(CallableStatement.class);
		when(rs.getString("json")).thenReturn("{\"a\":1}");
		when(rs.getString(2)).thenReturn("{\"b\":2}");
		when(cs.getString(3)).thenReturn("{\"c\":3}");

		assertThat(handler.getResult(rs, "json").get("a").asInt()).isEqualTo(1);
		assertThat(handler.getResult(rs, 2).get("b").asInt()).isEqualTo(2);
		assertThat(handler.getResult(cs, 3).get("c").asInt()).isEqualTo(3);
		verify(rs).getString("json");
		verify(rs).getString(2);
		verify(cs).getString(3);
	}

}
