package com.livk.context.mapstruct;

import com.livk.context.mapstruct.converter.Converter;

/**
 * @author livk
 */
public class IntConverter implements Converter<Integer, String> {

	@Override
	public Integer getSource(String s) {
		return Integer.parseInt(s);
	}

	@Override
	public String getTarget(Integer i) {
		return i.toString();
	}

}
