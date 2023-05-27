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

import com.livk.commons.util.ObjectUtils;
import org.postgresql.util.PGobject;
import org.springframework.util.StringUtils;

import java.sql.SQLException;

/**
 * <p>
 * PGJson
 * </p>
 *
 * @author livk
 */
class PGJson extends PGobject {

    /**
     * The constant TYPE_NAME.
     */
    public static final String TYPE_NAME = "json";

    private PGJson(String json) throws SQLException {
        super();
        setType(TYPE_NAME);
        setValue(json);
    }

    /**
     * Instantiates a new Pg json.
     *
     * @param pGobject the p gobject
     * @throws SQLException the sql exception
     */
    public PGJson(PGobject pGobject) throws SQLException {
        super();
        setType(TYPE_NAME);
        setValue(pGobject.getValue());
    }

    /**
     * Of pg json.
     *
     * @param json the json
     * @return the pg json
     * @throws SQLException the sql exception
     */
    public static PGJson of(String json) throws SQLException {
        return StringUtils.hasText(json) ? new PGJson(json) : null;
    }

    /**
     * Of pg json.
     *
     * @param pGobject the p gobject
     * @return the pg json
     * @throws SQLException the sql exception
     */
    public static PGJson of(PGobject pGobject) throws SQLException {
        return ObjectUtils.isEmpty(pGobject) ? null : new PGJson(pGobject);
    }

}
