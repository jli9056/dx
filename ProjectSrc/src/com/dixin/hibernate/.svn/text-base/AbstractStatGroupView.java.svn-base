package com.dixin.hibernate;

import java.util.List;

import com.dixin.annotation.Aggregate;
import com.dixin.annotation.Name;
import com.dixin.business.IAggregation;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.Parse;

@Name("按产品库存视图")
public abstract class AbstractStatGroupView extends BaseJDO implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9077027639141723972L;
	// Fields

	/**
	 * 
	 */
	private Integer id;
	private String productModel;
	private String storehouseName;
	private Integer repertoryQuantity;
	private Integer needCount;
	private Integer csCount;
	private Integer faCount;
	private List<StatView> statViews;

	// Constructors

	/** default constructor */
	public AbstractStatGroupView() {
	}

	public List<StatView> getStatViews() {
		return statViews;
	}

	public void setStatViews(List<StatView> statViews) {
		this.statViews = statViews;
	}

	@JSONalize
	public String getStatViewsHTML() {

		StringBuilder product = new StringBuilder();
		StringBuilder storehouse = new StringBuilder();
		StringBuilder repertoryQuantity = new StringBuilder();
		StringBuilder csCount = new StringBuilder();
		StringBuilder faCount = new StringBuilder();
		StringBuilder needCount = new StringBuilder();

		// product.append("<table>");//<tr><td>产品</td></tr>");
		storehouse.append("<table>");// <tr><td>仓库</td></tr>");
		repertoryQuantity.append("<table>");// <tr><td>库存数量</td></tr>");
		csCount.append("<table>");// <tr><td>客户订货数量</td></tr>");
		faCount.append("<table>");// <tr><td>工厂欠货数量</td></tr>");
		// needCount.append("<table>");//<tr><td>&nbsp;</td></tr>");

		if (statViews != null) {
			for (StatView sv : statViews) {
				// product.append("<tr><td nowrap>" + sv.getProductModel()
				// + "</td></tr>");
				storehouse.append("<tr><td nowrap>" + sv.getStorehouseName()
						+ "</td></tr>");
				repertoryQuantity.append("<tr><td nowrap>"
						+ sv.getRepertoryQuantity() + "</td></tr>");
				csCount.append("<tr><td nowrap>" + sv.getCsCount()
						+ "</td></tr>");
				faCount.append("<tr><td nowrap>" + sv.getFaCount()
						+ "</td></tr>");
				// needCount.append("<tr><td>&nbsp;</td></tr>");
			}
		}
		StringBuilder buf = new StringBuilder();
		buf.append("<td>" + "</td>");
		buf.append("<td><div>" + storehouse.toString() + "</table></div></td>");
		buf.append("<td><div>" + repertoryQuantity.toString()
				+ "</table></div></td>");
		buf.append("<td><div>" + csCount.toString() + "</table></div></td>");
		buf.append("<td><div>" + faCount.toString() + "</table></div></td>");
		buf.append("<td>" + "</td>");
		return buf.toString();
	}

	@JSONalize
	public String getProductModel() {
		return productModel;
	}

	@Parse
	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	@JSONalize
	public String getStorehouseName() {
		return storehouseName;
	}

	@Parse
	public void setStorehouseName(String storehouseName) {
		this.storehouseName = storehouseName;
	}

	@Aggregate(name = "库存数量", property = "repertoryQuantity", type = IAggregation.SUM)
	@JSONalize
	public Integer getRepertoryQuantity() {
		return repertoryQuantity;
	}

	@Parse
	public void setRepertoryQuantity(Integer repertoryQuantity) {
		this.repertoryQuantity = repertoryQuantity;
	}

	@Aggregate(name = "超额数量", property = "needCount", type = IAggregation.SUM)
	@JSONalize
	public Integer getNeedCount() {
		return needCount;
	}

	@Parse
	public void setNeedCount(Integer needCount) {
		this.needCount = needCount;
	}

	/**
	 * @return the id
	 */
	@JSONalize
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	@Parse
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the csCount
	 */
	@Aggregate(name = "客户已订数量", property = "csCount", type = IAggregation.SUM)
	@JSONalize
	public Integer getCsCount() {
		return csCount;
	}

	/**
	 * @param csCount
	 *            the csCount to set
	 */
	@Parse
	public void setCsCount(Integer csCount) {
		this.csCount = csCount;
	}

	/**
	 * @return the faCount
	 */
	@Aggregate(name = "工厂欠货数量", property = "csCount", type = IAggregation.SUM)
	@JSONalize
	public Integer getFaCount() {
		return faCount;
	}

	/**
	 * @param faCount
	 *            the faCount to set
	 */
	@Parse
	public void setFaCount(Integer faCount) {
		this.faCount = faCount;
	}

}
