package com.dixin.hibernate.convert.converters;

import com.dixin.hibernate.convert.Converter;

public class IntegerToYesNo implements Converter {

	public Object convert(Object src) {
		if (src != null) {
			if (src.equals(1)) {
				return "是";
			}
		}
		return "否";
	}

}
