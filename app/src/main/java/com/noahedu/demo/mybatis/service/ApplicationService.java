package com.noahedu.demo.mybatis.service;


import com.noahedu.demo.mybatis.domain.Role;
import com.noahedu.demo.mybatis.domain.User;

/**
 * Created by refitgustaroska on 10/27/15.
 */
public interface ApplicationService {

    public void createUser(User user, Role role);
}
