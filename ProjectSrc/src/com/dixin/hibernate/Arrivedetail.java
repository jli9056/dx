package com.dixin.hibernate;

/**
 * Arrivedetail entity.
 * 
 *
 */
public class Arrivedetail extends AbstractArrivedetail implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 1925570903507864250L;

	/** default constructor */
	public Arrivedetail() {
	}

	/** full constructor */
	public Arrivedetail(Arrivement arrivement, Storehouse storehouse,
			Product product, Integer quantity, Double cost) {
		super(arrivement, storehouse, product, quantity, cost);
	}
}
