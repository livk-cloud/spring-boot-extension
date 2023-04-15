package com.livk.autoconfigure.mybatis.inject.handler.time;

import com.livk.autoconfigure.mybatis.inject.handler.FunctionHandle;

import java.util.Date;

/**
 * <p>
 * DateFaction
 * </p>
 *
 * @author livk
 */
public class DateFunction implements FunctionHandle<Date> {

    @Override
    public Date handler() {
        return new Date();
    }

}
