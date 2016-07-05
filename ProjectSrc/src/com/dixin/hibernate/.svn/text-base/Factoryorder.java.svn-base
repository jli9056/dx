package com.dixin.hibernate;

import java.util.Date;
import java.util.Set;

/**
 * Factoryorder entity.
 * 
 *
 */
public class Factoryorder extends AbstractFactoryorder implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 8334045850627725600L;

	/** default constructor */
	public Factoryorder() {
	}

	/** minimal constructor */
	public Factoryorder(Employee employee, String orderNo, Date orderDate,
			Integer isplan) {
		super(employee, orderNo, orderDate, isplan);
	}

	/** full constructor */
	public Factoryorder(Employee employee, String orderNo, Date orderDate,Double total,
			Integer isplan, Integer finished, String comment,
			Set factoryorderdetails, Set arrivements) {
		super(employee, orderNo, orderDate, total, isplan, finished, comment,
				factoryorderdetails, arrivements);
	}

}
