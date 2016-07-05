package com.dixin.hibernate;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.IAggregation;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.JSONalizeType;
import com.dixin.hibernate.convert.Parse;
import com.dixin.hibernate.convert.ParseType;
import com.dixin.hibernate.convert.converters.IntegerToYesNo;
import com.dixin.hibernate.convert.converters.MaskNullDateConverter;
import com.dixin.hibernate.convert.converters.YesNoToInteger;

/**
 * AbstractOrder entity provides the base persistence definition of the Order
 * entity.
 * 
 * 
 */
@Name("客户订单")
public abstract class AbstractOrder extends BaseJDO implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4933930376786556526L;
	private Integer id;
	private Customer customer;
	private Shop shop;
	private String orderNo;
	private String address;
	private Date orderDate;
	private Double total;// 总计金额
	private Double realTotal;// 实收金额
	private String status;
	private String comment;
	private String customerName;
	private Date deliverDate;
	private Integer isPaid = new Integer(0);
	private Integer isValid = new Integer(1);
	private Set deliverydetails = new HashSet(0);
	private Set orderdetails = new HashSet(0);
	private Set payments = new HashSet(0);
	private Set refunds = new HashSet(0);
	private int doubleCheck;
	private String checker;

	// Constructors

	/** default constructor */
	public AbstractOrder() {
	}

	/** minimal constructor */
	public AbstractOrder(Customer customer, Shop shop, String orderNo,
			String address, Date orderDate, Double realTotal) {
		this.customer = customer;
		this.shop = shop;
		this.orderNo = orderNo;
		this.address = address;
		this.orderDate = orderDate;
		this.realTotal = realTotal;
	}

	/** full constructor */
	public AbstractOrder(Customer customer, Shop shop, String orderNo,
			String address, Date orderDate, Double total, Double realTotal,
			String status, Integer isPaid, Integer isValid, String comment,
			String customerName, Date deliverDate, Set deliverydetails,
			Set orderdetails, Set payments, Set refunds) {
		this.customer = customer;
		this.shop = shop;
		this.orderNo = orderNo;
		this.address = address;
		this.orderDate = orderDate;
		this.total = total;
		this.realTotal = realTotal;
		this.status = status;
		this.isPaid = isPaid;
		this.isValid = isValid;
		this.comment = comment;
		this.deliverDate = deliverDate;
		this.deliverydetails = deliverydetails;
		this.orderdetails = orderdetails;
		this.payments = payments;
		this.refunds = refunds;
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

	@JSONalize(type = JSONalizeType.Properties, properties = "id,name,phone")
	public Customer getCustomer() {
		return this.customer;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.CustomerHelper.class, findFields = "id")
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@JSONalize(type = JSONalizeType.Properties, properties = "name")
	public Shop getShop() {
		return this.shop;
	}

	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.ShopHelper.class, findFields = "name")
	public void setShop(Shop shop) {
		this.shop = shop;
	}

	@JSONalize
	public String getOrderNo() {
		return this.orderNo;
	}

	@Parse
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@JSONalize
	public String getAddress() {
		return this.address;
	}

	@Parse
	public void setAddress(String address) {
		this.address = address;
	}

	@JSONalize(type = JSONalizeType.Basic)
	public Date getOrderDate() {
		return this.orderDate;
	}

	@Parse
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @return the total
	 */
	// @Aggregate(name = "订单金额总和", property = "total", type = IAggregation.SUM)
	@JSONalize
	public Double getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	@Parse
	public void setTotal(Double total) {
		this.total = total;
	}

	@Aggregate(name = "订单金额", property = "realTotal", type = IAggregation.SUM)
	@JSONalize
	public Double getRealTotal() {
		return this.realTotal;
	}

	@Parse
	public void setRealTotal(Double realTotal) {
		this.realTotal = realTotal;
	}

	@JSONalize
	public String getStatus() {
		return this.status;
	}

	@Parse
	public void setStatus(String status) {
		this.status = status;
	}

	@JSONalize
	public String getComment() {
		return this.comment;
	}

	@Parse
	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set getDeliverydetails() {
		return this.deliverydetails;
	}

	public void setDeliverydetails(Set deliverydetails) {
		this.deliverydetails = deliverydetails;
	}

	@JSONalize(type = JSONalizeType.Default)
	public Set getOrderdetails() {
		return this.orderdetails;
	}

	public void setOrderdetails(Set orderdetails) {
		this.orderdetails = orderdetails;
	}

	public Set getPayments() {
		return this.payments;
	}

	@JSONalize
	public Double getPaid() {
		Set<Payment> pms = this.getPayments();
		double paid = 0.0;
		if (pms != null) {
			for (Payment p : pms) {
				paid += p.getAmount();
			}
		}
		return paid;
	}

	public void setPayments(Set payments) {
		this.payments = payments;
	}

	public Set getRefunds() {
		return this.refunds;
	}

	public void setRefunds(Set refunds) {
		this.refunds = refunds;
	}

	@JSONalize(type = JSONalizeType.Converter, converter = IntegerToYesNo.class)
	public Integer getIsPaid() {
		return isPaid;
	}

	@Parse(type = ParseType.Converter, converter = YesNoToInteger.class)
	public void setIsPaid(Integer isPaid) {
		this.isPaid = isPaid;
	}

	@JSONalize(type = JSONalizeType.Converter, converter = MaskNullDateConverter.class)
	public Date getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	@JSONalize
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the isValid
	 */
	@JSONalize
	public Integer getIsValid() {
		return isValid;
	}

	/**
	 * @param isValid
	 *            the isValid to set
	 */
	@Parse
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	@JSONalize(type = JSONalizeType.Converter, converter = IntegerToYesNo.class)
	public int getDoubleCheck() {
		return doubleCheck;
	}

	@Parse(type = ParseType.Converter, converter = YesNoToInteger.class)
	public void setDoubleCheck(int doubleCheck) {
		this.doubleCheck = doubleCheck;
	}

	/**
	 * @return the checker
	 */
	@JSONalize
	public String getChecker() {
		return checker;
	}

	/**
	 * @param checker the checker to set
	 */
	@Parse
	public void setChecker(String checker) {
		this.checker = checker;
	}
}
