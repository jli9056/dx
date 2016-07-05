package com.dixin.hibernate;

import com.dixin.annotation.Name;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.Parse;

/**
 * AbstractMenu entity provides the base persistence definition of the Menu
 * entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Name("操作")
public abstract class AbstractAction extends com.dixin.hibernate.BaseJDO
		implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private Menu menu;

	// Constructors
	public String toString() {
		return this.name;
	}

	/** default constructor */
	public AbstractAction() {
	}

	/** minimal constructor */
	public AbstractAction(String name) {
		this.name = name;
	}

	/** full constructor */
	public AbstractAction(String name, Menu menu) {
		this.name = name;
		this.menu = menu;
	}

	// Property accessors
	@JSONalize
	public Integer getId() {
		return this.id;
	}

	@Parse
	public void setId(Integer id) {
		this.id = id;
	}

	@JSONalize
	public String getName() {
		return this.name;
	}

	@Parse
	public void setName(String name) {
		this.name = name;
	}

	public Menu getMenu() {
		return this.menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

}