package com.noahedu.demo.mybatis.dao;

import com.noahedu.demo.mybatis.core.DaoBase;
import com.noahedu.demo.mybatis.domain.User;

import java.util.List;



public class UserDaoImpl  extends DaoBase<User> implements UserDao {


	public UserDaoImpl() {
		super(UserDao.class.getName(), User.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updatePassword(User user) {
		// getSqlSession().update(getNamespace()+".updatePassword", user); 
		update("updatePassword", user);
	}

	@Override
	public List<User> listTestCustomQuery() {
		// TODO Auto-generated method stub
		return list("listTestCustomQuery", User.class);
	}

}
