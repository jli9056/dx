package com.dixin.hibernate;

import java.util.Date;
import java.util.Set;

/**
 * Order entity.
 * 
 * 
 */
public class Order extends AbstractOrder implements java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 2088284506897320721L;

	/** default constructor */
	public Order() {
	}

	/** minimal constructor */
	public Order(Customer customer, Shop shop, String orderNo, String address,
			Date orderDate, Double realTotal) {
		super(customer, shop, orderNo, address, orderDate, realTotal);
	}

	/** full constructor */
	public Order(Customer customer, Shop shop, String orderNo, String address,
			Date orderDate, Double total, Double realTotal, String status, Integer isPaid,
			Integer isValid, String comment, String customerName,
			Date deliverDate, Set deliverydetails, Set orderdetails,
			Set payments, Set refunds) {
		super(customer, shop, orderNo, address, orderDate, total, realTotal, status,
				isPaid, isValid, comment, customerName, deliverDate,
				deliverydetails, orderdetails, payments, refunds);
	}

}
