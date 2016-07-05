/**
 * 
 */
package com.dixin.action.payment;

import com.dixin.action.GenericSaveAction;
import com.dixin.business.impl.PaymentHelper;
import com.dixin.hibernate.Payment;

/**
 * Save Action For JDO Payment
 * 
 * @author Luo
 * 
 */
public class SavePaymentAction extends GenericSaveAction<Payment> {
	public SavePaymentAction() {
		super(Payment.class, PaymentHelper.class);
		uniqueKeys = new String[] { "", "id", "order_no" };
	}
}
