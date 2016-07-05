package com.dixin.hibernate;

import com.dixin.annotation.Name;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.JSONalizeType;
import com.dixin.hibernate.convert.Parse;
import com.dixin.hibernate.convert.ParseType;

@Name("状态视图")
public abstract class AbstractStatView extends BaseJDO implements
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
	private Repertory repertory;

	private Integer csCount;
	private Integer faCount;

	private String productModel;
	private String storehouseName;
	private Integer repertoryQuantity;

	// Constructors

	/** default constructor */
	public AbstractStatView() {
	}

	/** full constructor */
	public AbstractStatView(Repertory repertory, Integer csCount,
			Integer faCount) {
		this.repertory = repertory;
		this.csCount = csCount;
		this.faCount = faCount;

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

	@JSONalize
	public Integer getRepertoryQuantity() {
		return repertoryQuantity;
	}

	@Parse
	public void setRepertoryQuantity(Integer repertoryQuantity) {
		this.repertoryQuantity = repertoryQuantity;
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

	/**
	 * @return the repertory
	 */
	@JSONalize(type = JSONalizeType.Properties, properties = "id")
	public Repertory getRepertory() {
		return repertory;
	}

	/**
	 * @param repertory
	 *            the repertory to set
	 */
	@Parse(type = ParseType.Helper, helper = com.dixin.business.impl.RepertoryHelper.class, findFields = "id")
	public void setRepertory(Repertory repertory) {
		this.repertory = repertory;
	}

}
