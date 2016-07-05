/**
 * 
 */
package com.dixin.action.ladingBill;

import com.dixin.action.GenericDeleteAction;
import com.dixin.business.impl.LadingBillHelper;
import com.dixin.hibernate.LadingBill;

/**
 * Delete Action For JDO LadingBill
 * 
 * @author Luo
 * 
 */
public class DeleteLadingBillAction extends GenericDeleteAction<LadingBill> {
	public DeleteLadingBillAction() {
		super(LadingBill.class, LadingBillHelper.class);
	}
}
