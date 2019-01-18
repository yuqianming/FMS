package com.mvc.model.sys;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "t_store_deal")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TStoreDeal implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5847870829055991606L;
	private int id;
	private String project_code;
	private String project_name;
	private String order_code;
	private String batch_no;
	private String material_code;
	private String material_name;
	private String busi_type;
	private String opration_type;
	private String supplier_code;
	private String supplier_name;
	private int quantity_org;
	private int quantity_txn;
	private int quantity_left;
	private BigDecimal total_amt_e_tax;
	private BigDecimal total_amt_i_tax;
	private Date create_time;
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
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public String getBatch_no() {
		return batch_no;
	}
	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}
	public String getMaterial_code() {
		return material_code;
	}
	public void setMaterial_code(String material_code) {
		this.material_code = material_code;
	}
	public String getMaterial_name() {
		return material_name;
	}
	public void setMaterial_name(String material_name) {
		this.material_name = material_name;
	}
	public String getBusi_type() {
		return busi_type;
	}
	public void setBusi_type(String busi_type) {
		this.busi_type = busi_type;
	}
	public String getOpration_type() {
		return opration_type;
	}
	public void setOpration_type(String opration_type) {
		this.opration_type = opration_type;
	}
	public String getSupplier_code() {
		return supplier_code;
	}
	public void setSupplier_code(String supplier_code) {
		this.supplier_code = supplier_code;
	}
	public String getSupplier_name() {
		return supplier_name;
	}
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}
	public int getQuantity_org() {
		return quantity_org;
	}
	public void setQuantity_org(int quantity_org) {
		this.quantity_org = quantity_org;
	}
	public int getQuantity_txn() {
		return quantity_txn;
	}
	public void setQuantity_txn(int quantity_txn) {
		this.quantity_txn = quantity_txn;
	}
	public int getQuantity_left() {
		return quantity_left;
	}
	public void setQuantity_left(int quantity_left) {
		this.quantity_left = quantity_left;
	}
	public BigDecimal getTotal_amt_e_tax() {
		return total_amt_e_tax;
	}
	public void setTotal_amt_e_tax(BigDecimal total_amt_e_tax) {
		this.total_amt_e_tax = total_amt_e_tax;
	}
	public BigDecimal getTotal_amt_i_tax() {
		return total_amt_i_tax;
	}
	public void setTotal_amt_i_tax(BigDecimal total_amt_i_tax) {
		this.total_amt_i_tax = total_amt_i_tax;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
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
