package com.mvc.model.sys;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "t_material_adj")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TMaterialAdj implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3858762836407007390L;
	private int id;
	private String operate_type;
	private String item_explain;
	private BigDecimal amt;
	private String voucher_no;
	private String system_type;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOperate_type() {
		return operate_type;
	}
	public void setOperate_type(String operate_type) {
		this.operate_type = operate_type;
	}
	public String getItem_explain() {
		return item_explain;
	}
	public void setItem_explain(String item_explain) {
		this.item_explain = item_explain;
	}
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public String getVoucher_no() {
		return voucher_no;
	}
	public void setVoucher_no(String voucher_no) {
		this.voucher_no = voucher_no;
	}
	public String getSystem_type() {
		return system_type;
	}
	public void setSystem_type(String system_type) {
		this.system_type = system_type;
	}
}
