/**
 * 
 */
package com.dixin.action.payment;

import com.dixin.action.ActionException;
import com.dixin.action.GenericAddAction;
import com.dixin.business.impl.PaymentHelper;
import com.dixin.hibernate.Payment;

/**
 * Add Action For JDO Payment
 * 
 * @author Luo
 * 
 */
public class AddPaymentAction extends GenericAddAction<Payment> {
	public AddPaymentAction() {
		super(Payment.class, PaymentHelper.class);
		uniqueKeys = new String[] { "", "id", "order_no" };
	}

	protected void validate(Payment t) {
		if (t.getAmount() <= 0) {
			throw new ActionException("付款金额不能小于等于零。");
		}
		double unpaid = t.getOrder().getRealTotal() - t.getOrder().getPaid();
		if (t.getAmount() > unpaid) {
			throw new ActionException("付款金额不能超出未付金额" + unpaid);
		}
	}
}
