/**
 * 
 */
package com.dixin.action.repertory;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import net.sf.json.JSONObject;

import com.dixin.action.AbstractAction;
import com.dixin.action.ActionException;
import com.dixin.business.IPagedResult;
import com.dixin.business.impl.ProductHelper;
import com.dixin.business.impl.RepertoryHelper;
import com.dixin.business.impl.StorehouseHelper;
import com.dixin.business.query.QueryDefn;
import com.dixin.hibernate.Product;
import com.dixin.hibernate.Repertory;
import com.dixin.hibernate.Storehouse;

/**
 * Load Action For JDO Repertory
 * 
 * @author Luo
 * 
 */
public class TransferRepertoryAction extends AbstractAction<Repertory> {
	private ProductHelper productHelper;

	private StorehouseHelper storehouseHelper;

	public TransferRepertoryAction() {
		super(Repertory.class, RepertoryHelper.class);
	}

	/**
	 * 
	 */
	public void init() {
		if (productHelper == null) {
			productHelper = new ProductHelper();
			storehouseHelper = new StorehouseHelper();
		}
	}

	@Override
	public boolean process(HttpServletRequest request, JSONObject result) {
		String product = request.getParameter("product");
		String inHouse = request.getParameter("inStorehouse");
		String outHouse = request.getParameter("outStorehouse");
		String quantity = request.getParameter("quantity");
		int count = Integer.parseInt(quantity);
		init();
		Product p = getProduct(product);
		Storehouse in = getIn(inHouse);
		Storehouse out = getOut(outHouse);
		((RepertoryHelper) getHelper()).transfer(out, in, p, count);
		return true;
	}

	public Product getProduct(String product) {
		QueryDefn qf = new QueryDefn();
		Criterion c = Restrictions.eq("model", product);
		Criterion c1 = Restrictions.eq("alias", product);
		Criterion c2 = Restrictions.eq("barcode", product);
		c = Restrictions.or(c, c1);
		c = Restrictions.or(c, c2);
		qf.addCriterion(c);
		IPagedResult<Product> pr = productHelper.find(qf);
		if (pr.count() > 1) {
			throw new ActionException("产品不明确，有多个产品的型号、别名或者条码符合输入值。");
		}
		if (pr.count() < 1) {
			throw new ActionException("指定的产品不存在。");
		}
		return pr.getResult(0, 1).get(0);
	}

	public Storehouse getIn(String storehouse) {
		IPagedResult<Storehouse> pr = getStorehouse(storehouse);
		if (pr.count() > 1) {
			throw new ActionException("调拨的目的仓库不明确，有多个仓库符合指定的名字");
		}
		if (pr.count() < 1) {
			throw new ActionException("调拨的目的仓库不存在。");
		}
		return pr.getResult(0, 1).get(0);
	}

	public Storehouse getOut(String storehouse) {
		IPagedResult<Storehouse> pr = getStorehouse(storehouse);
		if (pr.count() > 1) {
			throw new ActionException("调拨的来源仓库不明确，有多个仓库符合指定的名字");
		}
		if (pr.count() < 1) {
			throw new ActionException("调拨的来源仓库不存在。");
		}
		return pr.getResult(0, 1).get(0);
	}

	public IPagedResult<Storehouse> getStorehouse(String storehouse) {
		QueryDefn qf = new QueryDefn();
		Criterion c = Restrictions.eq("name", storehouse);
		if (storehouse.matches("[0-9]+")) {
			Criterion c1 = Restrictions.eq("id", Integer.valueOf(storehouse));
			c = Restrictions.or(c, c1);
		}
		qf.addCriterion(c);
		IPagedResult<Storehouse> pr = storehouseHelper.find(qf);
		return pr;
	}

	public String getLocalizedName() {
		return "库存调拨";
	}
}
