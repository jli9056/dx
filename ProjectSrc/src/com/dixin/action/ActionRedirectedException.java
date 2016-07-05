package com.dixin.action;

public class ActionRedirectedException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ActionRedirectedException() {
		
	}

	public ActionRedirectedException(String message) {
		super(message);
		
	}

	public ActionRedirectedException(Throwable cause) {
		super(cause);
		
	}

	public ActionRedirectedException(String message, Throwable cause) {
		super(message, cause);
		
	}

}
