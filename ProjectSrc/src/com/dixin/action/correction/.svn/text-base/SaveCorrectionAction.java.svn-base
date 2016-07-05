/**
 * 
 */
package com.dixin.action.correction;

import com.dixin.action.GenericSaveAction;
import com.dixin.business.impl.CorrectionHelper;
import com.dixin.hibernate.Correction;

/**
 * Save Action For JDO Correction
 * 
 * @author Luo
 * 
 */
public class SaveCorrectionAction extends GenericSaveAction<Correction> {
	public SaveCorrectionAction() {
		super(Correction.class, CorrectionHelper.class);
		uniqueKeys = new String[] { "", "id", "order_no" };
	}
}
