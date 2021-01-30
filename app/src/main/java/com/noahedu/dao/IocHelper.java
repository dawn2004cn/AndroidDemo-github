package com.noahedu.dao;

import com.noahedu.common.util.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：IocHelper$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/26$ 20:23$
 */
public class IocHelper {

    private static final String TAG=IocHelper.class.getSimpleName();

    static{
        Map<Class<?>,Object> beanMap=BeanHelper.getBeanMap();
        if(!beanMap.isEmpty()){
            for(Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()){
                Class<?> cls=beanEntry.getKey();
                Object bean=beanEntry.getValue();
                mapperInject(cls,bean,beanMap);
            }
            /*for(LoadHelper.IocLinstener linstener : LoadHelper.getLinstener()){
                LogUtils.d(TAG, "--------------completeInject---------------------");
                linstener.onInjectComplete();
            }*/
        }
    }

    public static Object mapperInject(Class<?> cls,Object bean,Map<Class<?>,Object> beanMap){
        Field[] beanFields=cls.getDeclaredFields();
        for(Field beanField : beanFields){
            //依赖注入@Table注释的mapper
            if(beanField.isAnnotationPresent(Table.class)){
                beanField.setAccessible(true);
                try {
                    //获取注解
                    Table table=beanField.getAnnotation(Table.class);
                    int tableResource=table.value();
                    //通过动态代理实现mapper接口
                    DaoHelper daoHelper=(DaoHelper) beanMap.get(DaoHelper.class);
                    daoHelper.setXmlId(tableResource);
                    daoHelper.setContext(ApplicationSelf.getContext());
                    daoHelper.setHelper(new DBHelper(ApplicationSelf.getContext()));
                    Object newProxyInstance = Proxy.newProxyInstance(
                            daoHelper.getMapper().getClassLoader(),
                            new Class[] {daoHelper.getMapper()}, daoHelper);
                    beanField.set(bean, newProxyInstance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

}
