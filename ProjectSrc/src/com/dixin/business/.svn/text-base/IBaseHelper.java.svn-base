/**
 * 
 */
package com.dixin.business;

import com.dixin.hibernate.BaseJDO;

/**
 * @author Jason
 * 
 */
public interface IBaseHelper<T extends BaseJDO> {
	/**
	 * 
	 * @param id
	 * @return
	 */
	public T findById(Integer id);

	/**
	 * 
	 * @param queryDefn
	 * @return
	 */
	public IPagedResult<T> find(IQueryDefn queryDefn);

	/**
	 * 
	 * @return
	 */
	public IPagedResult<T> findAll();

	/**
	 * 
	 * @param detachedInstance
	 */
	public T merge(T detachedInstance);

	/**
	 * 
	 * @param transientInstance
	 */
	public void save(T transientInstance);

	/**
	 * 
	 * @param id
	 */
	public abstract boolean deleteById(Integer id);
	
	public boolean restoreById(Integer id);
}
