package com.dixin.business.impl;

import com.dixin.business.DataException;
import com.dixin.hibernate.Orderdetail;

public class OrderdetailHelper extends BaseHelper<Orderdetail> {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#merge(com.dixin.hibernate.BaseJDO)
	 */
	public Orderdetail merge(Orderdetail detachedInstance) {
		// throw new DataException("不支持的操作");
		// for order detail page
		return super.merge(detachedInstance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#save(com.dixin.hibernate.BaseJDO)
	 */
	public void save(Orderdetail transientInstance) {
		throw new DataException("不支持的操作");
	}

	public void saveLockReserve(Integer id) {
		Orderdetail d = this.findById(id);
		d.setReserveLocked(1);
		super.save(d);
	}

	public void saveUnlockReserve(Integer id) {
		Orderdetail d = this.findById(id);
		d.setReserveLocked(0);
		super.save(d);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#deleteById(java.lang.Integer)
	 */
	public boolean deleteById(Integer id) {
		throw new DataException("不支持的操作");
	}
}
