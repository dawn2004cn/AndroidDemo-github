package com.noahedu.demo.mybatis.dao;

import com.noahedu.demo.mybatis.domain.User;

import java.util.List;

/**
 * © 2020 www.youxuepai.com
 *
 * @version 1.0
 * @file name：UserMapper$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/11/30$ 17:12$
 */
public interface UserMapper {
    public void insert(User user);
    public void update(User user);
    public void delete(Integer id);
    public User select(Integer id);
    public List<User> selectAll();

}
