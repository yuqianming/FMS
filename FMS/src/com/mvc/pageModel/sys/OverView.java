package com.mvc.pageModel.sys;

import java.math.BigDecimal;

public class OverView {
	private String project_code;
	private String address_code;
	private String address_name;
	private String account_name;
	private String supplier_name;
	private BigDecimal account_amt;
	private BigDecimal total_amt_i_tax;
	private BigDecimal t_pay_amt;//已付款
	private BigDecimal n_pay_amt;//未付款
	private BigDecimal t_invoice_amt;
	private BigDecimal t_invoice_amt_e_tax;
	private String project_category;
	private BigDecimal tax_rate;
	private boolean hide_zero;
	private boolean hide_account;
	private String org_id;
	public String getProject_code() {
		return project_code;
	}
	public void setProject_code(String project_code) {
		this.project_code = project_code;
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
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	public String getSupplier_name() {
		return supplier_name;
	}
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}
	public BigDecimal getAccount_amt() {
		return account_amt;
	}
	public void setAccount_amt(BigDecimal account_amt) {
		this.account_amt = account_amt;
	}
	public BigDecimal getTotal_amt_i_tax() {
		return total_amt_i_tax;
	}
	public void setTotal_amt_i_tax(BigDecimal total_amt_i_tax) {
		this.total_amt_i_tax = total_amt_i_tax;
	}
	public BigDecimal getT_pay_amt() {
		return t_pay_amt;
	}
	public void setT_pay_amt(BigDecimal t_pay_amt) {
		this.t_pay_amt = t_pay_amt;
	}
	public BigDecimal getN_pay_amt() {
		return n_pay_amt;
	}
	public void setN_pay_amt(BigDecimal n_pay_amt) {
		this.n_pay_amt = n_pay_amt;
	}
	public String getProject_category() {
		return project_category;
	}
	public void setProject_category(String project_category) {
		this.project_category = project_category;
	}
	public BigDecimal getT_invoice_amt() {
		return t_invoice_amt;
	}
	public void setT_invoice_amt(BigDecimal t_invoice_amt) {
		this.t_invoice_amt = t_invoice_amt;
	}
	public BigDecimal getT_invoice_amt_e_tax() {
		return t_invoice_amt_e_tax;
	}
	public void setT_invoice_amt_e_tax(BigDecimal t_invoice_amt_e_tax) {
		this.t_invoice_amt_e_tax = t_invoice_amt_e_tax;
	}
	public BigDecimal getTax_rate() {
		return tax_rate;
	}
	public void setTax_rate(BigDecimal tax_rate) {
		this.tax_rate = tax_rate;
	}
	public boolean isHide_zero() {
		return hide_zero;
	}
	public void setHide_zero(boolean hide_zero) {
		this.hide_zero = hide_zero;
	}
	public boolean isHide_account() {
		return hide_account;
	}
	public void setHide_account(boolean hide_account) {
		this.hide_account = hide_account;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
}
