package com.dixin;

/**
 * 
 * @author Luo
 * 
 */
public class DixinException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String field;

	public DixinException() {
	}

	public DixinException(String message) {
		super(message);

	}

	public DixinException(Throwable cause) {
		super(cause);

	}

	public DixinException(String message, Throwable cause) {
		super((cause instanceof DixinException) ? message + ": "
				+ cause.getMessage() : message, cause);

	}

	public DixinException(String field, String message, Throwable cause) {
		super((cause instanceof DixinException) ? message + ": "
				+ cause.getMessage() : message, cause);
		this.field = field;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

}
