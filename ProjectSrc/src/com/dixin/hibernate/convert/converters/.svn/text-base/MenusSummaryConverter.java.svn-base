package com.dixin.hibernate.convert.converters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.dixin.hibernate.AbstractMenu;
import com.dixin.hibernate.Menu;
import com.dixin.hibernate.convert.Converter;

/**
 * Convert full set of all menus to a sorted list contains only main menus.
 * 
 * @author Luo
 * 
 */
public class MenusSummaryConverter implements Converter {

	public Object convert(Object src) {
		if (src instanceof Collection) {

			@SuppressWarnings("unchecked")
			Collection<Menu> c = (Collection<Menu>) src;
			List<Menu> mainMenus = new ArrayList<Menu>(c.size());
			for (Menu m : c) {
				if (m.getParent() == null) {
					mainMenus.add(m);
				}
			}
			Collections.sort(mainMenus, new Comparator<AbstractMenu>() {
				public int compare(AbstractMenu o1, AbstractMenu o2) {
					return o1.getId() - o2.getId();
				}
			});
			return mainMenus.toString();
		}
		return src;
	}
}
