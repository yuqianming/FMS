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
@Table(name = "t_pms")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TPms implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8353022385366431974L;
	private int id;
	private String project_code;
	private String project_name;
	private String build_mode;
	private String accept_date;
	private String deliver_date;
	private String project_status;
	private String order_code;
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
	public String getProject_code() {
		return project_code;
	}
	public void setProject_code(String project_code) {
		this.project_code = project_code;
	}
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getBuild_mode() {
		return build_mode;
	}
	public void setBuild_mode(String build_mode) {
		this.build_mode = build_mode;
	}
	public String getAccept_date() {
		return accept_date;
	}
	public void setAccept_date(String accept_date) {
		this.accept_date = accept_date;
	}
	public String getDeliver_date() {
		return deliver_date;
	}
	public void setDeliver_date(String deliver_date) {
		this.deliver_date = deliver_date;
	}
	public String getProject_status() {
		return project_status;
	}
	public void setProject_status(String project_status) {
		this.project_status = project_status;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
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
