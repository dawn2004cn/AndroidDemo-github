package com.noahedu.demo.mybatis.core;


public abstract class Entity {

	private int id;
	private int rowNum;
	
	public Entity() {
		this(0);
	}
	
	public Entity(int id) {
		this.id=id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id= id;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	
}
