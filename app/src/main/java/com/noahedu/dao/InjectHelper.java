package com.noahedu.dao;

import android.view.View;



import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：InjectHelper$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/26$ 20:12$
 */
public class InjectHelper {
    public InjectHelper(){
        inject(this);
    }

    public static void inject(Object o){
        Class<?> cls=o.getClass();
        try{
            Field[] fields=cls.getDeclaredFields();
            for(Field field : fields){
                Class<?> fcls=field.getType();
                //动态注入resource，和本文无关
                if(View.class.isAssignableFrom(fcls)){
                    if(field.isAnnotationPresent(Resource.class) && !Modifier.isStatic(field.getModifiers())){
                        field.setAccessible(true);
                        Resource resource=field.getAnnotation(Resource.class);
                        int rid=resource.value();
                        Method m=cls.getMethod("findViewById", int.class);
                        View v=(View) m.invoke(o, rid);
                        field.set(o, v);
                    }
                }
                //如果字段上有@Component注释，则依赖注入
                else if(field.isAnnotationPresent(Component.class) && !Modifier.isStatic(field.getModifiers())){
                    field.setAccessible(true);
                    Object fbean=BeanHelper.getBean(fcls);
                    field.set(o, fbean);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
