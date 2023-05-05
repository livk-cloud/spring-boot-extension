package com.livk.commons.sqlparser;

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
    public List<String> parseTable(String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            TablesNamesFinder namesFinder = new TablesNamesFinder();
            return namesFinder.getTableList(statement);
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Gets params.
     *
     * @param sql the sql
     * @return the params
     */
    public List<String> getParams(String sql) {
        try {
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
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Format sql string.
     *
     * @param sql the sql
     * @return the string
     */
    public String formatSql(String sql) {
        try {
            return CCJSqlParserUtil.parse(sql).toString();
        } catch (JSQLParserException e) {
            return sql;
        }
    }
}
