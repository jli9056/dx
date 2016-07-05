/**
 * 
 */
package com.dixin.action.menu;

import com.dixin.action.GenericSearchAction;
import com.dixin.business.impl.MenuHelper;
import com.dixin.hibernate.Menu;

/**
 * @author Luo
 * 
 */
public class SearchMenuAction extends GenericSearchAction<Menu> {
	public SearchMenuAction() {
		super(Menu.class, MenuHelper.class);
	}
}
