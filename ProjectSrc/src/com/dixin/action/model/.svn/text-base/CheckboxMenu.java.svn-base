package com.dixin.action.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dixin.hibernate.Menu;
import com.dixin.hibernate.Role;
import com.dixin.hibernate.User;

public class CheckboxMenu implements Serializable, Comparable<CheckboxMenu> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean checked;
	private Integer id;
	private Integer sort;
	private Boolean leaf;
	private String text;
	private String url;
	private String description;
	private List<CheckboxMenu> children = new ArrayList<CheckboxMenu>();

	public CheckboxMenu(Menu menu) {
		this.id = menu.getId();
		this.text = menu.getText();
		this.url = menu.getUrl();
		this.sort = menu.getSort();
		this.leaf = menu.getLeaf();
		this.description = menu.getDescription();
		for (Object obj : menu.getChildren()) {
			Menu m = (Menu) obj;
			CheckboxMenu child = new CheckboxMenu(m);
			children.add(child);
		}
		Collections.sort(children);
	}

	public CheckboxMenu(Menu menu, Role r) {
		this.checked = menu.getRoles().contains(r);
		this.id = menu.getId();
		this.sort = menu.getSort();
		this.text = menu.getText();
		this.url = menu.getUrl();
		this.leaf = menu.getLeaf();
		this.description = menu.getDescription();
		for (Object obj : menu.getChildren()) {
			Menu m = (Menu) obj;
			CheckboxMenu child = new CheckboxMenu(m, r);
			children.add(child);
		}
		Collections.sort(children);
	}

	public CheckboxMenu(Menu menu, User u) {
		this.id = menu.getId();
		this.text = menu.getText();
		this.url = menu.getUrl();
		this.sort = menu.getSort();
		this.leaf = menu.getLeaf();
		this.description = menu.getDescription();
		for (Object obj : menu.getRoles()) {
			Role role = (Role) obj;
			if (role.getUsers().contains(u)) {
				this.checked = true;
				break;
			}
		}
		for (Object obj : menu.getChildren()) {
			Menu m = (Menu) obj;
			CheckboxMenu child = new CheckboxMenu(m, u);
			children.add(child);
		}
		Collections.sort(children);
	}

	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<CheckboxMenu> getChildren() {
		return children;
	}

	public void setChildren(List<CheckboxMenu> children) {
		this.children = children;
	}

	public int compareTo(CheckboxMenu o) {
		if (o == null) {
			return -1;
		}
		return this.sort - o.sort;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}
