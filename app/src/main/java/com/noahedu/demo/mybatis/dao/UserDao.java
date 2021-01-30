package com.noahedu.demo.mybatis.dao;

import com.noahedu.demo.mybatis.core.DaoGeneric;
import com.noahedu.demo.mybatis.domain.User;

import java.util.List;



public interface UserDao extends DaoGeneric<User> {

	public void updatePassword(User user);
	
	public List<User> listTestCustomQuery();

}
