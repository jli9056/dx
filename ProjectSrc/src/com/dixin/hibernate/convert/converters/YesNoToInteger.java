package com.dixin.hibernate.convert.converters;

import com.dixin.hibernate.convert.Converter;

public class YesNoToInteger implements Converter {

	public Object convert(Object src) {
		String s = "" + src;
		if ("是".equals(s) || "Yes".equalsIgnoreCase(s) || "1".equals(s)) {
			return 1;
		} else {
			return 0;
		}
	}

}
