package com.livk.autoconfigure.mybatis.support.postgresql;

import org.postgresql.util.PGobject;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.sql.SQLException;

/**
 * <p>
 * PGJson
 * </p>
 *
 * @author livk
 *
 */
class PGJson extends PGobject {

    public static final String TYPE_NAME = "json";

    private PGJson(String json) throws SQLException {
        super();
        setType(TYPE_NAME);
        setValue(json);
    }

    public PGJson(PGobject pGobject) throws SQLException {
        super();
        setType(TYPE_NAME);
        setValue(pGobject.getValue());
    }

    public static PGJson of(String json) throws SQLException {
        return StringUtils.hasText(json) ? new PGJson(json) : null;
    }

    public static PGJson of(PGobject pGobject) throws SQLException {
        return ObjectUtils.isEmpty(pGobject) ? null : new PGJson(pGobject);
    }

}
