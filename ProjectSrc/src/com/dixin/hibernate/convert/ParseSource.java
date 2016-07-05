package com.dixin.hibernate.convert;

import java.util.Collection;

/**
 * A data source contains the name-value pair for parser to parse from.
 * 
 * @author Luo
 * 
 */
public interface ParseSource {
	/**
	 * get all names
	 * 
	 * @return
	 */
	public Collection<String> getNames();

	/**
	 * Get value of the specified name.
	 * 
	 * @param name
	 * @return
	 */
	public String getValue(String name);

	/**
	 * Get value of the specified name.
	 * 
	 * @param name
	 * @return
	 */
	public String[] getValues(String name);

	/**
	 * check if contains a name.
	 * 
	 * @param name
	 * @return
	 */
	public boolean containsName(String name);
}
