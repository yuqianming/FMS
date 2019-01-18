package com.mvc.pageModel.sys;

import java.math.BigDecimal;

public class ProjectTest {
	private int id;
	private String project_code;
	private String address_name;
	private String supplier_name;
	private String account_name;
	private BigDecimal c_account_amt;//已入账金额
	private BigDecimal c_invoice_amt;//已开票金额
	private BigDecimal c_pay_amt;//已付款金额
	private BigDecimal n_pay_amt;//已签字未登记
	private BigDecimal t_invoice_amt;//本次开票金额
	private BigDecimal t_pay_amt;//本次付款金额
	private String is_pass;
	private String build_mode;
	private String accept_date;
	private String deliver_date;
	private String project_status;
	private String rent_status;
	private String is_cancel;
	private String is_old;
	private BigDecimal tax_rate;
	private String org_id;
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
	public String getAddress_name() {
		return address_name;
	}
	public void setAddress_name(String address_name) {
		this.address_name = address_name;
	}
	public String getSupplier_name() {
		return supplier_name;
	}
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}
	public BigDecimal getC_account_amt() {
		return c_account_amt;
	}
	public void setC_account_amt(BigDecimal c_account_amt) {
		this.c_account_amt = c_account_amt;
	}
	public BigDecimal getC_invoice_amt() {
		return c_invoice_amt;
	}
	public void setC_invoice_amt(BigDecimal c_invoice_amt) {
		this.c_invoice_amt = c_invoice_amt;
	}
	public BigDecimal getC_pay_amt() {
		return c_pay_amt;
	}
	public void setC_pay_amt(BigDecimal c_pay_amt) {
		this.c_pay_amt = c_pay_amt;
	}
	public BigDecimal getN_pay_amt() {
		return n_pay_amt;
	}
	public void setN_pay_amt(BigDecimal n_pay_amt) {
		this.n_pay_amt = n_pay_amt;
	}
	public BigDecimal getT_invoice_amt() {
		return t_invoice_amt;
	}
	public void setT_invoice_amt(BigDecimal t_invoice_amt) {
		this.t_invoice_amt = t_invoice_amt;
	}
	public BigDecimal getT_pay_amt() {
		return t_pay_amt;
	}
	public void setT_pay_amt(BigDecimal t_pay_amt) {
		this.t_pay_amt = t_pay_amt;
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
	public String getRent_status() {
		return rent_status;
	}
	public void setRent_status(String rent_status) {
		this.rent_status = rent_status;
	}
	public String getIs_pass() {
		return is_pass;
	}
	public void setIs_pass(String is_pass) {
		this.is_pass = is_pass;
	}
	public String getIs_cancel() {
		return is_cancel;
	}
	public void setIs_cancel(String is_cancel) {
		this.is_cancel = is_cancel;
	}
	public String getIs_old() {
		return is_old;
	}
	public void setIs_old(String is_old) {
		this.is_old = is_old;
	}
	public BigDecimal getTax_rate() {
		return tax_rate;
	}
	public void setTax_rate(BigDecimal tax_rate) {
		this.tax_rate = tax_rate;
	}
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
}
