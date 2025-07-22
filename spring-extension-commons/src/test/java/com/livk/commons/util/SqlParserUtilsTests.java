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

package com.livk.commons.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class SqlParserUtilsTests {

	@Test
	void parseTable() {
		assertThat(SqlParserUtils.parseTable("select * from user")).containsExactly("user");

		assertThat(SqlParserUtils.parseTable("select * from user u left join account a on u.id = a.user_id"))
			.containsExactlyInAnyOrder("user", "account");

		assertThat(SqlParserUtils.parseTable("insert into user values (1,'root','root')")).containsExactly("user");

		assertThat(SqlParserUtils.parseTable("update user set username = 'root' where id = 1")).containsExactly("user");

		assertThat(SqlParserUtils.parseTable("delete from user where id = 1")).containsExactly("user");

		assertThatThrownBy(() -> SqlParserUtils.parseTable("")).isInstanceOf(NullPointerException.class);

		assertThatThrownBy(() -> SqlParserUtils.parseTable(null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void getParams() {
		assertThat(SqlParserUtils.getParams("select id,username,password from user")).containsExactly("id", "username",
				"password");

		assertThat(SqlParserUtils
			.getParams("select u.id,u.username,a.account_name from user u left join account a on u.id = a.user_id"))
			.containsExactly("u.id", "u.username", "a.account_name");

		assertThat(SqlParserUtils.getParams("insert into user (id,username,password) values (1,'root','root')"))
			.containsExactly("id", "username", "password");

		assertThat(SqlParserUtils.getParams("update user set username = 'root' where id = 1"))
			.containsExactly("username");
	}

	@Test
	void formatSql() {
		String sql = """
				SELECT *
				FROM user_account AS ua
				LEFT JOIN user_info AS ui ON ua.user_id = ui.id
				WHERE ui.id = 1""";

		String expected = "SELECT * FROM user_account AS ua LEFT JOIN user_info AS ui ON ua.user_id = ui.id WHERE ui.id = 1";

		assertThat(SqlParserUtils.formatSql(sql)).isEqualToNormalizingWhitespace(expected);
	}

}
