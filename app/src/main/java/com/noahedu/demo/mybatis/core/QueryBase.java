package com.noahedu.demo.mybatis.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class QueryBase extends SqlSessionDaoSupport {

	private String namespace;

	public QueryBase(String namespace) {
		this.namespace = namespace;
	}
	
	protected String getNamespace() {
		return namespace;
	}
	
	
	protected <T> List<T> list(String query, Class<T> returnType) {
		List<Clause> filter = new ArrayList<Clause>();
		int index = -1;
		int count = -1;
		String order = "";
		return list(query, returnType, filter, index, count, order);
	}

	
	protected <T> List<T> list(String query, Class<T> returnType, List<Clause> filter) {
		int index = -1;
		int count = -1;
		String order = "";
		return list(query, returnType, filter, index, count, order);
	}

	
	protected <T> List<T> list(String query, Class<T> returnType, List<Clause> filter, String order) {
		int index = -1;
		int count = -1;
		return list(query, returnType, filter, index, count, order);
	}

	
	protected <T> List<T> list(String query, Class<T> returnType, int index, int count) {
		List<Clause> filter = new ArrayList<Clause>();
		String order = "";
		return list(query, returnType, filter, index, count, order);
	}

	
	protected <T> List<T> list(String query, Class<T> returnType, int index, int count, String order) {
		List<Clause> filter = new ArrayList<Clause>();
		return list(query, returnType, filter, index, count, order);
	}

	
	protected <T> List<T> list(String query, Class<T> returnType, List<Clause> filter, int index, int count) {
		String order = "";
		return list(query, returnType, filter, index, count, order);
	}

	
	protected <T> List<T> list(String query, Class<T> returnType, List<Clause> filter, int index, int count, String order) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		String strLimit = "";
		String strOffset = "";
		if(index >= 0 && count >= 0){
			strLimit = "LIMIT " + count;
			strOffset = "OFFSET " + index;
		}
		param.put("limit", strLimit); // number of rows to be display
		param.put("offset", strOffset); // start from row index to be display
		if(!order.equals(""))
			param.put("order", order);
		if(filter.size() > 0)
			param.put("filter", filter);
		
		return getSqlSession().selectList(this.namespace+"."+query, param);
	}

	protected void update(String query, Object parameter){
		getSqlSession().update(getNamespace()+"."+query, parameter);
	}
	
	protected void update(String query){
		getSqlSession().update(getNamespace()+"."+query);
	}
	
	protected void insert(String query, Object parameter){
		getSqlSession().insert(getNamespace()+"."+query, parameter);
	}
	protected void insert(String query){
		getSqlSession().insert(getNamespace()+"."+query);
	}
	protected void delete(String query, Object parameter){
		getSqlSession().delete(getNamespace()+"."+query, parameter);
	}
	protected void delete(String query){
		getSqlSession().delete(getNamespace()+"."+query);
	}
	

}
