package com.noahedu.demo.mybatis.core;

public class Clause {

	private String column;
	private String value;
	private String operator;
	
	public Clause(String column, String value) {
		this(column, "like", value);
	}
	
	public Clause(String column, String operator, String value) {
		this.operator = operator;
		this.column = column;
		this.value = value;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
}
