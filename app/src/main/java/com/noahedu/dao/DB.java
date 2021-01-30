package com.noahedu.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：DB$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/26$ 20:24$
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DB {
    String db_name() default "myTask.db";
    int db_version() default 1;
    int db_xml_id();
}
