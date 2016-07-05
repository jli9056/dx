package com.dixin.hibernate.convert.converters;

import com.dixin.hibernate.convert.Converter;

public class IntToBoolean implements Converter {

	public Object convert(Object src) {
		Integer i = (Integer) src;
		return (i != null && i != 0);
	}

}
