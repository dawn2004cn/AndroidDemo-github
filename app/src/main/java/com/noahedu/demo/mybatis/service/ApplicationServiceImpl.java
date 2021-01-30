package com.noahedu.demo.mybatis.service;

import com.google.inject.Inject;
import com.noahedu.demo.mybatis.dao.RoleDao;
import com.noahedu.demo.mybatis.dao.UserDao;
import com.noahedu.demo.mybatis.domain.Role;
import com.noahedu.demo.mybatis.domain.User;


/**
 * Created by refitgustaroska on 10/27/15.
 */
public class ApplicationServiceImpl implements ApplicationService {

    @Inject
    private UserDao userDao;

    @Inject
    private RoleDao roleDao;


    public void createUser(User user, Role role){
        role.setId(roleDao.newId());

        user.setId(userDao.newId());
        user.setRole(role);

        roleDao.add(role);
        userDao.add(user);
    }
}
