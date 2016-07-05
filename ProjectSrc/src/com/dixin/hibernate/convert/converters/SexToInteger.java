package com.dixin.hibernate.convert.converters;

import com.dixin.hibernate.convert.Converter;

public class SexToInteger implements Converter {

	public Object convert(Object src) {
		if (src == null)
			return null;
		if (src.toString().equals("男")) {
			return 1;
		}
		if (src.toString().equals("女")) {
			return 0;
		}
		return null;
	}

}
