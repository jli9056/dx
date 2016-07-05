package com.dixin.action.util;

import java.io.UnsupportedEncodingException;

public class CodingUtil {
	private static final String UTF_8 = "utf-8";
	private static final String TARGET_ENCODING = "iso-8859-1";

	private CodingUtil() {
	}

	public static String decode(String s) {
		if(s==null) return null;
		try {
			return new String(s.getBytes(TARGET_ENCODING), UTF_8);
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

}
