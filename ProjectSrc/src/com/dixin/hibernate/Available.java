package com.dixin.hibernate;

/**
 * Available entity.
 * 
 */
public class Available extends AbstractAvailable implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 4567618294762100091L;

	/** default constructor */
	public Available() {
	}

	/** minimal constructor */
	public Available(Factoryorderdetail factoryorderdetail,
			Orderdetail orderdetail) {
		super(factoryorderdetail, orderdetail);
	}

	/** full constructor */
	public Available(Factoryorderdetail factoryorderdetail,
			Orderdetail orderdetail, Integer availableCount) {
		super(factoryorderdetail, orderdetail, availableCount);
	}

}
