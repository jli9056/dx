/**
 * 
 */
package com.dixin.action.refund;

import com.dixin.action.GenericAddAction;
import com.dixin.business.impl.RefundHelper;
import com.dixin.hibernate.Refund;

/**
 * @author Luo
 * 
 */
public class AddRefundAction extends GenericAddAction<Refund> {
	public AddRefundAction() {
		super(Refund.class, RefundHelper.class);
		uniqueKeys = new String[] { "", "id", "model", "alias", "barcode" };
	}

	protected void validate(Refund r) {
		log.debug("Refund: " + r.toJSONString());
	}
}
