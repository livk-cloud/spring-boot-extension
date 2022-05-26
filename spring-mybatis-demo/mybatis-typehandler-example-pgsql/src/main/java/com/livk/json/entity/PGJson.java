package com.livk.json.entity;

import org.postgresql.util.PGobject;
import org.springframework.util.StringUtils;

import java.sql.SQLException;

/**
 * <p>
 * PGJson
 * </p>
 *
 * @author livk
 * @date 2022/5/26
 */
public class PGJson extends PGobject {

    public static final String TYPE_NAME = "json";

    private PGJson(String json) throws SQLException {
        super();
        setType(TYPE_NAME);
        setValue(json);
    }

    public static PGJson of(String json) throws SQLException {
        return StringUtils.hasText(json) ? new PGJson(json) : null;
    }
}
