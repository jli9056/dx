package com.dixin.hibernate;

import java.util.HashSet;
import java.util.Set;

import com.dixin.annotation.Name;
import com.dixin.hibernate.convert.JSONalize;
import com.dixin.hibernate.convert.JSONalizeType;
import com.dixin.hibernate.convert.Parse;

/**
 * AbstractMenu entity provides the base persistence definition of the Menu
 * entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Name("菜单")
public abstract class AbstractMenu extends com.dixin.hibernate.BaseJDO
		implements Comparable<Object>, java.io.Serializable {

	// Fields

	private Integer id;
	private Menu parent;
	private Integer sort;
	private Boolean leaf;
	private String text;
	private String url;
	private String description;
	private Set<Role> roles = new HashSet<Role>(0);
	private Set<Action> actions = new HashSet<Action>(0);
	private Set<Menu> children = new HashSet<Menu>(0);

	// Constructors
	public String toString() {
		return this.text;
	}

	/** default constructor */
	public AbstractMenu() {
	}

	/** minimal constructor */
	public AbstractMenu(String text) {
		this.text = text;
	}

	/** full constructor */
	public AbstractMenu(Menu menu, Integer sort, String text, String url,
			String description, Set<Role> roles, Set<Action> actions,
			Set<Menu> menus) {
		this.parent = menu;
		this.sort = sort;
		this.text = text;
		this.url = url;
		this.description = description;
		this.roles = roles;
		this.actions = actions;
		this.children = menus;
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

	@Parse
	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	@JSONalize(type = JSONalizeType.Properties)
	public Menu getParent() {
		return this.parent;
	}

	@Parse
	public void setParent(Menu menu) {
		this.parent = menu;
	}

	@JSONalize
	public String getText() {
		return this.text;
	}

	@Parse
	public void setText(String text) {
		this.text = text;
	}

	@JSONalize
	public Boolean getLeaf() {
		return this.leaf;
	}

	@JSONalize
	public String getUrl() {
		return this.url;
	}

	@Parse
	public void setUrl(String url) {
		this.url = url;
	}

	@JSONalize
	public String getDescription() {
		return this.description;
	}

	@Parse
	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@JSONalize
	public Set<Menu> getChildren() {
		return this.children;
	}

	public void setChildren(Set<Menu> menus) {
		this.children = menus;
	}

	public Set<Action> getActions() {
		return actions;
	}

	public void setActions(Set<Action> actions) {
		this.actions = actions;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public int compareTo(Object o) {
		return compareTo((AbstractMenu) o);
	}

	public int compareTo(AbstractMenu m) {
		if (m == null) {
			return 0;
		}
		if (this.sort == m.sort) {
			return 0;
		}
		if (this.sort == null || m.sort == null) {
			return 0;
		}
		return this.sort - m.sort;
	}
}