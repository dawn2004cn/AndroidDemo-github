package com.noahedu.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：BeanHelper$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/26$ 20:11$
 */
public class BeanHelper<T> {
    //用来保存实例化后的对象
    private static final Map<Class<?>,Object> BEAN_MAP=new HashMap<Class<?>,Object>();

    static{
        //获得类集合
        Set<Class<?>> classSet=ClassHelper.getBeanClassSet();
        for(Class<?> c : classSet){
            try {
                //封装了数据库管理，和本文无关
                if(DBHelper.class.isAssignableFrom(c)){
                    if(c.isAnnotationPresent(DB.class)){
                        DB db=c.getAnnotation(DB.class);
                        String dbName=db.db_name();
                        int dbVersion=db.db_version();
                        int resourceId=db.db_xml_id();
                        DBHelper.DB_NAME=dbName;
                        DBHelper.DB_VERSION=dbVersion;
                        DBHelper.DB_RESOURCE=resourceId;
                    }
                }else{
                    //实例化
                    BEAN_MAP.put(c,c.newInstance());
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    /**
     *
     * 由于在service之中，动态代理的实例莫名其妙为null，因此先判断一下，如果为null就先依赖注入
     *
     * @param cls
     * @return
     */
    public static <T> T getBean(Class<T> cls){
        //获得实例化后的对象，该方法可以手动调用，类似于spring从BeanFactory中获取对象
        T bean=(T) BEAN_MAP.get(cls);
        //这里判断动态代理的实例是否null，如果未null，则从新注入
        bean=(T) IocHelper.mapperInject(cls,bean,BEAN_MAP);
        return bean;
    }

	/*
	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> c){
		return (T) BEAN_MAP.get(c);
	}
	*/
}
