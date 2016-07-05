/**
 * 
 */
package com.dixin.action.correction;

import com.dixin.action.GenericLoadAction;
import com.dixin.business.impl.CorrectionHelper;
import com.dixin.hibernate.Correction;

/**
 * Load Action For JDO Correction
 * 
 * @author Luo
 * 
 */
public class LoadCorrectionAction extends GenericLoadAction<Correction> {
	public LoadCorrectionAction() {
		super(Correction.class, CorrectionHelper.class);
	}
}
