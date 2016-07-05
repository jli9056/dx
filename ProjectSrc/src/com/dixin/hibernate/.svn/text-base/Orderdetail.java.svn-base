package com.dixin.hibernate;

import java.util.Date;

import com.dixin.business.constants.Bool;

/**
 * Orderdetail entity.
 * 
 * 
 */
public class Orderdetail extends AbstractOrderdetail implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -2057378633926461862L;

	/** default constructor */
	public Orderdetail() {
	}

	/** minimal constructor */
	public Orderdetail(Product product, Order order, Integer quantity,
			Double price, Integer reservedCount, Integer deliveredCount,
			Date deliverDate) {
		super(product, order, quantity, price, reservedCount, deliveredCount,
				deliverDate);
	}

	/** full constructor */
	public Orderdetail(Product product, Order order, Integer quantity,
			Double price, Double cost, Integer reservedCount,
			Integer deliveredCount, Date deliverDate, Integer reserveLocked,
			Integer isValid, String comment, String customerName) {
		super(product, order, quantity, price, cost, reservedCount,
				deliveredCount, deliverDate, reserveLocked, isValid, comment,
				customerName);
	}

	/**
	 * clone basic properties.
	 */
	public Orderdetail clone() {
		Orderdetail od = new Orderdetail();
		od.setOrder(getOrder());
		od.setCost(getCost());
		od.setCustomerName(getCustomerName());
		od.setDeliverDate(getDeliverDate());
		od.setDeliveredCount(0);
		od.setReservedCount(0);
		od.setDoubleCheck(getDoubleCheck());
		od.setIsValid(Bool.FALSE);
		od.setModel(getModel());
		od.setPrice(getPrice());
		od.setProduct(getProduct());
		od.setQuantity(getQuantity());
		return od;
	}

}
