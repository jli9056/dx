/**
 * 
 */
package com.dixin.action.correction;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.CorrectionHelper;
import com.dixin.hibernate.Correction;

/**
 * Search Action For JDO Correction
 * 
 * @author Luo
 * 
 */
public class SearchCorrectionAction extends GenericSearchAction<Correction> {
	public SearchCorrectionAction() {
		super(Correction.class, CorrectionHelper.class);
	}
}
