package com.dixin.hibernate.convert.converters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dixin.hibernate.convert.Converter;

public class DateToTimeConverter implements Converter {
	DateFormat df = new SimpleDateFormat("HH:mm");

	public Object convert(Object src) {
		Date d = (Date) src;
		return df.format(d);
	}

}
