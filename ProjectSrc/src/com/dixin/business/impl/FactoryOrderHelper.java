/**
 * 
 */
package com.dixin.business.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dixin.business.DataException;
import com.dixin.business.StatManager;
import com.dixin.business.constants.Bool;
import com.dixin.hibernate.Available;
import com.dixin.hibernate.Factoryorder;
import com.dixin.hibernate.Factoryorderdetail;
import com.dixin.hibernate.Orderdetail;
import com.dixin.hibernate.Overflow;
import com.dixin.hibernate.Product;

/**
 * @author Jason
 * 
 */
public class FactoryOrderHelper extends BaseHelper<Factoryorder> {
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.impl.BaseHelper#deleteById(java.lang.Integer)
	 */
	@Override
	public boolean deleteById(Integer id) {
		log.info("删除工厂订单... ID=" + id);
		Factoryorder forder = findById(id);
		if (forder == null) {
			log.info("无法找到相应的工厂订单, 删除失败! ID=" + id);
			return false;
		}
		if (forder.getFinished() == Bool.TRUE) {
			throw new DataException("工厂订单已完成, 不能删除!");
		}
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Set<Factoryorderdetail> forderdetails = forder.getFactoryorderdetails();
			for (Factoryorderdetail fod : forderdetails) {
				deleteDetail(session, fod, forder.getDoubleCheck());
			}
			session.delete(forder);
			tx.commit();
			log.info("删除工厂订单成功! ID=" + id);
			return true;
		} catch (Exception e) {
			tx.rollback();
			log.info("删除工厂订单过程中出现异常, 事务回滚!", e);
			return false;
		}
	}

	/**
	 * @param session
	 * @param fod
	 * @param doubleCheck
	 */
	@SuppressWarnings("unchecked")
	private void deleteDetail(Session session, Factoryorderdetail fod,
			int doubleCheck) {
		if (doubleCheck == Bool.FALSE) {
			log.info("simply delete factory order detail! id="+fod.getId());
			session.delete(fod);
			return;
		}
		log.info("删除工厂订单明细... ID=" + fod.getId());
		if (fod.getOwnedCount() < fod.getQuantity()) {// 该订单已有部分到货,不能删除
			log.error("该订单已有商品到货,工厂订单明细不能被删除!");
			throw new DataException("该订单已有商品到货,工厂订单明细不能被删除!");
		}
		//
		Query query = session
				.createQuery("from Available a where a.factoryorderdetail.id=:fodId");
		query.setParameter("fodId", fod.getId());
		for (Iterator j = query.iterate(); j.hasNext();) {
			Available available = (Available) j.next();
			Orderdetail od = available.getOrderdetail();
			Overflow ovf = findOverflow(session, od.getId());
			if (ovf == null) {
				ovf = new Overflow(od, available.getConsumedCount());
				session.save(ovf);
			} else {
				ovf.setOverflowCount(ovf.getOverflowCount()
						+ available.getConsumedCount());
				session.merge(ovf);
			}
			session.delete(available);
		}
		session.delete(fod);
		log.info("删除工厂订单明细成功! ID=" + fod.getId());
		updateStatView(session, fod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.impl.BaseHelper#merge(com.dixin.hibernate.BaseJDO)
	 */
	@Override
	public Factoryorder merge(Factoryorder detachedInstance) {
		if (detachedInstance.getFinished() == Bool.TRUE) {
			log.error("工厂订单已完成, 不能再编辑!");
			throw new DataException("工厂订单已完成, 不能再编辑!");
		}
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Factoryorder result = modifyFactoryorder(session, detachedInstance);
			tx.commit();
			return result;
		} catch (Exception e) {
			tx.rollback();
			log.info("rollback:" + e);
			throw new DataException("编辑工厂订单出错! 订单号:{0}",
					new Object[] { detachedInstance.getOrderNo() }, e);
		}
	}

	/**
	 * @param session
	 * @param detachedInstance
	 * @return
	 */
	private Factoryorder modifyFactoryorder(Session session,
			Factoryorder detachedInstance) {
		final Factoryorder oldForder = this.findById(detachedInstance.getId());
		final int doubleCheck = detachedInstance.getDoubleCheck();
		int updateRelatedData = doubleCheck-oldForder.getDoubleCheck();
		oldForder.setDoubleCheck(doubleCheck);//just update the double check field to make StatManager work correctly
		
		Set<Factoryorderdetail> oldDetails = oldForder.getFactoryorderdetails();
		Set<Factoryorderdetail> detachedDetails = detachedInstance
				.getFactoryorderdetails();
		for (Factoryorderdetail fod : oldDetails) {
			if (needDelete(fod, detachedDetails)) {
				deleteDetail(session, fod, doubleCheck);
			}
		}
		for (Factoryorderdetail fod : detachedDetails) {
			saveDetail(session, fod, doubleCheck, updateRelatedData);
		}
		Factoryorder result = (Factoryorder) session.merge(detachedInstance);
		// change factory order status
		Set<Factoryorderdetail> details = result.getFactoryorderdetails();
		int ownedCount = 0;
		for (Factoryorderdetail fod : details) {
			ownedCount += fod.getOwnedCount();
		}
		if (ownedCount == 0) {
			result.setFinished(Bool.TRUE);
		} else if (ownedCount > 0) {
			result.setFinished(Bool.FALSE);
		}
		result = (Factoryorder) session.merge(result);
		return result;
	}

	/**
	 * @param fod
	 */
	private void setDefaultValues(Factoryorderdetail fod) {
		Integer quantity = fod.getQuantity();
		fod.setAvailableCount(quantity);
		fod.setDeliveredCount(0);
		fod.setOwnedCount(quantity);
		fod.setUnladingCount(quantity);
		fod.setLadingCount(0);
	}

	/**
	 * 
	 * @param fod
	 * @param Set
	 *            <Factoryorderdetail> details
	 * @return
	 */
	private boolean needDelete(Factoryorderdetail fod,
			Set<Factoryorderdetail> detachedDetails) {
		for (Factoryorderdetail dd : detachedDetails) {
			if (fod.getId().equals(dd.getId())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * generate a factoryorder instance from the given order and overflows.
	 * 
	 * @param orderOnly
	 * @return
	 */
	public Factoryorder genFactoryorderFromOverflow(Factoryorder orderOnly) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Set<Factoryorderdetail> fodSet = new HashSet<Factoryorderdetail>();
			List<Overflow> overflows = findAllOverflows(session);
			Map<Product, Integer> productQuantityMap = new HashMap<Product, Integer>();
			for (Overflow ovf : overflows) {
				Product product = ovf.getOrderdetail().getProduct();
				Integer quantity = ovf.getOverflowCount();
				Integer oldquantity = productQuantityMap.get(product);
				if (oldquantity == null) {
					productQuantityMap.put(product, quantity);
				} else {
					productQuantityMap.put(product, oldquantity + quantity);
				}
			}
			// generate factory order details
			for (Product p : productQuantityMap.keySet()) {
				Integer quality = productQuantityMap.get(p);
				Factoryorderdetail fod = new Factoryorderdetail(orderOnly, p,
						quality, p.getCost(), 0, 0);
				fodSet.add(fod);
			}
			orderOnly.setFactoryorderdetails(fodSet);
			tx.commit();
			return orderOnly;
		} catch (Exception e) {
			tx.rollback();
			log.info("rollback:" + e);
		}
		return orderOnly;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.impl.BaseHelper#save(com.dixin.hibernate.BaseJDO)
	 */
	@Override
	public void save(Factoryorder forder) {
		log.info("保存工厂订单...");
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.save(forder);
			for (Factoryorderdetail d : forder.getFactoryorderdetails()) {
				saveDetail(session, d, Bool.FALSE, Bool.FALSE);
			}
			tx.commit();
			log.info("保存工厂订单成功!");
		} catch (Exception e) {
			tx.rollback();
			log.info("保存工厂订单中出现异常, 事务回滚!", e);
			if (e instanceof DataException) {
				throw (DataException) e;
			} else
				throw new DataException("保存工厂订单出错! 订单号:{0}",
						new Object[] { forder.getOrderNo() }, e);
		}
	}

	/**
	 * @param session
	 * @param fod
	 * @param doubleCheck 
	 * @param updateRelatedData 
	 */
	private void saveDetail(Session session, Factoryorderdetail fod, int doubleCheck, int updateRelatedData) {
		if (fod.getId() == null) {
			setDefaultValues(fod);
			session.save(fod);
		} else {
			session.merge(fod);
			if (updateRelatedData == Bool.FALSE) {
				return;
			}
		}
		if (doubleCheck == Bool.FALSE) {
			log.info("Simply save factory order detail! id="+fod.getId());
			return;
		}
		List<Overflow> overflows = findOverflows(session, fod.getProduct());
		consumeOverflow(session, fod, overflows);
	}

	/**
	 * @param session
	 * @param fod
	 * @param overflows
	 */
	public void consumeOverflow(Session session, Factoryorderdetail fod,
			List<Overflow> overflows) {
		int availableCount = fod.getAvailableCount();
		for (Overflow ovf : overflows) {
			if (!session.contains(ovf)) {//this overflow has been deleted
				continue;
			}
			Integer overflowCount = ovf.getOverflowCount();
			int delta = Math.min(availableCount, overflowCount);
			if (delta == 0) {
				return;
			}
			updateOverflow(session, ovf, overflowCount - delta);
			availableCount -= delta;
			fod.setAvailableCount(availableCount);
			session.save(new Available(fod, ovf.getOrderdetail(), delta));
		}
		fod = (Factoryorderdetail) session.merge(fod);
		updateStatView(session, fod);
	}

	/**
	 * @param session
	 * @param fod
	 */
	private void updateStatView(Session session, Factoryorderdetail fod) {
		Product product = fod.getProduct();
		Integer pid = product.getId();
		StatManager.getInstance().updateStatView(session, new int[] { pid });
	}

	/**
	 * @param ovf
	 * @param ovCnt
	 */
	private void updateOverflow(Session session, Overflow ovf, int ovCnt) {
		if (ovCnt == 0) {
			session.delete(ovf);
		} else {
			ovf.setOverflowCount(ovCnt);
			session.merge(ovf);
		}
	}
	
	/**
	 * 
	 * @param session
	 * @param fod 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Overflow> findAllOverflows(Session session) {
		Query query = session
				.createQuery("from Overflow o where o.orderdetail is not null");
		return query.list();
	}
	
	
	/**
	 * 
	 * @param session
	 * @param detail
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Overflow> findOverflows(Session session, Product product) {
		Query query = session
				.createQuery("from Overflow o where o.orderdetail is not null and o.orderdetail.product.id=:id");
		query.setInteger("id", product.getId());
		return query.list();
	}

	/**
	 * 查找订单明细orderdetailId对应的溢出记录, 如果不存在返回null.
	 * 
	 * @param session
	 * @param orderdetailId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Overflow findOverflow(Session session, Integer orderdetailId) {
		Query query = session
				.createQuery("from Overflow o where o.orderdetail.id=:odId");
		query.setParameter("odId", orderdetailId);
		List list = query.list();
		if (list.isEmpty())
			return null;
		else
			return (Overflow) list.get(0);
	}
}
