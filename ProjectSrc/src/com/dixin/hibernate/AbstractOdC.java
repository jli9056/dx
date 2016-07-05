package com.dixin.hibernate;

/**
 * AbstractAdC entity provides the base persistence definition of the AdC
 * entity.
 * 
 * 
 */
public abstract class AbstractOdC extends BaseJDO implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1949834642194250531L;
	private Integer id;
	private Orderdetail orderdetail;
	private Correction correction;
	private int delta;

	// Constructors

	/** default constructor */
	public AbstractOdC() {
	}

	/** full constructor */
	public AbstractOdC(Orderdetail orderdetail, Correction correction, int delta) {
		this.orderdetail = orderdetail;
		this.correction = correction;
		this.delta = delta;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the orderdetail
	 */
	public Orderdetail getOrderdetail() {
		return orderdetail;
	}

	/**
	 * @param orderdetail
	 *            the orderdetail to set
	 */
	public void setOrderdetail(Orderdetail orderdetail) {
		this.orderdetail = orderdetail;
	}

	/**
	 * @return the correction
	 */
	public Correction getCorrection() {
		return correction;
	}

	/**
	 * @param correction
	 *            the correction to set
	 */
	public void setCorrection(Correction correction) {
		this.correction = correction;
	}

	/**
	 * @return the delta
	 */
	public int getDelta() {
		return delta;
	}

	/**
	 * @param delta
	 *            the delta to set
	 */
	public void setDelta(int delta) {
		this.delta = delta;
	}

}
