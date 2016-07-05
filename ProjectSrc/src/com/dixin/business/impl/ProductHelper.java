/**
 * 
 */
package com.dixin.business.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;

import com.dixin.business.DataException;
import com.dixin.hibernate.Product;

/**
 * @author Jason
 * 
 */
public class ProductHelper extends BaseHelper<Product> {

	private static final String FIELD_SEP = "☺";

	/**
	 * 
	 * @param product
	 */
	@SuppressWarnings("unchecked")
	private void validate(Product product) {
		String model = product.getModel();
		Criteria criteria = getSession().createCriteria(Product.class);
		criteria.add(Expression.eq("alias", model));
		Integer id = product.getId();
		excludeSelf(criteria, id);
		List list = criteria.list();
		if (list != null && list.size() > 0) {
			throw new DataException("输入的型号【{0}】与已有的型号别称重复!",
					new Object[] { model });
		}
		String alias = product.getAlias();
		if (alias != null) {
			criteria = getSession().createCriteria(Product.class);
			criteria.add(Expression.eq("model", alias));
			excludeSelf(criteria, id);
			list = criteria.list();
			if (list != null && list.size() > 0) {
				throw new DataException("输入的型号别称【{0}】与已有的型号重复!",
						new Object[] { alias });
			}
		}
	}

	/**
	 * @param criteria
	 * @param id
	 */
	private void excludeSelf(Criteria criteria, Integer id) {
		if (id != null) {// 编辑时不必验证本条记录
			criteria.add(Expression.not(Expression.idEq(id)));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#merge(com.dixin.hibernate.BaseJDO)
	 */
	public Product merge(Product detachedInstance) {
		validate(detachedInstance);
		return super.merge(detachedInstance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dixin.business.IBaseHelper#save(com.dixin.hibernate.BaseJDO)
	 */
	public void save(Product transientInstance) {
		validate(transientInstance);
		super.save(transientInstance);
	}

	/**
	 * 统一提高所有产品价格percent个百分点. percent取值范围为[-1~+∞], 负值代表降价, 正值代表提价.
	 * 
	 * @param percent
	 */
	public void modifyPrice(double percent) {
		if (percent < -1) {
			throw new IllegalArgumentException("percent取值范围为[-1~+∞]");
		}
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session
					.createSQLQuery("update product set price=price*"
							+ (1 + percent));
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			log.error(e);
			throw new DataException("修改产品价格失败!", e);
		}
	}

	/**
	 * 
	 * @param file
	 */
	@SuppressWarnings("unchecked")
	public void export(File file) {
		Session session = getSession();
		Transaction tx = session.beginTransaction();
		try {
			BufferedOutputStream out = new BufferedOutputStream(
					new GZIPOutputStream(new FileOutputStream(file)));
			Query query = session
					.createQuery("from Product p order by p.model");
			for (Iterator i = query.iterate(); i.hasNext();) {
				Product p = (Product) i.next();
				String buf = getRecordString(p);
				out.write(buf.getBytes("UTF-8"));
			}
			out.close();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			log.error(e);
			throw new DataException("导出产品信息失败!", e);
		}

	}

	/**
	 * @param p
	 * @return
	 */
	private String getRecordString(Product p) {
		StringBuffer buf = new StringBuffer();
		buf.append(p.getId() + FIELD_SEP);
		buf.append(p.getName() + FIELD_SEP);
		buf.append(p.getModel() + FIELD_SEP);
		buf.append(p.getAlias() + FIELD_SEP);
		buf.append(p.getColor() + FIELD_SEP);
		String s = extract(p.getSize());
		buf.append(s + FIELD_SEP);
		buf.append(p.getMaterial() + FIELD_SEP);
		buf.append(p.getPrice() + "\n");
		return buf.toString();
	}

	/**
	 * 从产品规格中抽取统一格式的信息, x*y*z
	 * 
	 * @param size
	 * @return
	 */
	private String extract(String size) {
		if (size == null)
			return null;
		int[] index = new int[5];
		for (int i = 0; i < index.length; i++) {
			index[i] = -1;
		}
		String[] values = new String[5];
		Pattern ptnLong = Pattern.compile(".*?长.*?([0-9]+).*?");
		Matcher mtrLong = ptnLong.matcher(size);
		if (mtrLong.matches()) {
			index[0] = mtrLong.start(1);
			values[0] = "L:" + mtrLong.group(1);
		}
		Pattern ptnWidth = Pattern.compile(".*?宽.*?([0-9]+).*?");
		Matcher mtrWidth = ptnWidth.matcher(size);
		if (mtrWidth.matches()) {
			index[1] = mtrWidth.start(1);
			values[1] = "W:" + mtrWidth.group(1);
		}
		Pattern ptnHeight = Pattern.compile(".*?高.*?([0-9]+).*?");
		Matcher mtrHeight = ptnHeight.matcher(size);
		if (mtrHeight.matches()) {
			index[2] = mtrHeight.start(1);
			values[2] = "H:" + mtrHeight.group(1);
		}
		Pattern ptnDepth = Pattern.compile(".*?深.*?([0-9]+).*?");
		Matcher mtrDepth = ptnDepth.matcher(size);
		if (mtrDepth.matches()) {
			index[3] = mtrDepth.start(1);
			values[3] = "D:" + mtrDepth.group(1);
		}
		Pattern ptnDiameter = Pattern.compile(".*?直径.*?([0-9]+).*?");
		Matcher mtrDiameter = ptnDiameter.matcher(size);
		if (mtrDiameter.matches()) {
			index[4] = mtrDiameter.start(1);
			values[4] = "Φ:" + mtrDiameter.group(1);
		}
		int[] index2 = new int[5];
		System.arraycopy(index, 0, index2, 0, index.length);
		Arrays.sort(index);
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < index.length; i++) {
			if (index[i] != -1) {
				for (int j = 0; j < index2.length; j++) {
					if (index2[j] == index[i]) {
						buf.append(values[j] + ' ');
						break;
					}
				}
			}
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}
}
