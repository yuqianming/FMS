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
@Table(name = "t_cost")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TCost implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7926400538482878372L;
	private int id;
	private String list_id;
	private String busi_date;
	private String order_code;
	private String source_type;
	private String supplier_code;
	private String supplier_name;
	private String material_code;
	private String manufacturer;
	private String spec_code;
	private String material_name;
	private String service_code;
	private String unit;
	private BigDecimal quantity;
	private BigDecimal unit_price_e_tax;
	private BigDecimal total_amt_e_tax;
	private BigDecimal total_amt_i_tax;
	private BigDecimal progress;
	private BigDecimal tax_amt;
	private BigDecimal period_cost_amt;
	private BigDecimal account_amt;
	private String project_code;
	private String project_name;
	private String address_code;
	private String address_name;
	private String accounting_org;
	private String doc_type;
	private String documentary;
	private String assemble_sts;
	private String allocation_sts;
	private String asset_trans_sts;
	private String timestamp;
	private String account_code;
	private String account_name;
	private String order_type;
	private String voucher_no;
	private String detail_no;
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
	public String getList_id() {
		return list_id;
	}
	public void setList_id(String list_id) {
		this.list_id = list_id;
	}
	public String getBusi_date() {
		return busi_date;
	}
	public void setBusi_date(String busi_date) {
		this.busi_date = busi_date;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public String getSource_type() {
		return source_type;
	}
	public void setSource_type(String source_type) {
		this.source_type = source_type;
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
	public String getMaterial_code() {
		return material_code;
	}
	public void setMaterial_code(String material_code) {
		this.material_code = material_code;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getSpec_code() {
		return spec_code;
	}
	public void setSpec_code(String spec_code) {
		this.spec_code = spec_code;
	}
	public String getMaterial_name() {
		return material_name;
	}
	public void setMaterial_name(String material_name) {
		this.material_name = material_name;
	}
	public String getService_code() {
		return service_code;
	}
	public void setService_code(String service_code) {
		this.service_code = service_code;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getUnit_price_e_tax() {
		return unit_price_e_tax;
	}
	public void setUnit_price_e_tax(BigDecimal unit_price_e_tax) {
		this.unit_price_e_tax = unit_price_e_tax;
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
	public BigDecimal getProgress() {
		return progress;
	}
	public void setProgress(BigDecimal progress) {
		this.progress = progress;
	}
	public BigDecimal getTax_amt() {
		return tax_amt;
	}
	public void setTax_amt(BigDecimal tax_amt) {
		this.tax_amt = tax_amt;
	}
	public BigDecimal getPeriod_cost_amt() {
		return period_cost_amt;
	}
	public void setPeriod_cost_amt(BigDecimal period_cost_amt) {
		this.period_cost_amt = period_cost_amt;
	}
	public BigDecimal getAccount_amt() {
		return account_amt;
	}
	public void setAccount_amt(BigDecimal account_amt) {
		this.account_amt = account_amt;
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
	public String getAddress_code() {
		return address_code;
	}
	public void setAddress_code(String address_code) {
		this.address_code = address_code;
	}
	public String getAddress_name() {
		return address_name;
	}
	public void setAddress_name(String address_name) {
		this.address_name = address_name;
	}
	public String getAccounting_org() {
		return accounting_org;
	}
	public void setAccounting_org(String accounting_org) {
		this.accounting_org = accounting_org;
	}
	public String getDoc_type() {
		return doc_type;
	}
	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
	}
	public String getDocumentary() {
		return documentary;
	}
	public void setDocumentary(String documentary) {
		this.documentary = documentary;
	}
	public String getAssemble_sts() {
		return assemble_sts;
	}
	public void setAssemble_sts(String assemble_sts) {
		this.assemble_sts = assemble_sts;
	}
	public String getAllocation_sts() {
		return allocation_sts;
	}
	public void setAllocation_sts(String allocation_sts) {
		this.allocation_sts = allocation_sts;
	}
	public String getAsset_trans_sts() {
		return asset_trans_sts;
	}
	public void setAsset_trans_sts(String asset_trans_sts) {
		this.asset_trans_sts = asset_trans_sts;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getAccount_code() {
		return account_code;
	}
	public void setAccount_code(String account_code) {
		this.account_code = account_code;
	}
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	public String getOrder_type() {
		return order_type;
	}
	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}
	public String getVoucher_no() {
		return voucher_no;
	}
	public void setVoucher_no(String voucher_no) {
		this.voucher_no = voucher_no;
	}
	public String getDetail_no() {
		return detail_no;
	}
	public void setDetail_no(String detail_no) {
		this.detail_no = detail_no;
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
