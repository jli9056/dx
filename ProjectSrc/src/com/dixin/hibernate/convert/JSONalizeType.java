package com.dixin.hibernate.convert;

/**
 * Indicate jsonable type
 * 
 * @author Luo
 * 
 */
public enum JSONalizeType {
	/**
	 * Make default simple convert on simple data type, include string, number,
	 * date.
	 */
	Basic,
	/**
	 * Use jsonlib's default way
	 */
	Default,
	/**
	 * use to string method convert to string
	 */
	ToString,
	/**
	 * Get from one or more properties. Separated by comma(,).<BR>
	 * First property will be in the objects position and others will conver to
	 * a new property following a name convention: <BR>
	 * e.g. <BR>
	 * <code>
	 * @JSONable(type=JSONableType.Properties,properties="id,name")
	 * public Shop getShop(){...}
	 *  
	 * produce:
	 * 
	 * {
	 * ...
	 * "shop":"123245",
	 * shop_name:"NorthShop",
	 * ...
	 * }
	 * </code>
	 */
	Properties,
	/**
	 * Convert some properties. Like convert 1 to male, 0 to female.
	 */
	Converter;
}
