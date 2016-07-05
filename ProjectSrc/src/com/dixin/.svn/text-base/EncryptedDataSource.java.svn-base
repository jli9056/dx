/**
 * 
 */
package com.dixin;

import java.io.IOException;

import org.apache.commons.dbcp.BasicDataSource;

import sun.misc.BASE64Decoder;

/**
 * @author Jason
 * 
 */
public class EncryptedDataSource extends BasicDataSource {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.dbcp.BasicDataSource#setPassword(java.lang.String)
	 */
	@Override
	public synchronized void setPassword(String password) {
		super.setPassword(decode(password));
	}


	/**
	 * 
	 * @return
	 */
	private String decode(String pwd) {
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] bytes = decoder.decodeBuffer(pwd);
			return new String(bytes);
		} catch (IOException e) {
			return "";
		}
	}
}
