/**
 * 
 */
package com.dixin.action.correction;

import com.dixin.action.GenericAddAction;
import com.dixin.business.impl.CorrectionHelper;
import com.dixin.hibernate.Correction;

/**
 * Add Action For JDO Correction
 * 
 * @author Luo
 * 
 */
public class AddCorrectionAction extends GenericAddAction<Correction> {
	public AddCorrectionAction() {
		super(Correction.class, CorrectionHelper.class);
		uniqueKeys = new String[] { "", "id" };
	}

	protected void validate(Correction t) {
		t.setIsAuto(0);
	};
}
