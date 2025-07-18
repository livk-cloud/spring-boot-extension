/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.commons.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

/**
 * @author livk
 */
class SqlParserUtilsTest {

	@Test
	void parseTable() {
		assertEquals(Set.of("user"), SqlParserUtils.parseTable("select * from user"));
		assertEquals(Set.of("user", "account"),
				SqlParserUtils.parseTable("select * from user u left join account a on u.id = a.user_id"));
		assertEquals(Set.of("user"), SqlParserUtils.parseTable("insert into user values (1,'root','root')"));
		assertEquals(Set.of("user"), SqlParserUtils.parseTable("update user set username = 'root' where id = 1"));
		assertEquals(Set.of("user"), SqlParserUtils.parseTable("delete from user where id = 1"));
	}

	@Test
	void getParams() {
		assertLinesMatch(List.of("id", "username", "password"),
				SqlParserUtils.getParams("select id,username,password from user"));
		assertLinesMatch(List.of("u.id", "u.username", "a.account_name"), SqlParserUtils
			.getParams("select u.id,u.username,a.account_name from user u left join account a on u.id = a.user_id"));
		assertLinesMatch(List.of("id", "username", "password"),
				SqlParserUtils.getParams("insert into user (id,username,password) values (1,'root','root')"));
		assertLinesMatch(List.of("username"),
				SqlParserUtils.getParams("update user set username = 'root' where id = 1"));
	}

	@Test
	void formatSql() {
		String sql = """
				SELECT *
				FROM user_account AS ua
				LEFT JOIN user_info AS ui ON ua.user_id = ui.id
				WHERE ui.id = 1""";
		String formatSQL = "SELECT * FROM user_account AS ua LEFT JOIN user_info AS ui ON ua.user_id = ui.id WHERE ui.id = 1";
		assertEquals(formatSQL, SqlParserUtils.formatSql(sql));
	}

}
