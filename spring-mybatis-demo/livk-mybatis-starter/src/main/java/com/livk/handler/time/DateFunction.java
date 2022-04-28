package com.livk.handler.time;

import com.livk.handler.FunctionHandle;

import java.util.Date;

/**
 * <p>
 * DateFaction
 * </p>
 *
 * @author livk
 * @date 2022/2/9
 */
public class DateFunction implements FunctionHandle<Date> {

	@Override
	public Date handler() {
		return new Date();
	}

}
