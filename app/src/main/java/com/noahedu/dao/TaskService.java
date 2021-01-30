package com.noahedu.dao;

import com.noahedu.demo.R;

import java.io.Serializable;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：TaskService$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/26$ 20:54$
 */
public class TaskService implements Serializable {
    @Table(value= R.xml.task)
    private TaskMapper taskMapper;
}
