package com.livk.context.mapstruct;

import com.livk.context.mapstruct.converter.Converter;

/**
 * @author livk
 */
public class LongConverter implements Converter<Long, String> {

	@Override
	public Long getSource(String s) {
		return Long.parseLong(s);
	}

	@Override
	public String getTarget(Long l) {
		return l.toString();
	}

}
