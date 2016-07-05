package com.dixin.hibernate.convert;

import com.dixin.DixinException;

/**
 * 
 * @author Luo
 * 
 */
public class ConvertException extends DixinException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConvertException() {

	}

	public ConvertException(String message) {
		super(message);

	}

	public ConvertException(Throwable cause) {
		super(cause);

	}

	public ConvertException(String message, Throwable cause) {
		super(message, cause);

	}

}
