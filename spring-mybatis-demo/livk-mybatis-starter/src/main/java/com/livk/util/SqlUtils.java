package com.livk.util;

import lombok.experimental.UtilityClass;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * SqlUtils
 * </p>
 *
 * @author livk
 * @date 2022/3/25
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
}
