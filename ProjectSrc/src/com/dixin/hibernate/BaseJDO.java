/**
 * 
 */
package com.dixin.hibernate;

import java.io.Serializable;

import net.sf.json.JSONString;

import com.dixin.hibernate.convert.JSONalizer;

/**
 * @author Jason
 * 
 */
public abstract class BaseJDO implements Serializable, JSONString {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2230842791797907466L;

	public abstract Integer getId();

	/**
	 * 
	 */
	public BaseJDO() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.json.JSONString#toJSONString()
	 */
	public String toJSONString() {
		return JSONalizer.getInstance().toJSONString(this);
	}

	public String toString() {
		return this.getClass().getName() + "#" + this.getId();
	}
}
