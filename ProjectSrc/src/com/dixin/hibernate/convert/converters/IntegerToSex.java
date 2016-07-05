package com.dixin.hibernate.convert.converters;

import com.dixin.hibernate.convert.Converter;

public class IntegerToSex implements Converter {

	public Object convert(Object src) {
		if (src.equals(1)) {
			return "男";
		}
		if (src.equals(0)) {
			return "女";
		}
		return null;
	}

}
