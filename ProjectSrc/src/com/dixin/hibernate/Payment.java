package com.dixin.hibernate;

import java.util.Date;

/**
 * Payment entity.
 * 
 * 
 */
public class Payment extends AbstractPayment implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -1222251484114606810L;

	/** default constructor */
	public Payment() {
	}

	/** minimal constructor */
	public Payment(Order order, Double amount, String method, Date payDate) {
		super(order, amount, method, payDate);
	}

	/** full constructor */
	public Payment(Order order, Double amount, String method, Date payDate,
			String comment, String customerName) {
		super(order, amount, method, payDate, comment, customerName);
	}

}
