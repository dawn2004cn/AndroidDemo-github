package com.noahedu.demo.mybatis.dao;


import com.noahedu.demo.mybatis.core.DaoBase;
import com.noahedu.demo.mybatis.domain.Role;

public class RoleDaoImpl  extends DaoBase<Role> implements RoleDao {


	public RoleDaoImpl() {
		super(RoleDao.class.getName(), Role.class);
		// TODO Auto-generated constructor stub
	}

	

}
