package com.dixin.hibernate;

/**
 * Overflow entity.
 * 
 */
public class Overflow extends AbstractOverflow implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5609957123987732362L;

	// Constructors

	/** default constructor */
	public Overflow() {
	}

	/** minimal constructor */
	public Overflow(Orderdetail orderdetail) {
		super(orderdetail);
	}

	/** minimal constructor */
	public Overflow(Orderdetail orderdetail, Integer count) {
		super(orderdetail, count);
	}

	/** full constructor */
	public Overflow(Orderdetail orderdetail, Integer overflowCount,
			String orderNo, Integer customerId, String customerName,
			String productModel) {
		super(orderdetail, overflowCount, orderNo, customerId, customerName,
				productModel);
	}

}
