package com.noahedu.dao;

import android.content.Context;

import com.noahedu.demo.R;

import java.lang.reflect.Proxy;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：BaseService$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/26$ 20:15$
 */
public class BaseService<T> {
    protected Context context;
    protected T t;

    protected BaseService(Context context, DaoHelper hc){
        this.context=context;
        DaoHelper loader=new DaoHelper();
        loader.setXmlId(R.xml.task);
        loader.setHelper(hc.getHelper());
        loader.setContext(context);
        try {
            Object newProxyInstance = Proxy.newProxyInstance(
                    loader.getMapper().getClassLoader(),
                    new Class[] {loader.getMapper()}, loader);
            this.t=(T) newProxyInstance;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
