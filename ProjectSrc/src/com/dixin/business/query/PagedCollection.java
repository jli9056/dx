/**
 * 
 */
package com.dixin.business.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.dixin.business.IPagedResult;

/**
 * @author Jason
 * 
 */
public class PagedCollection<T> implements IPagedResult<T> {
	private List<T> list;

	/**
	 * 
	 * @param collection
	 */
	public PagedCollection(Collection<T> collection) {
		if (collection instanceof ArrayList) {
			list = (ArrayList<T>) collection;
		} else if (collection instanceof LinkedList) {
			list = (LinkedList<T>) collection;
		} else {
			list = new LinkedList<T>(collection);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IPagedResult#count()
	 */
	public int count() {
		return list.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IPagedResult#getResult(int, int)
	 */
	public List<T> getResult(int start, int limit) {
		if (start < 0)
			start = 0;
		int end = start + limit > list.size() ? list.size() : start + limit;
		return list.subList(start, end);
	}
}
