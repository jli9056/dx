/**
 * 
 */
package com.dixin.business.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dixin.business.DataException;
import com.dixin.business.IPagedResult;
import com.dixin.business.constants.OrderStatus;
import com.dixin.hibernate.Available;
import com.dixin.hibernate.Factoryorderdetail;
import com.dixin.hibernate.FdRefund;
import com.dixin.hibernate.Overflow;

/**
 * @author Jason
 * 
 */
public class FdRefundHelper extends BaseHelper<FdRefund> {

	private FactoryOrderHelper foHelper = new FactoryOrderHelper();

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
	 * @see com.dixin.business.impl.BaseHelper#save(com.dixin.hibernate.BaseJDO)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void save(FdRefund fr) {
		Factoryorderdetail fod = fr.getFactoryorderdetail();
		Integer refundQuantity = fr.getQuantity();
		if (refundQuantity > fod.getOwnedCount()) {
			throw new DataException("退货数量不能超过工厂欠货数量!");//即到货后的明细不能睡
		}
		if (refundQuantity <= 0) {
			throw new DataException("退货数量必须大于等于0!");
		}
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Integer fodQuantity = fod.getQuantity();
			fod.setQuantity(fodQuantity - refundQuantity);// 明细数量减少
			fod.setOwnedCount(fod.getOwnedCount() - refundQuantity);
			Integer availableCount = fod.getAvailableCount();
			//availableCount<=ownedCount always is true
			if (refundQuantity <= availableCount) {
				fod.setAvailableCount(availableCount - refundQuantity);//可用数量同步减少
			} else {
				int overflowCount = refundQuantity - availableCount;//需要把这么多客户关联转移到其他订单或者计划外订单
				fod.setAvailableCount(0);
				//首先查询其他工厂订单有没有指标,把这个明细需要删除的指标分配给他们
				//如果还不能满足条件的话就生成溢出数据
				List<Overflow>	overflows = Collections.synchronizedList(new ArrayList<Overflow>());
				IPagedResult<Available> results = new AvailableHelper()
						.findByProperty("factoryorderdetail.id", fod.getId());
				List<Available> list = results.getResult(0, results.count());
				for (Available a : list) {
					if (overflowCount <= 0) {
						break;
					}
					if (OrderStatus.FINISHED.equals(a.getOrderdetail()
							.getOrder().getStatus())) {
						continue;
					}
					int consumedCount = a.getConsumedCount();
					int delta = Math.min(overflowCount, consumedCount);
					overflowCount -= delta;//
					Overflow ov = new Overflow();
					ov.setOrderdetail(a.getOrderdetail());
					ov.setOverflowCount(delta);
					if (delta == consumedCount) {
						session.delete(a);
					} else {
						a.setConsumedCount(consumedCount - delta);
						session.merge(a);//modify old Available object which maps the relationship between fod and order details
					}
					overflows.add(ov);
				}
				for (Overflow ov : overflows) {
					session.save(ov);
				}
				Query fodQuery = session.createQuery("FROM Factoryorderdetail fod where fod.product.id=:pid AND fod.availableCount>0 AND fod.factoryorder.doubleCheck=1");
				fodQuery.setInteger("pid", fod.getProduct().getId());
				List fodList = fodQuery.list();
				for (Iterator i = fodList.iterator(); i.hasNext();) {
					Factoryorderdetail dd = (Factoryorderdetail) i.next();
					foHelper.consumeOverflow(session, dd, overflows);
				}
			}
			session.merge(fod);
			session.save(fr);
			tx.commit();
		} catch (Exception ex) {
			throw new DataException("保存退货记录过程中出现异常!", ex);
		}
	}

}
