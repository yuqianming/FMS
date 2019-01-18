package com.mvc.model.sys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "table_excel")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TTableExcel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5779249609379049180L;
	private int id;
	private String table_name;
	private String field;
	private String type;
	private String field_name;
	private int column_num;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getField_name() {
		return field_name;
	}
	public void setField_name(String field_name) {
		this.field_name = field_name;
	}
	public int getColumn_num() {
		return column_num;
	}
	public void setColumn_num(int column_num) {
		this.column_num = column_num;
	}
}
