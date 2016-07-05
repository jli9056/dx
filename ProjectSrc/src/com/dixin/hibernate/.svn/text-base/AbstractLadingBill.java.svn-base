/**
 * 
 */
package com.dixin.hibernate;

import java.util.Date;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.IAggregation;
import com.dixin.business.constants.Bool;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.JSONalizeType;
import com.dixin.hibernate.convert.Parse;
import com.dixin.hibernate.convert.ParseType;
import com.dixin.hibernate.convert.converters.DateLongFormatConverter;
import com.dixin.hibernate.convert.converters.IntegerToYesNo;
import com.dixin.hibernate.convert.converters.YesNoToInteger;

/**
 * @author Jason
 * 
 */
@Name("提货明细")
public abstract class AbstractLadingBill extends BaseJDO {

	private Integer id;
	private Factoryorderdetail forderdetail;
	private Integer quantity;
	private Date ladingDate;
	private String comment;
	private String orderNo;
	private String model;
	private Date lastUpdate;
	private Integer finished = Bool.FALSE;

	/**
	 * default contractor.
	 */
	public AbstractLadingBill() {

	}

	/**
	 * full contractor.
	 * 
	 * @param forderdetail
	 * @param quantity
	 */
	public AbstractLadingBill(Factoryorderdetail forderdetail,
			Integer quantity, Date ladingDate) {
		this.forderdetail = forderdetail;
		this.quantity = quantity;
		this.ladingDate = ladingDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.hibernate.BaseJDO#getId()
	 */
	@JSONalize
	public Integer getId() {
		return id;
	}

	@JSONalize(type = JSONalizeType.Properties, properties = "id")
	public Factoryorderdetail getForderdetail() {
		return forderdetail;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.FactoryorderdetailHelper.class, findFields = "id")
	public void setForderdetail(Factoryorderdetail forderdetail) {
		this.forderdetail = forderdetail;
	}
	@Aggregate(name = "产品数量", property = "quantity", type = IAggregation.SUM)
	@JSONalize
	public Integer getQuantity() {
		return quantity;
	}

	@Parse
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@JSONalize
	public Date getLadingDate() {
		return ladingDate;
	}

	@Parse
	public void setLadingDate(Date ladingDate) {
		this.ladingDate = ladingDate;
	}

	@Parse
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the comment
	 */
	@JSONalize
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	@Parse
	public void setComment(String comment) {
		this.comment = comment;
	}

	@JSONalize
	public String getOrderNo() {
		return orderNo;
	}

	@Parse
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@JSONalize
	public String getModel() {
		return model;
	}

	@Parse
	public void setModel(String model) {
		this.model = model;
	}

	@JSONalize(type = JSONalizeType.Converter, converter = DateLongFormatConverter.class)
	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date last_update) {
		this.lastUpdate = last_update;
	}

	@JSONalize(type = JSONalizeType.Converter, converter = IntegerToYesNo.class)
	public Integer getFinished() {
		return finished;
	}

	@Parse(type = ParseType.Converter, converter = YesNoToInteger.class)
	public void setFinished(Integer finished) {
		this.finished = finished;
	}

}
