/**
 * 
 */
package com.dixin.action.factoryorder;

import com.dixin.action.GenericLoadAction;
import com.dixin.business.impl.FactoryOrderHelper;
import com.dixin.hibernate.Factoryorder;

/**
 * Load Action For JDO Factoryorder
 * 
 * @author Luo
 * 
 */
public class LoadFactoryorderAction extends GenericLoadAction<Factoryorder> {
	public LoadFactoryorderAction() {
		super(Factoryorder.class, FactoryOrderHelper.class);
	}
}
