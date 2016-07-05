package com.dixin.hibernate;

import java.util.Date;

/**
 * Arrangement entity.
 * 
 * 
 */
public class Arrangement extends AbstractArrangement implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 7562843554622997410L;

	/** default constructor */
	public Arrangement() {
	}

	/** minimal constructor */
	public Arrangement(Orderdetail order, Integer quantity,
			Date queueTime) {
		super(order, quantity, queueTime);
	}

	/** full constructor */
	public Arrangement(Product product, Orderdetail order, Integer quantity,
			Date queueTime, Integer isFinished, String comment, String orderNo,
			String customerName, Date orderDate, String shopName,
			Date deliverDate) {
		super(order, quantity, queueTime, isFinished, comment,product, 
				orderNo, customerName, orderDate, shopName, deliverDate);
	}

}
