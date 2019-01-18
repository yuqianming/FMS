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
@Table(name = "t_payment_temp")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TPaymentTemp implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8660253724114233182L;
	private int id;
	private String project_code;
	private String supplier_code;
	private String supplier_name;
	private String order_code;
	private String account_code;
	private String account_name;
	private String address_code;
	private String address_name;
	private BigDecimal invoice_amt;
	private BigDecimal invoice_amt_e_tax;
	private BigDecimal pay_amt;
	private BigDecimal pay_amt_e_tax;
	private String month;
	private String voucher_no;
	private String online_offline;
	private String settle_no;
	private String remark;
	private BigDecimal tax_rate;
	private String accounting_org;
	private String update_by;
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
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
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
	public BigDecimal getInvoice_amt() {
		return invoice_amt;
	}
	public void setInvoice_amt(BigDecimal invoice_amt) {
		this.invoice_amt = invoice_amt;
	}
	public BigDecimal getInvoice_amt_e_tax() {
		return invoice_amt_e_tax;
	}
	public void setInvoice_amt_e_tax(BigDecimal invoice_amt_e_tax) {
		this.invoice_amt_e_tax = invoice_amt_e_tax;
	}
	public BigDecimal getPay_amt() {
		return pay_amt;
	}
	public void setPay_amt(BigDecimal pay_amt) {
		this.pay_amt = pay_amt;
	}
	public BigDecimal getPay_amt_e_tax() {
		return pay_amt_e_tax;
	}
	public void setPay_amt_e_tax(BigDecimal pay_amt_e_tax) {
		this.pay_amt_e_tax = pay_amt_e_tax;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getVoucher_no() {
		return voucher_no;
	}
	public void setVoucher_no(String voucher_no) {
		this.voucher_no = voucher_no;
	}
	public String getOnline_offline() {
		return online_offline;
	}
	public void setOnline_offline(String online_offline) {
		this.online_offline = online_offline;
	}
	public String getSettle_no() {
		return settle_no;
	}
	public void setSettle_no(String settle_no) {
		this.settle_no = settle_no;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public BigDecimal getTax_rate() {
		return tax_rate;
	}
	public void setTax_rate(BigDecimal tax_rate) {
		this.tax_rate = tax_rate;
	}
	public String getAccounting_org() {
		return accounting_org;
	}
	public void setAccounting_org(String accounting_org) {
		this.accounting_org = accounting_org;
	}
	public String getUpdate_by() {
		return update_by;
	}
	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
}
