package com.dixin.hibernate.convert.converters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dixin.hibernate.convert.Converter;

public class MaskNullDateConverter implements Converter {
	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	public Object convert(Object src) {
		Date d = (Date) src;
		if (d == null) {
			return "_";
		}
		String s = df.format(d);
		if (s.startsWith("3000")) {
			return "_";
		}
		return s;
	}

}
