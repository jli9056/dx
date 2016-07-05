/**
 * 
 */
package com.dixin.action.arrivement;

import com.dixin.action.GenericDeleteAction;
import com.dixin.business.impl.ArrivementHelper;
import com.dixin.hibernate.Arrivement;

/**
 * Delete Action For JDO Arrivement
 * 
 * @author Luo
 * 
 */
public class DeleteArrivementAction extends GenericDeleteAction<Arrivement> {
	public DeleteArrivementAction() {
		super(Arrivement.class, ArrivementHelper.class);
	}
}
