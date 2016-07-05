package com.dixin.hibernate.convert.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.dixin.hibernate.convert.Converter;

public class TimeToDateConverter implements Converter {
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public Object convert(Object src) {
		try {
			return df.parse("2000-01-01 " + src);
		} catch (ParseException e) {
			throw new RuntimeException("日期格式错误");
		}
	}
}
