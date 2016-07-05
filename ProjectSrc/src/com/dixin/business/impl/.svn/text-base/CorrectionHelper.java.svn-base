/**
 * 
 */
package com.dixin.business.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dixin.DixinException;
import com.dixin.business.DataException;
import com.dixin.business.StatManager;
import com.dixin.business.constants.Bool;
import com.dixin.hibernate.AdC;
import com.dixin.hibernate.Arrivedetail;
import com.dixin.hibernate.Correction;
import com.dixin.hibernate.OdC;
import com.dixin.hibernate.Orderdetail;
import com.dixin.hibernate.Product;
import com.dixin.hibernate.Repertory;
import com.dixin.hibernate.Storehouse;

/**
 * @author Jason
 * 
 */
public class CorrectionHelper extends BaseHelper<Correction> {
	/**
	 * @deprecated
	 */
	public Correction merge(Correction detachedInstance) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#save(com.dixin.hibernate.BaseJDO)
	 */
	public void save(Correction c) {
		Session session = this.getSession();
		Transaction t = session.beginTransaction();
		try {
			save(session, c);
			t.commit();
		} catch (DixinException ex) {
			throw ex;
		} catch (Exception ex) {
			try {
				t.rollback();
			} catch (Exception e) {
				log.error("事务回滚失败", e);
			}
			throw new DataException("增加库存修正记录失败", ex);
		}
	}

	/**
	 * 
	 * @param session
	 * @param st
	 * @param p
	 * @param quantity
	 * @param cost
	 * @param comment
	 * @return
	 */
	public Integer log(Session session, Storehouse st, Product p,
			int quantity, double cost, String comment) {
		Correction correction = new Correction(st, p, quantity, cost,
				new Date(), Bool.TRUE, comment);
		return (Integer) session.save(correction);
	}

	/**
	 * 
	 * @param session
	 * @param detail
	 * @param st
	 * @param p
	 * @param quantity
	 * @param cost
	 * @param comment
	 */
	@SuppressWarnings("unchecked")
	public void log(Session session, Arrivedetail detail, Storehouse st,
			Product p, int quantity, double cost, String comment) {
		if (quantity > 0) {
			Integer cid = log(session, st, p, quantity, cost, comment);
			Correction correction = new CorrectionHelper().findById(cid);
			session.save(new AdC(detail, correction, quantity));
		} else {
			Query query = session
					.createQuery("from AdC where arrivedetail.id=:aid");
			query.setInteger("aid", detail.getId());
			List result = query.list();
			if (result.size() > 0) {
				for (Iterator i = result.iterator(); i.hasNext();) {
					AdC adc = (AdC) i.next();
					if (adc.getDelta() + quantity == 0) {
						session.delete(adc);
						session.delete(adc.getCorrection());
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param session
	 * @param detail
	 * @param st
	 * @param p
	 * @param quantity
	 * @param cost
	 * @param comment
	 */
	public void log(Session session, Orderdetail detail, Storehouse st,
			Product p, int quantity, double cost, String comment) {
		if (quantity < 0) {
			Integer cid = log(session, st, p, quantity, cost, comment);
			Correction correction = new CorrectionHelper().findById(cid);
			session.merge(detail);
			session.save(new OdC(detail, correction, quantity));
		} else {
			Query query = session
					.createQuery("from OdC where orderdetail.id=:oid");
			query.setInteger("oid", detail.getId());
			List result = query.list();
			if (result.size() > 0) {
				for (Iterator i = result.iterator(); i.hasNext();) {
					OdC ddc = (OdC) i.next();
					if (ddc.getDelta() + quantity == 0) {
						session.delete(ddc);
						session.delete(ddc.getCorrection());
					}
				}
			}
		}
	}
	
	/**
	 * @param session
	 * @param correction
	 */
	public void save(Session session, Correction correction) {
		modifyRepertory(session, correction.getStorehouse(), correction
				.getProduct(), correction.getQuantity(), correction.getCost());
		String comment = correction.getComment();
		if (comment == null || "".equals(comment.trim())) {
			if (correction.getQuantity() >= 0)
				correction.setComment("入库[手工修正]");
			else
				correction.setComment("出库[手工修正]");
		}
		session.save(correction);
	}

	/**
	 * 
	 * @param session
	 * @param sh
	 * @param prod
	 * @param quantity
	 * @param cost 
	 */
	private void modifyRepertory(Session session, Storehouse sh, Product prod,
			Integer quantity, Double cost) {
		if (quantity == 0)
			throw new DataException("库存修正数量不能为0!");
		Repertory r = getOrCreateRepertory(session, sh, prod);
		int count = r.getQuantity() + quantity;
		if (count < 0) {
			throw new DataException("修正失败，如此修正后库存是负数");
		}
		if ((count - r.getReservedCount()) < 0) {
			throw new DataException("修正失败，此次修正影响到了已留货库存。请先解除部分留货。");
		}
		RepertoryHelper repertoryHelper = new RepertoryHelper();
		if (quantity > 0) {
			session.saveOrUpdate(r);
			repertoryHelper.ingather(session, r, quantity, cost);
		} else {
			repertoryHelper.consume(session, r, -quantity);
		}
		if (count > 0) {
			r.setQuantity(count);
			session.saveOrUpdate(r);
		}
		else if (count == 0)
			session.delete(r);
		else
			throw new DataException("库存不能为负数!");
		
		StatManager.getInstance().updateStatView(session,
				new int[] { prod.getId() });
	}


	@SuppressWarnings("unchecked")
	private Repertory getOrCreateRepertory(Session session,
			Storehouse storehouse, Product prod) {
		Query query = session
				.createQuery("from Repertory r where r.storehouse.id=:id and r.product.id=:pid");

		query.setInteger("id", storehouse.getId());
		query.setInteger("pid", prod.getId());
		List<Repertory> results = query.list();
		if (results.size() > 0) {
			return results.get(0);
		} else {
			return new Repertory(storehouse, prod, 0, 0);
		}
	}

	public boolean deleteById(Integer id) {
		Session session = this.getSession();
		Transaction t = session.beginTransaction();
		try {
			Correction c = this.findById(id);
			modifyRepertory(session, c.getStorehouse(), c.getProduct(), 0 - c
					.getQuantity(), c.getCost());
			session.delete(c);
			t.commit();
			return true;
		} catch (DixinException ex) {
			throw ex;
		} catch (Exception ex) {
			t.rollback();
			throw new DataException("删除库存修正记录失败", ex);
		}
	}
}
