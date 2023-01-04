package com.livk.autoconfigure.mybatis.util;

import lombok.experimental.UtilityClass;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * SqlUtils
 * </p>
 *
 * @author livk
 * @date 2023/1/4
 */
@UtilityClass
public class SqlUtils {

    public List<String> parseTable(String sql) {
        try {
            var statement = CCJSqlParserUtil.parse(sql);
            var namesFinder = new TablesNamesFinder();
            return namesFinder.getTableList(statement);
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<String> getParams(String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(new StringReader(sql));
            if (statement instanceof Select select) {
                var plain = (PlainSelect) select.getSelectBody();
                return plain.getSelectItems().stream().map(Object::toString).collect(Collectors.toList());
            } else if (statement instanceof Update update) {
                return update.getUpdateSets().get(0).getColumns().stream().map(Column::getColumnName)
                        .collect(Collectors.toList());
            } else if (statement instanceof Insert insert) {
                return insert.getColumns().stream().map(Column::getColumnName).collect(Collectors.toList());
            }
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public String formatSql(String sql) {
        try {
            return CCJSqlParserUtil.parse(new StringReader(sql)).toString();
        } catch (JSQLParserException e) {
            return "";
        }
    }
}
