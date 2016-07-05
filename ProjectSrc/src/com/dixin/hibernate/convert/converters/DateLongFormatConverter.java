package com.dixin.hibernate.convert.converters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dixin.hibernate.convert.Converter;

public class DateLongFormatConverter implements Converter {
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public Object convert(Object src) {
		if(src==null){
			return "";
		}
		return df.format((Date)src);
	}

}
