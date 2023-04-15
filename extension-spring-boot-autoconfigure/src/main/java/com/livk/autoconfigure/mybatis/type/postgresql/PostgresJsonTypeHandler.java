package com.livk.autoconfigure.mybatis.type.postgresql;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.jackson.JacksonUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>
 * 使用{@code rs.getObject()获取出来的类型是}{@link PGobject}
 * </p>
 * <p>
 * 使用{@code rs.getString()获取出来的类型是}{@link PGobject#getValue()}
 * </p>
 * <p>
 * 设置进入的的值必须为{@link PGobject},并且不能为{@code null}
 * </p>
 *
 * @author livk
 */
@MappedTypes(JsonNode.class)
public class PostgresJsonTypeHandler implements TypeHandler<JsonNode> {

    @Override
    public void setParameter(PreparedStatement ps, int i, JsonNode parameter, JdbcType jdbcType) throws SQLException {
        String json = parameter.toString();
        ps.setObject(i, PGJson.of(json));
    }

    @Override
    public JsonNode getResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return JacksonUtils.readTree(json);
    }

    @Override
    public JsonNode getResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return JacksonUtils.readTree(json);
    }

    @Override
    public JsonNode getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return JacksonUtils.readTree(json);
    }

}
