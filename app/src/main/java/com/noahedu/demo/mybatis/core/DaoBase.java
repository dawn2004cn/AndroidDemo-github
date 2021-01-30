package com.noahedu.demo.mybatis.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class DaoBase<T extends Entity > extends QueryBase implements DaoGeneric<T> {
	
	protected Class<T> type;


	public DaoBase(String namespace, Class<T> type) {
		super(namespace);
		this.type = type;
	}
	
	@Override
	public int newId(){
		return getSqlSession().selectOne(getNamespace()+".newid");
	}
	
	@Override
	public int count(){
		return getSqlSession().selectOne(getNamespace()+".count");
	}
	
	@Override
	public int countTrash(){
		return getSqlSession().selectOne(getNamespace()+".countTrash");
	}
	
	@Override
	public void add(T object){
		Entity e = ((Entity) object);
		if(e.getId() < 1) e.setId(newId());
		getSqlSession().insert(getNamespace()+".add", object);
	}
	
	@Override
	public void delete(int id){
		delete(id, false);
	}

	@Override
	public void delete(int id, boolean permanent){
		if(permanent) {
			getSqlSession().delete(getNamespace() + ".deletePermanent", id);
		}else{
			getSqlSession().delete(getNamespace() + ".delete", id);
		}
	}
	
	@Override
	public void restore(int id){
		getSqlSession().delete(getNamespace()+".restore", id);
	}
	
	@Override
	public void empty(){
		getSqlSession().delete(getNamespace()+".empty");
	}
	
	@Override
	public void clean(){
		getSqlSession().delete(getNamespace()+".clean");
	}
	
	@Override
	public void update(T object){
		getSqlSession().update(getNamespace()+".update", object);
	}
	
	@Override
	public List<T> list(){
		List<Clause> filter = new ArrayList<Clause>();
		int index = -1;
		int count = -1;
		String order = "";
		return list(filter, index, count, order);	
	}
	
	@Override
	public List<T> list(List<Clause> filter){
		int index = -1;
		int count = -1;
		String order = "";
		return list(filter, index, count, order);	
	}
	
	@Override
	public List<T> list(List<Clause> filter, String order){
		int index = -1;
		int count = -1;
		return list(filter, index, count, order);	
	}
	
	@Override
	public List<T> list(int index, int count){
		List<Clause> filter = new ArrayList<Clause>();
		String order = "";
		return list(filter, index, count, order);		
	}
	
	@Override
	public List<T> list(int index, int count, String order){
		List<Clause> filter = new ArrayList<Clause>();
		return list(filter, index, count, order);		
	}
	
	@Override
	public List<T> list(List<Clause> filter, int index, int count){
		String order = "";
		return list(filter, index, count, order);
	}
	
	@Override
	public List<T> list(List<Clause> filter, int index, int count, String order){
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
		return list("list", this.type, filter, index, count, order);
	}
	
	@Override
	public T get(int id){
		return getSqlSession().selectOne(getNamespace()+".get", id);
	}
	
	@Override
	public T getWithDetailCollection(int id){
		return getSqlSession().selectOne(getNamespace()+".getWithDetailCollection", id);
	}

}
