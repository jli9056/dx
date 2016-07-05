package com.dixin.business.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dixin.business.DataException;
import com.dixin.hibernate.Factoryorderdetail;

public class FactoryorderdetailHelper extends BaseHelper<Factoryorderdetail> {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#merge(com.dixin.hibernate.BaseJDO)
	 */
	public Factoryorderdetail merge(Factoryorderdetail detachedInstance) {
		//for update pickDate and comment
		return super.merge(detachedInstance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#save(com.dixin.hibernate.BaseJDO)
	 */
	public void save(Factoryorderdetail transientInstance) {
		throw new DataException("不支持的操作");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#deleteById(java.lang.Integer)
	 */
	public boolean deleteById(Integer id) {
		throw new DataException("不支持的操作");
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void suggestLaddingCount(List<Factoryorderdetail> fodList, Date start, Date end) {
		Session session = super.getSession();
		Transaction tx = session.beginTransaction();
		try {
			Query q = session
					.createQuery("select od.product.id, SUM(od.quantity-od.deliveredCount-od.reservedCount) from Orderdetail od where od.isValid=1 and od.doubleCheck=1 and od.deliverDate>:start and od.deliverDate<:end group by od.product.id");
			q.setParameter("start", start);
			q.setParameter("end", end);
			List result = q.list();
			if (result.isEmpty()) {
				for (Factoryorderdetail fod : fodList) {
					fod.setSuggestLaddingCount(0);
				}
			} else {
				for (Iterator i = result.iterator(); i.hasNext();) {
					Object[] value = (Object[]) i.next();
					if (value[1] == null) {
						continue;
					}
					Integer pid = (Integer) value[0];
					Integer cnt = ((Long) value[1]).intValue();
					for (Iterator j = fodList.iterator(); cnt > 0
							&& j.hasNext();) {
						Factoryorderdetail fod = (Factoryorderdetail) j.next();
						if (fod.getProduct().getId().equals(pid)) {
							int min = Math.min(cnt, fod.getOwnedCount());
							if (min == cnt) {
								fod.setSuggestLaddingCount(cnt);
								cnt = 0;
							} else {
								fod.setSuggestLaddingCount(fod.getOwnedCount());
								cnt -= fod.getOwnedCount();
							}
						}
					}
				}
			}
			tx.commit();
		} catch (Exception e) {
			throw new DataException("", e);
		}
	}
}
