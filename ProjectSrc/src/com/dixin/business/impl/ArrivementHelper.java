/**
 * 
 */
package com.dixin.business.impl;

import java.text.MessageFormat;
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
import com.dixin.business.impl.RepertoryHelper.BiddingResult;
import com.dixin.hibernate.Arrivedetail;
import com.dixin.hibernate.Arrivement;
import com.dixin.hibernate.Factoryorder;
import com.dixin.hibernate.Factoryorderdetail;
import com.dixin.hibernate.Orderdetail;
import com.dixin.hibernate.Product;
import com.dixin.hibernate.Repertory;
import com.dixin.hibernate.Reserved;

/**
 * @author Jason
 * 
 */
public class ArrivementHelper extends BaseHelper<Arrivement> {

	private static final RepertoryHelper repertoryHelper = new RepertoryHelper();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.impl.BaseHelper#deleteById(java.lang.Integer)
	 */
	@Override
	public boolean deleteById(Integer id) {
		log.info("删除工厂到货记录... ID=" + id);
		Arrivement arrivement = findById(id);
//		Factoryorder factoryorder = arrivement.getFactoryorder();
//		if (Bool.TRUE == factoryorder.getFinished().intValue()) {
//			log.error(MessageFormat.format("工厂订单{0}已完成, 不能删除!", factoryorder.getOrderNo()));
//			throw new DataException("工厂订单已完成, 不能刪除!");
//		}
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Set<Arrivedetail> details = arrivement.getArrivedetails();
			if (arrivement.getDoubleCheck() == Bool.TRUE) {
				// 当在已复核状态下删除到货才需要检查留货信息
				checkReserved(session, details);
			}
			deleteDetails(session, details, arrivement.getDoubleCheck());
			session.delete(arrivement);
			tx.commit();
			log.info("删除工厂到货记录成功! ID="+id	);
			return true;
		} catch (Exception e) {
			tx.rollback();
			log.info("删除工厂到货失败, 进行回滚操作! ID="+id , e);
			if (e instanceof DataException) {
				throw (DataException) e;
			}
			return false;
		}
	}

	/**
	 * @param session
	 * @param details
	 */
	private void checkReserved(Session session, Set<Arrivedetail> details) {
		StringBuffer buf = new StringBuffer();
		for (Arrivedetail dd : details) {
			Repertory r = repertoryHelper.findRepertoryInfo(session, dd
					.getStorehouse(), dd.getProduct());
			if(r==null) continue;
			String sql = "select r from Reserved r,Orderdetail od where r.storehouse.id=:sid and od.product.id=:pid and r.orderdetail.id=od.id";
			Query query = session.createQuery(sql);
			query.setParameter("sid", r.getStorehouse().getId());
			query.setParameter("pid", r.getProduct().getId());
			List holdings = query.list();
			int reserved_count = 0;
			if (holdings.size() > 0) {
				for (Iterator itr = holdings.iterator(); itr.hasNext();) {
					Reserved rs = (Reserved) itr.next();
					Orderdetail orderdetail = rs.getOrderdetail();
					if (orderdetail != null) {
						reserved_count += rs.getReservedCount();
					}
				}
				int free_count = r.getQuantity() - reserved_count;
				if (dd.getQuantity() <= free_count)
					break;
				//库存自由数量不够，需要用户手工解除留货
				for (Iterator itr = holdings.iterator(); itr.hasNext();) {
					Reserved rs = (Reserved) itr.next();
					Orderdetail orderdetail = rs.getOrderdetail();
					if (orderdetail != null) {
						buf.append("【");
						buf.append(orderdetail.getId());
						buf.append(",\t");
						buf.append(rs.getReservedCount());
						buf.append(",\t");
						buf.append(dd.getQuantity() - free_count);
						buf.append("】");
					}
				}
			}
		}
		if (buf.length() > 0) {
			buf
					.insert(0,
							"以下订单明细已有留货，请解除留货后再进行删除或编辑到货的操作！<br/><br/>【订单明细号,\t已留货数量,\t需解除留货数量】<br/>");
			throw new DataException(buf.toString());
		}
	}

	/**
	 * @param session
	 * @param details
	 * @param doubleCheck 
	 * @param forder
	 */
	private void deleteDetails(Session session, Set<Arrivedetail> details, int doubleCheck) {
		for (Arrivedetail detail : details) {
			deleteDetail(session, detail, doubleCheck);
		}
	}

	/**
	 * @param session
	 * @param detail
	 * @param doubleCheck 
	 */
	private void deleteDetail(Session session, Arrivedetail detail, int doubleCheck) {
		if (doubleCheck == Bool.TRUE) {
			Factoryorder forder = detail.getFactoryorder();
			Repertory r = updateRepertory(session, detail, detail.getQuantity(), false);
			updateFactoryoderOwnedCount(session, forder
					.getFactoryorderdetails(), r.getProduct(), detail
					.getQuantity());
			updateFactoryOrderStatus(session, forder);
		}
		log.info("删除工厂到货明细. ID=" + detail.getId());
		session.delete(detail);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.impl.BaseHelper#merge(com.dixin.hibernate.BaseJDO)
	 */
	@Override
	public Arrivement merge(Arrivement detachedInstance) {
		Arrivement oldAvm = this.findById(detachedInstance.getId());
		final int doubleCheck = detachedInstance.getDoubleCheck();
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Set<Arrivedetail> detachedAdSet = detachedInstance.getArrivedetails();
			Set<Arrivedetail> oldAdSet = oldAvm.getArrivedetails();
			Map<Arrivedetail, Arrivedetail> updateMap = getUpdateMapping(oldAdSet, detachedAdSet);
			final int firstCheck = doubleCheck - oldAvm.getDoubleCheck();
			if (firstCheck == Bool.FALSE) {// merge checking
				checkReserved(session, updateMap.keySet());
			}
			for (Arrivedetail od : updateMap.keySet()) {
				Arrivedetail dd = updateMap.get(od);
				if (firstCheck == Bool.TRUE) {
					session.delete(od);
				} else {
					deleteDetail(session, od, doubleCheck);
				}
				if (dd != null) {// update
					saveDetail(session, dd, doubleCheck);
				}
			}
			Arrivement arrivement = (Arrivement) session
					.merge(detachedInstance);
			tx.commit();
			return arrivement;
		} catch (Exception e) {
			tx.rollback();
			log.info("rollback:" + e);
			if (e instanceof DataException)
				throw (DataException) e;
			else
				throw new DataException("编辑到货信息失败!", e);
		}
	}

	/**
	 * @param oldAdSet
	 * @param detachedAdSet
	 * @return
	 */
	private Map<Arrivedetail, Arrivedetail> getUpdateMapping(
			Set<Arrivedetail> oldAdSet, Set<Arrivedetail> detachedAdSet) {
		Map<Arrivedetail, Arrivedetail> updateMap = new HashMap<Arrivedetail, Arrivedetail>();
		for (Arrivedetail od : oldAdSet) {
			boolean found = false;
			for (Arrivedetail dd : detachedAdSet) {
				if (od.getId().equals(dd.getId())) {
					updateMap.put(od, dd);
					found = true;
					break;
				}
			}
			if (!found) {
				updateMap.put(od, null);
			}
		}
		return updateMap;
	}

	/**
	 * 把到货信息arrivement保存到仓库storehouse中.
	 * 
	 * @param arrivement
	 * @param storehouse
	 */
	public void save(Arrivement arrivement) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.save(arrivement);
			Set<Arrivedetail> details = arrivement.getArrivedetails();
			saveDetails(session, details, arrivement.getDoubleCheck());
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			log.info("rollback:" + e);
			if (e instanceof DataException)
				throw (DataException) e;
			else
				throw new DataException("保存到货信息失败!", e);
		}
	}

	/**
	 * @param session
	 * @param details
	 * @param doubleCheck 
	 */
	private void saveDetails(Session session, Set<Arrivedetail> details,
			int doubleCheck) {
		for (Arrivedetail detail : details) {
			saveDetail(session, detail, doubleCheck);
		}
	}

	/**
	 * @param session
	 * @param detail
	 * @param doubleCheck 
	 */
	private void saveDetail(Session session, Arrivedetail detail, int doubleCheck) {
		if(detail.getQuantity()<=0){
			throw new DataException("到货数量必须大于0");
		}
		Factoryorder forder = detail.getFactoryorder();
//		if (Bool.TRUE == forder.getFinished().intValue()) {
//			log.error(MessageFormat.format("工厂订单{0}已完成, 不能再到货!", forder.getOrderNo()));
//			throw new DataException("工厂订单已完成, 不能再到货!");
//		}
		copyProductCost(forder, detail);
		session.save(detail);
		if (doubleCheck == Bool.TRUE) {
			Repertory r = updateRepertory(session, detail,detail.getQuantity(), true);
			// 工厂订单明细的欠货数量减少
			updateFactoryoderOwnedCount(session, forder
					.getFactoryorderdetails(), r.getProduct(), -detail
					.getQuantity());
			updateFactoryOrderStatus(session, forder);
		}
	}

	/**
	 * copy product cost from corresponding factory order detail.
	 * @param forder
	 * @param detail
	 */
	private void copyProductCost(Factoryorder forder, Arrivedetail detail) {
		Set<Factoryorderdetail> fdetails = forder.getFactoryorderdetails();
		for (Factoryorderdetail fod : fdetails) {
			if (fod.getProduct().getId().equals(detail.getProduct().getId())) {
				detail.setCost(fod.getCost());
				break;
			}
		}
	}

	private void updateFactoryOrderStatus(Session session, Factoryorder forder) {
		int ownedCount = 0;
		for (Factoryorderdetail fod : forder.getFactoryorderdetails()) {
			ownedCount += fod.getOwnedCount();
		}
		if (ownedCount == 0) {
			forder.setFinished(Bool.TRUE);
		} else {
			forder.setFinished(Bool.FALSE);
		}
		session.merge(forder);
	}

	/**
	 * @param session
	 * @param detail
	 * @param delta
	 * @param isAdd
	 * @return
	 */
	private Repertory updateRepertory(Session session, Arrivedetail detail,
			Integer delta, boolean isAdd) {
		CorrectionHelper correctionHelper = new CorrectionHelper();
		Repertory r = repertoryHelper.findRepertoryInfo(session, detail
				.getStorehouse(), detail.getProduct());
		if (isAdd) {//保存到货
			if (r != null) {
				String storehouseName = r.getStorehouse().getName();
				int quantity = r.getQuantity() + delta;
				log.info(MessageFormat.format("更新库存记录! 仓库:{0} 原数量:{1} 现数量:{2}",
						storehouseName, r.getQuantity(), quantity));
				r.setQuantity(quantity);
				r = (Repertory) session.merge(r);
			} else {
				r = new Repertory(detail.getStorehouse(), detail.getProduct(),
						delta, 0);
				String storehouseName = r.getStorehouse().getName();
				session.save(r);
				log.info(MessageFormat.format("添加库存记录! 仓库:{0} 数量:{1}", storehouseName,
						r.getQuantity()));
			}
			Product p = r.getProduct();
			repertoryHelper.ingather(session, r, delta, detail.getCost());
			correctionHelper.log(session, detail, r.getStorehouse(), p, delta, detail.getCost(), "入库[到货]");
		} else {//删除到货
			if (r != null) {
				BiddingResult result = repertoryHelper.consume(session, r, delta);
				if (result.count < delta) {
					throw new DataException("库存数量小于" + delta
							+ ",不能删除！");
				}
				int remaining = r.getQuantity() - result.count;
				if (remaining == 0) {
					String storehouseName = r.getStorehouse().getName();
					log.info(MessageFormat.format("删除库存信息! 仓库:{0} 数量:{1}",
							storehouseName, delta));
					session.delete(r);
				} else if (remaining > 0) {
					String storehouseName = r.getStorehouse().getName();
					log.info(MessageFormat.format("减小库存信息! 仓库:{0} 原数量:{1} 现数量:{2}",
							storehouseName, r.getQuantity(), remaining));
					r.setQuantity(remaining);
					r = (Repertory) session.merge(r);
				}
				Product p = r.getProduct();
				correctionHelper.log(session, detail, r.getStorehouse(), p, -result.count, p.getCost(), "出库[到货删除]");
			} else {
				throw new DataException("仓库【" + detail.getStorehouseName()
						+ "】中产品【" + detail.getProduct().getModel()
						+ "】库存为0，无法删除");
			}
		}
		return r;
	}

	/**
	 * 
	 * @param fdetails
	 * @param product
	 * @param delta
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void updateFactoryoderOwnedCount(Session session, Set<Factoryorderdetail> fdetails,
			Product product, Integer delta) {
		for (Factoryorderdetail fod : fdetails) {
			log.info("更新工厂订单明细欠货数量... ID:" + fod.getId());
			if (fod.getProduct().equals(product)) {
				int ownedCount = fod.getOwnedCount() + delta;
				if (ownedCount < 0) {
					String msg = MessageFormat.format("产品【{0}】到货数量不能大于工厂订单数量!",
							product.getModel());
					log.error(msg);
					throw new DataException(msg);
				}
				log.info(MessageFormat.format("欠货数量更新! 原数量:{0} 现数量:{1}", fod
						.getOwnedCount(), ownedCount));
				fod.setOwnedCount(ownedCount);
				
				int availableCount = fod.getAvailableCount();
				if (delta > 0) {// restore available
					Query query = session
							.createQuery("select sum(a.consumedCount) from Available a where a.factoryorderdetail.id=:id");
					query.setInteger("id", fod.getId());
					List result = query.list();
					Long consumedCount = null;
					if (result.isEmpty() == false) {
						consumedCount = (Long) result.get(0);
					}
					if (consumedCount == null) {
						consumedCount = new Long(0);
					}
					fod.setAvailableCount(fod.getQuantity()
							- consumedCount.intValue());
					log.info(MessageFormat.format("可用数量更新! 原数量:{0} 现数量:{1}", fod
							.getAvailableCount(), fod.getAvailableCount()));
				} else if (ownedCount < availableCount) {
					log.info(MessageFormat.format("欠货数量小于可用数量. 欠货数量:{0} 可用数量:{1}",
							ownedCount, availableCount));
					fod.setAvailableCount(ownedCount);
				}
				session.merge(fod);
				StatManager.getInstance().updateStatView(session,
						new int[] { product.getId() });// 到货时更新库存视图表
				return;
			}
		}
	}
}
