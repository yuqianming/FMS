package com.mvc.model.sys;


import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "department_info")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TDepartment implements java.io.Serializable {

	private static final long serialVersionUID = 6119176633833266453L;
	
	private String org_id; 
	private String org_name;
	
	private TDepartment department;

	
	public TDepartment() {
		super();
	}

	
	public TDepartment(String org_id) {
		super();
		this.org_id = org_id;
	}

	@Id
	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pid")
	public TDepartment getDepartment() {
		return department;
	}

	public void setDepartment(TDepartment department) {
		this.department = department;
	}


	public String getOrg_name() {
		return org_name;
	}


	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	
	
}