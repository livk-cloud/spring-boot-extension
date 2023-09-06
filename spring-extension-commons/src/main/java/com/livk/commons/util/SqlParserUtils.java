/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.commons.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.Collections;
import java.util.List;

/**
 * @author livk
 */
@UtilityClass
public class SqlParserUtils {

	/**
	 * Parse table list.
	 *
	 * @param sql the sql
	 * @return the list
	 */
	@SneakyThrows
	public List<String> parseTable(String sql) {
		Statement statement = CCJSqlParserUtil.parse(sql);
		TablesNamesFinder namesFinder = new TablesNamesFinder();
		return namesFinder.getTableList(statement);
	}

	/**
	 * Gets params.
	 *
	 * @param sql the sql
	 * @return the params
	 */
	@SneakyThrows
	public List<String> getParams(String sql) {
		Statement statement = CCJSqlParserUtil.parse(sql);
		if (statement instanceof Select select) {
			PlainSelect plain = (PlainSelect) select.getSelectBody();
			return plain.getSelectItems()
				.stream()
				.map(Object::toString)
				.toList();
		} else if (statement instanceof Update update) {
			return update.getUpdateSets()
				.stream()
				.flatMap(updateSet -> updateSet.getColumns().stream())
				.map(Column::getColumnName)
				.toList();
		} else if (statement instanceof Insert insert) {
			return insert.getColumns()
				.stream()
				.map(Column::getColumnName)
				.toList();
		}
		return Collections.emptyList();
	}

	/**
	 * Format sql string.
	 *
	 * @param sql the sql
	 * @return the string
	 */
	@SneakyThrows
	public String formatSql(String sql) {
		return CCJSqlParserUtil.parse(sql).toString();
	}
}
