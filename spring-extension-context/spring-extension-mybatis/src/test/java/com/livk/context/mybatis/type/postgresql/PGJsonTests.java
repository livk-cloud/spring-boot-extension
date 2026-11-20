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

package com.livk.context.mybatis.type.postgresql;

import org.junit.jupiter.api.Test;
import org.postgresql.util.PGobject;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class PGJsonTests {

	@Test
	void ofStringReturnsNullWhenBlank() throws SQLException {
		assertThat(PGJson.of((String) null)).isNull();
		assertThat(PGJson.of("")).isNull();
		assertThat(PGJson.of("   ")).isNull();
	}

	@Test
	void ofStringSetsTypeAndValue() throws SQLException {
		PGJson json = PGJson.of("{\"a\":1}");
		assertThat(json).isNotNull();
		assertThat(json.getType()).isEqualTo(PGJson.TYPE_NAME);
		assertThat(json.getValue()).isEqualTo("{\"a\":1}");
	}

	@Test
	void ofPgObjectReturnsNullWhenEmpty() throws SQLException {
		assertThat(PGJson.of((PGobject) null)).isNull();
	}

	@Test
	void ofPgObjectCopiesValueWithJsonType() throws SQLException {
		PGobject source = new PGobject();
		source.setType("text");
		source.setValue("{\"b\":2}");

		PGJson json = PGJson.of(source);
		assertThat(json).isNotNull();
		assertThat(json.getType()).isEqualTo(PGJson.TYPE_NAME);
		assertThat(json.getValue()).isEqualTo("{\"b\":2}");
	}

}
