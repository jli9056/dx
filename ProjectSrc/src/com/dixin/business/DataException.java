/**
 * 
 */
package com.dixin.business;

import java.text.MessageFormat;

import com.dixin.DixinException;

/**
 * @author Jason
 * 
 */
public class DataException extends DixinException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9060428797150290223L;

	/**
	 * 
	 */
	public DataException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DataException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataException(String message, Object[] args, Throwable cause) {
		super(format(message, args), cause);
	}

	/**
	 * @param message
	 * @param args
	 * @return
	 */
	private static String format(String message, Object[] args) {
		return MessageFormat.format(message, args);
	}

	/**
	 * @param message
	 */
	public DataException(String message) {
		super(message);
	}

	/**
	 * @param message
	 */
	public DataException(String message, Object[] args) {
		super(format(message, args));
	}

	/**
	 * @param cause
	 */
	public DataException(Throwable cause) {
		super(cause);
	}

}
