/**
 * 
 */
package com.dixin.business.impl;

import java.text.MessageFormat;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dixin.business.DataException;
import com.dixin.business.StatManager;
import com.dixin.business.constants.Bool;
import com.dixin.business.constants.DeliveryStatus;
import com.dixin.business.constants.OrderStatus;
import com.dixin.business.constants.RefundMethod;
import com.dixin.hibernate.Deliverydetail;
import com.dixin.hibernate.Order;
import com.dixin.hibernate.Product;
import com.dixin.hibernate.Refund;
import com.dixin.hibernate.Repertory;
import com.dixin.hibernate.Storehouse;

/**
 * 
 */
public class RefundHelper extends BaseHelper<Refund> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.impl.BaseHelper#deleteById(java.lang.Integer)
	 */
	@Override
	public boolean deleteById(Integer id) {
		Refund refund = super.findById(id);
		if (refund == null) {
			throw new DataException(MessageFormat.format(
					"找不到相应的返修或退货信息：id={0}", id));
		}
		if (RefundMethod.返修.toString().equals(refund.getMethod())) {
			if (refund.getFinished().equals(Bool.TRUE)) {
				{
					throw new DataException("返修产品已修复，不能删除返修信息！");
				}
			}
		} else if (RefundMethod.退货.toString().equals(refund.getMethod())) {
			throw new DataException("产品已退货，不能删除退货信息！");
		}
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			if (RefundMethod.返修.toString().equals(refund.getMethod())) {
				if (refund.getFinished().equals(Bool.FALSE)) {
					Deliverydetail dd = refund.getDeliverydetail();
					dd.setQuantity(dd.getQuantity() + refund.getQuantity());// 返修的产品返回到送货订单上
					dd.setStatus("");
					session.delete(refund);
				}
			}
			tx.commit();
			int pid = refund.getDeliverydetail().getProduct().getId();
			StatManager.getInstance()
					.updateStatView(session, new int[] { pid });
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.impl.BaseHelper#save(com.dixin.hibernate.BaseJDO)
	 */
	public void save(Refund r) {
		DeliveryHelper helper = new DeliveryHelper();
		Integer quantity = r.getQuantity();
		if (quantity <= 0)
			throw new DataException("返修或退货的产品数量必须大于0!");
		if (RefundMethod.返修.toString().equals(r.getMethod())) {
			helper.repairProduct(r.getDeliverydetail(), quantity, r
					.getRefundDate());
		} else if (RefundMethod.退货.toString().equals(r.getMethod())) {
			helper.refundProduct(r.getDeliverydetail(), quantity, r
					.getRefundDate());
		}
		
		Order order = r.getDeliverydetail().getOrder();
		if (OrderStatus.FINISHED.equals(order.getStatus())) {
			Session session = getSession();
			Transaction tx = session.beginTransaction();
			try {
				order.setStatus(OrderStatus.NEW_ORDER);
				session.merge(order);
			} catch (Exception e) {
				tx.rollback();
				log.error("rollback", e);
			}
		}
	}

	/**
	 * 返修成功, 把修复后的正常的产品放入到storehouse的库存中.
	 * 
	 * @param refund
	 * @param storehouse
	 * @param quanlity
	 */
	@SuppressWarnings("unchecked")
	public void revive(Refund refund, Storehouse storehouse, int quantity) {
		if (quantity <= 0 || quantity > refund.getQuantity()) {
			throw new DataException("quantity参数不能小于0或大于返修数目!");
		}
		if (RefundMethod.退货.toString().equals(refund.getMethod())) {
			throw new DataException("退货不能使用修复功能!");
		}
		if (refund.getFinished().intValue() == Bool.TRUE) {
			throw new DataException("返修已完成, 不能再使用修复功能!");
		}
		int refundedCount = refund.getRefundedCount() + quantity;
		if (refundedCount > refund.getQuantity()) {
			throw new DataException("已换退货数量不能超过{0}!", new Object[] { refund
					.getQuantity() });
		}
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Deliverydetail deliverydetail = refund.getDeliverydetail();
			Product product = deliverydetail.getProduct();
			Query query = session
					.createQuery("from Repertory r where r.product.id=:pid and r.storehouse.id=:sid");
			query.setParameter("pid", product.getId());
			query.setParameter("sid", storehouse.getId());
			List result = query.list();
			Repertory repertory = null;
			if (result.isEmpty()) {
				repertory = new Repertory(storehouse, product, quantity, 0);
				session.save(repertory);
			} else {
				repertory = (Repertory) result.get(0);
				repertory.setQuantity(repertory.getQuantity() + quantity);
				session.merge(repertory);
			}
			new RepertoryHelper().ingather(session, repertory, quantity,
					deliverydetail.getCost());
			new CorrectionHelper().log(session, storehouse, product, quantity,
					deliverydetail.getCost(), "[入库]修复后入库");
			refund.setRefundedCount(refundedCount);
			if (refundedCount == refund.getQuantity().intValue()) {
				refund.setFinished(Bool.TRUE);
			} else {
				refund.setFinished(Bool.FALSE);
			}
			if (!DeliveryStatus.全部送货成功.toString().equals(
					deliverydetail.getStatus())) {
				deliverydetail.setStatus(DeliveryStatus.已修复.toString()
						+ quantity + "件");
			}
			session.merge(refund);
			tx.commit();
			
			StatManager.getInstance().updateStatView(session,
					new int[] { product.getId() });
		} catch (Exception e) {
			tx.rollback();
			log.error("rollback:", e);
		}
	}
}
