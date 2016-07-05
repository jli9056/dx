package com.dixin.hibernate.convert.converters;

import com.dixin.hibernate.convert.Converter;

public class BooleanToInt implements Converter {

	public Object convert(Object src) {
		Boolean b = Boolean.valueOf(src + "");
		return Integer.valueOf(b ? 1 : 0);
	}

}
