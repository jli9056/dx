/**
 * 
 */
package com.dixin.action.arrangement;

import com.dixin.action.GenericDeleteAction;
import com.dixin.business.impl.ArrangementHelper;
import com.dixin.hibernate.Arrangement;

/**
 * Delete Action For JDO Arrangement
 * 
 * @author Luo
 * 
 */
public class DeleteArrangementAction extends GenericDeleteAction<Arrangement> {
	public DeleteArrangementAction() {
		super(Arrangement.class, ArrangementHelper.class);
	}
}
