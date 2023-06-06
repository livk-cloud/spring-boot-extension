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

package com.livk.autoconfigure.mybatis.type.postgresql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.livk.autoconfigure.mybatis.type.AbstractJsonTypeHandler;
import com.livk.commons.jackson.core.JacksonSupport;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.postgresql.util.PGobject;

import java.sql.PreparedStatement;
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
public class PostgresJsonTypeHandler extends AbstractJsonTypeHandler implements TypeHandler<JsonNode> {

    public PostgresJsonTypeHandler(ObjectMapper mapper) {
        super(JacksonSupport.create(mapper));
    }

    @Override
    protected void setParameter(PreparedStatement ps, int i, String json, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, PGJson.of(json));
    }
}
