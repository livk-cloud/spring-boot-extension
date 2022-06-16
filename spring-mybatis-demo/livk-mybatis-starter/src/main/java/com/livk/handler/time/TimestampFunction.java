package com.livk.handler.time;

import com.livk.handler.FunctionHandle;

/**
 * <p>
 * TimestampFunction
 * </p>
 *
 * @author livk
 * @date 2022/3/28
 */
public class TimestampFunction implements FunctionHandle<Long> {

	@Override
	public Long handler() {
		return System.currentTimeMillis();
	}

}
