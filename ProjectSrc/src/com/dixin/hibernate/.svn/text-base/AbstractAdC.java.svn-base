package com.dixin.hibernate;

/**
 * AbstractAdC entity provides the base persistence definition of the AdC
 * entity.
 * 
 * 
 */
public abstract class AbstractAdC extends BaseJDO implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1949834642194250531L;
	private Integer id;
	private Arrivedetail arrivedetail;
	private Correction correction;
	private int delta;

	// Constructors

	/** default constructor */
	public AbstractAdC() {
	}

	/** full constructor */
	public AbstractAdC(Arrivedetail arrivedetail, Correction correction,
			int delta) {
		this.arrivedetail = arrivedetail;
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
	 * @return the arrivedetail
	 */
	public Arrivedetail getArrivedetail() {
		return arrivedetail;
	}

	/**
	 * @param arrivedetail
	 *            the arrivedetail to set
	 */
	public void setArrivedetail(Arrivedetail arrivedetail) {
		this.arrivedetail = arrivedetail;
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
