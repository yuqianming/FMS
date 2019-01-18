package com.mvc.model.sys;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "t_supplier_adj")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TSupplierAdj implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3038914573328793142L;
	private int id;
	private String s_supplier_code;
	private String s_supplier_name;
	private String t_supplier_code;
	private String t_supplier_name;
	private String update_by;
	private String org_id;
	private Date update_time;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getS_supplier_code() {
		return s_supplier_code;
	}
	public void setS_supplier_code(String s_supplier_code) {
		this.s_supplier_code = s_supplier_code;
	}
	public String getS_supplier_name() {
		return s_supplier_name;
	}
	public void setS_supplier_name(String s_supplier_name) {
		this.s_supplier_name = s_supplier_name;
	}
	public String getT_supplier_code() {
		return t_supplier_code;
	}
	public void setT_supplier_code(String t_supplier_code) {
		this.t_supplier_code = t_supplier_code;
	}
	public String getT_supplier_name() {
		return t_supplier_name;
	}
	public void setT_supplier_name(String t_supplier_name) {
		this.t_supplier_name = t_supplier_name;
	}
	public String getUpdate_by() {
		return update_by;
	}
	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
}
