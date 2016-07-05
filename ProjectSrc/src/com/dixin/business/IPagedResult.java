package com.dixin.business;

import java.util.List;

/**
 * Support paging search.<BR>
 * 
 * @author Luo
 * 
 * @param <T>
 *            The type to search.
 */
public interface IPagedResult<T> {
	/**
	 * 
	 * @return
	 */
	public int count();

	/**
	 * 
	 * @param start
	 *            start is 0-based
	 * @param limit
	 * @return
	 */
	public List<T> getResult(int start, int limit);
}
