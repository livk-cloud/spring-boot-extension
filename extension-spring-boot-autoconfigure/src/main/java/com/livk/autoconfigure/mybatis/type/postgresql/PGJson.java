package com.livk.autoconfigure.mybatis.type.postgresql;

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
