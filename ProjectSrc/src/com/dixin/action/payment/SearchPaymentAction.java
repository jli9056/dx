/**
 * 
 */
package com.dixin.action.payment;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.PaymentHelper;
import com.dixin.hibernate.Payment;

/**
 * Search Action For JDO Payment
 * 
 * @author Luo
 * 
 */
public class SearchPaymentAction extends GenericSearchAction<Payment> {
	public SearchPaymentAction() {
		super(Payment.class, PaymentHelper.class);
	}
}
