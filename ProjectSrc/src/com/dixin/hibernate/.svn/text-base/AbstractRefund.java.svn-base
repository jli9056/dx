package com.dixin.hibernate;

import java.util.Date;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.IAggregation;
import com.dixin.business.impl.DeliverydetailHelper;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.JSONalizeType;
import com.dixin.hibernate.convert.Parse;
import com.dixin.hibernate.convert.ParseType;
import com.dixin.hibernate.convert.converters.IntegerToYesNo;
import com.dixin.hibernate.convert.converters.YesNoToInteger;

/**
 * AbstractRefund entity provides the base persistence definition of the Refund
 * entity.
 * 
 */
@Name("退换记录")
public abstract class AbstractRefund extends BaseJDO implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private Deliverydetail deliverydetail;
	private Integer quantity;
	private Integer refundedCount = 0;
	private String method;
	private Integer finished;
	private Date refundDate;

	// Constructors

	/** default constructor */
	public AbstractRefund() {
	}

	/** minimal constructor */
	public AbstractRefund(Deliverydetail deliverydetail, Integer quantity,
			Integer finished, Date refundDate) {
		this.deliverydetail = deliverydetail;
		this.quantity = quantity;
		this.finished = finished;
		this.refundDate = refundDate;
	}

	/** full constructor */
	public AbstractRefund(Deliverydetail deliverydetail, Integer quantity,
			String method, Integer finished, Date refundDate) {
		this.deliverydetail = deliverydetail;
		this.quantity = quantity;
		this.method = method;
		this.finished = finished;
		this.refundDate = refundDate;
	}

	// Property accessors
	@JSONalize
	public Integer getId() {
		return this.id;
	}

	@Parse
	public void setId(Integer id) {
		this.id = id;
	}

	@JSONalize
	public String getProduct() {
		if (this.getDeliverydetail() != null) {
			return this.getDeliverydetail().getProduct().getModel();
		}
		return null;
	}

	@JSONalize(type = JSONalizeType.Properties, properties = "id")
	public Deliverydetail getDeliverydetail() {
		return this.deliverydetail;
	}

	@JSONalize
	public String getOrderNo() {
		Deliverydetail dd = this.getDeliverydetail();
		if (dd != null) {
			return dd.getOrder().getOrderNo();
		} else {
			return null;
		}
	}

	@Parse(type = ParseType.Helper, helper = DeliverydetailHelper.class, findFields = "id")
	public void setDeliverydetail(Deliverydetail deliverydetail) {
		this.deliverydetail = deliverydetail;
	}
	@Aggregate(name = "产品数量",property="quantity", type = IAggregation.SUM)
	@JSONalize
	public Integer getQuantity() {
		return this.quantity;
	}

	@Parse
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	@Aggregate(name = "修复数量", property = "refundedCount", type = IAggregation.SUM)
	@JSONalize
	public Integer getRefundedCount() {
		return this.refundedCount;
	}

	@Parse
	public void setRefundedCount(Integer refundedCount) {
		this.refundedCount = refundedCount;
	}

	@JSONalize
	public String getMethod() {
		return this.method;
	}

	@Parse
	public void setMethod(String method) {
		this.method = method;
	}

	@JSONalize(type = JSONalizeType.Converter, converter = IntegerToYesNo.class)
	public Integer getFinished() {
		return this.finished;
	}

	@Parse(type = ParseType.Converter, converter = YesNoToInteger.class)
	public void setFinished(Integer finished) {
		this.finished = finished;
	}

	@JSONalize(type = JSONalizeType.Basic)
	public Date getRefundDate() {
		return this.refundDate;
	}

	@Parse
	public void setRefundDate(Date refundDate) {
		this.refundDate = refundDate;
	}

}