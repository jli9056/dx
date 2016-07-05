package com.dixin.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampUtil {
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat tf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static DateFormat stf = new SimpleDateFormat("HH:mm");

	private TimestampUtil() {
	}

	/**
	 * 
	 * @param date
	 * @param time
	 * @return
	 */
	public static Date combine(Date date, Date time) {
		try {
			return tf.parse(df.format(date) + ' ' + stf.format(time));
		} catch (ParseException e) {
		}
		return date;
	}
}