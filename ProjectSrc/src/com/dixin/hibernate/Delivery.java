package com.dixin.hibernate;

import java.util.Date;
import java.util.Set;

/**
 * Delivery entity.
 * 
 *
 */
public class Delivery extends AbstractDelivery implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -676366848496678785L;

	/** default constructor */
	public Delivery() {
	}

	/** minimal constructor */
	public Delivery(Employee employee, Date deliverDate) {
		super(employee, deliverDate);
	}

	/** full constructor */
	public Delivery(Employee employee, Date deliverDate, String comment,
			Set deliverydetails) {
		super(employee, deliverDate, comment, deliverydetails);
	}

}
