package com.mvc.model.sys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "menu_info")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TMenu implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8597691961406963236L;
	private String menu_id;
	private String menu_name;
	private String url;
	private TMenu parent;
	private String sort;
	private String type;
	private String level;
	@NotBlank
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public String getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}
	public String getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@ManyToOne
	@JoinColumn(name = "pid")
	public TMenu getParent() {
		return parent;
	}
	public void setParent(TMenu parent) {
		this.parent = parent;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
}
