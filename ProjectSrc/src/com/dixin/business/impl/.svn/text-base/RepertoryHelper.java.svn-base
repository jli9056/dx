/**
 * 
 */
package com.dixin.business.impl;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;

import com.dixin.business.DataException;
import com.dixin.business.StatManager;
import com.dixin.hibernate.Bidding;
import com.dixin.hibernate.Product;
import com.dixin.hibernate.Repertory;
import com.dixin.hibernate.Storehouse;

/**
 * @author Jason
 * 
 */
public class RepertoryHelper extends BaseHelper<Repertory> {

	private static final CorrectionHelper correctionHelper = new CorrectionHelper();

	static class BiddingResult {
		int count = 0;
		double cost = 0;
	}

	/**
	 * 出货.
	 * 
	 * @param storehouse
	 * @param product
	 * @param quantity
	 */
	@SuppressWarnings("unchecked")
	public BiddingResult consume(Session session, Repertory r, int quantity) {
		BiddingResult biddingResult = new BiddingResult();
		List<Bidding> sortedBiddings = getSortedBiddingList(session, r.getId());
		int cnt = 0;
		for (Iterator i = sortedBiddings.iterator(); i.hasNext() && cnt < quantity; cnt++) {
			Bidding bidding = (Bidding) i.next();
			biddingResult.count++;
			biddingResult.cost += bidding.getCost();
			session.delete(bidding);
		}
		
		if (biddingResult.count > 0) {
			biddingResult.cost /= biddingResult.count;
		}
		return biddingResult;
	}

	@SuppressWarnings("unchecked")
	private List<Bidding> getSortedBiddingList(Session session, Integer id) {
		Query query = session
				.createQuery("from Bidding b where b.repertory.id=:rid order by b.cost,b.id");
		query.setInteger("rid", id);
		return query.list();
	}

	
	/**
	 * 入货.
	 * 
	 * @param storehouse
	 * @param product
	 * @param quantity
	 */
	public void ingather(Session session, Repertory r, int quantity, double cost) {
		for (int i = 0; i < quantity; i++) {
			Bidding bidding = new Bidding(r, cost);
			session.save(bidding);
		}
	}

	/**
	 * 
	 * @param session
	 * @param storehouse
	 * @param product
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Repertory findRepertoryInfo(Session session, Storehouse storehouse,
			Product product) {
		Query query = session
				.createQuery("from Repertory r where r.storehouse.id=:id and r.product.id=:pid");
		query.setInteger("id", storehouse.getId());
		query.setInteger("pid", product.getId());
		List result = query.list();
		return (Repertory) (result.isEmpty() ? null : result.get(0));
	}

	/**
	 * 从仓库st1中调拨count个产品product到仓库st2中.
	 * 
	 * @param st1
	 * @param st2
	 * @param product
	 * @param count
	 */
	@SuppressWarnings("unchecked")
	public void transfer(Storehouse st1, Storehouse st2, Product product,
			int count) {
		log.info(MessageFormat.format("库存调拨... 调出仓库:{0} 调入仓库:{1} 调拨数量:{2}", st1
				.getName(), st2.getName(), count));
		if (st1.equals(st2)) {
			log.error("调出仓库与调入仓库相同, 不能调拨!");
			throw new DataException("调出仓库与调入仓库相同, 不能调拨!");
		}
		if (count <= 0) {
			log.error("调拨数目必须大于0!");
			throw new DataException("调拨数目必须大于0!");
		}
		Session session = getSession();
		Criteria criteria = session.createCriteria(Repertory.class);
		criteria.add(Expression.eq("storehouse.id", st1.getId()));
		criteria.add(Expression.eq("product.id", product.getId()));
		List list = criteria.list();
		if (list.isEmpty()) {
			throw new DataException("仓库【{0}】中没有产品型号【{1}】!", new Object[] {
					st1.getName(), product.getModel() });
		}
		Repertory repertory = (Repertory) list.get(0);
		Integer quantity = repertory.getQuantity();
		int availableCount = quantity - repertory.getReservedCount();
		if (availableCount == 0 || availableCount < count) {
			throw new DataException("库存不足, 无法调拨!");
		}
		Transaction tx = session.beginTransaction();
		try {
			BiddingResult biddingResult = this.consume(session, repertory, count);
			String comment = "出库[调拨]【"+st1.getName()+"】->【"+st2.getName()+"】";
			correctionHelper.log(session, st1, product, -count, biddingResult.cost, comment);
			if (quantity - count > 0) {
				repertory.setQuantity(quantity - count);// 库存中的数目减少count个
				session.merge(repertory);
			} else if (quantity == count) {
				session.delete(repertory);
			}

			criteria = session.createCriteria(Repertory.class);
			criteria.add(Expression.eq("storehouse.id", st2.getId()));
			criteria.add(Expression.eq("product.id", product.getId()));
			list = criteria.list();
			Repertory r = null;
			if (list.isEmpty()) {
				// 库存中没有仓库storehouse的记录,添加count个该产品的库存,留货数目为0
				r = new Repertory(st2, repertory.getProduct(), count, 0);
				session.save(r);
			} else {
				r = (Repertory) list.get(0);
				r.setQuantity(r.getQuantity() + count);
				session.merge(r);
			}
			comment = "入库[调拨]【"+st2.getName()+"】<-【"+st1.getName()+"】";
			correctionHelper.log(session, st2, product, count, biddingResult.cost, comment);
			this.ingather(session, r, count, biddingResult.cost);
			
			StatManager.getInstance().updateStatView(session,
					new int[] { product.getId() });
			
			tx.commit();
			log.info("库存调拨成功!");
		} catch (Exception e) {
			tx.rollback();
			log.info("库存调拨过程中出现异常, 事务回滚!", e);
			throw new DataException("库存调拨失败!", e);// 抛出异常由UI捕获
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.impl.BaseHelper#deleteById(java.lang.Integer)
	 */
	@Override
	public boolean deleteById(Integer id) {
		log.info("删除库存信息... ID=" + id);
		Repertory repertory = findById(id);
		if (repertory == null) {
			log.info("库存信息不存在, 无法删除!");
			throw new DataException("库存信息不存在, 无法删除!");
		}
		if (repertory.getReservedCount() > 0) {
			log.info("库存信息已有留貨, 不能刪除!");
			throw new DataException("库存信息已有留貨, 不能刪除!");
		}
		log.info("删除库存信息成功! ID=" + id);
		return super.deleteById(id);
	}

	/**
	 * @deprecated 只能通过到货信息输入、留货和调拨业务改变库存信息.
	 */
	public Repertory merge(Repertory detachedInstance) {
		throw new UnsupportedOperationException();
	}
}
