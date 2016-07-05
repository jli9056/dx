/**
 * 
 */
package com.dixin.business.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dixin.business.DataException;
import com.dixin.business.constants.Bool;
import com.dixin.hibernate.Arrangement;
import com.dixin.hibernate.Delivery;
import com.dixin.hibernate.Deliverydetail;
import com.dixin.hibernate.Employee;
import com.dixin.hibernate.Orderdetail;
import com.dixin.util.TimestampUtil;

/**
 * @author Jason
 * 
 */
public class ArrangementHelper extends BaseHelper<Arrangement> {

	/**
	 * 确认送货：根据排货安排生成送货信息。
	 */
	public void confirmDelivery(Employee employee, int[] arrangementIds,
			Date deliveryDate, String comment) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Delivery delivery = new Delivery(employee, deliveryDate);
			int[] ids = unique(arrangementIds);
			// 查找所定义的排货信息
			Arrangement[] pais = new Arrangement[ids.length];
			Deliverydetail[] details = new Deliverydetail[ids.length];
			Set<Deliverydetail> set = new HashSet<Deliverydetail>();
			for (int i = 0; i < pais.length; i++) {
				pais[i] = super.findById(ids[i]);
				Orderdetail orderdetail = pais[i].getOrderdetail();
				pais[i].setQueueTime(TimestampUtil.combine(deliveryDate,
						pais[i].getQueueTime()));
				details[i] = new Deliverydetail(delivery, pais[i].getProduct(),
						orderdetail.getOrder(), pais[i].getQuantity(), pais[i]
								.getQueueTime());
				details[i].setCost(orderdetail.getCost());
				set.add(details[i]);
			}
			delivery.setDeliverydetails(set);
			// 保存送货信息
			new DeliveryHelper().saveDelivery(delivery, session);
			// 排货信息状态设置为已完成
			for (int i = 0; i < pais.length; i++) {
				pais[i].setIsFinished(Bool.TRUE);
				session.update(pais[i]);
			}
			tx.commit();
		} catch (Exception e) {
			if (e instanceof DataException)
				throw (DataException) e;
			else
				throw new DataException("未知错误：也许您输入了错误的数据，请检查！", e);
		}
	}

	/**
	 * 
	 * @param array
	 * @return
	 */
	private int[] unique(int[] array) {
		int[] ids = new int[array.length];
		int index = 0;
		for (int i = 0; i < array.length; i++) {
			boolean isUnique = true;
			for (int j = 0; j < i; j++) {
				if (i != j && array[j] == array[i]) {
					isUnique = false;
					break;
				}
			}
			if (isUnique)
				ids[index++] = array[i];
		}
		int[] result = new int[index];
		System.arraycopy(ids, 0, result, 0, index);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.impl.BaseHelper#deleteById(java.lang.Integer)
	 */
	@Override
	public boolean deleteById(Integer id) {
		return super.deleteById(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.impl.BaseHelper#merge(com.dixin.hibernate.BaseJDO)
	 */
	@Override
	public Arrangement merge(Arrangement detachedInstance) {
		checkQuantity(detachedInstance);
		return super.merge(detachedInstance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.impl.BaseHelper#save(com.dixin.hibernate.BaseJDO)
	 */
	@Override
	public void save(Arrangement transientInstance) {
		checkQuantity(transientInstance);
		super.save(transientInstance);
	}

	/**
	 * @param arrangement
	 * @throws DataException
	 */
	private void checkQuantity(Arrangement arrangement) throws DataException {
		Orderdetail od = arrangement.getOrderdetail();
		if (od.getReservedCount() <= 0) {
			throw new DataException("没有留货，不能排货！");
		}
		/*IPagedResult<Arrangement> result = this.findByProperty("orderdetail",
				od);
		int count = result.count();
		if (count > 1) {
			throw new DataException("该明细已经有多个排货，请删除多个排货，并重新排货");
		} else if (count > 0
				&& (arrangement.getId() == null || arrangement.getId() == 0)) {
			throw new DataException("该明细已经有排货，请删除已有排货，并重新排货");
		}*/
		if (od.getReservedCount() < arrangement.getQuantity())
			throw new DataException("排货数量不能大于订单已留货数量！");
	}
}
