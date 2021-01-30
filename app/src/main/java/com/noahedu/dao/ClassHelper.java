package com.noahedu.dao;

import android.app.Application;
import android.content.res.AssetManager;

import com.noahedu.audiorecorder.base.MyApp;
import com.noahedu.common.util.LogUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：ClassHelper$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/26$ 20:09$
 */
public class ClassHelper {
    private static final String TAG="ClassHelper";

    private static final String BASE_PACKAGE="com.noahedu.demo";
    private static final String SCAN_PACKAGE="scan";
    //类集合
    private static Set<Class<?>> CLASS_SET=new HashSet<Class<?>>();

    static{
        LogUtils.d(TAG, "class scan");
        //assets文件夹
        AssetManager assets = MyApp.getInstance().getAssets();
        InputStream input=null;
        try {
            //获得配置文件
            input=assets.open("config.propertites");
            Properties p=new Properties();
            p.load(new BufferedInputStream(input));
            String scanPackage=(String) p.get(SCAN_PACKAGE);
            String[] ps=scanPackage.split(",");
            for(String sp : ps){
                //加载类
                CLASS_SET.addAll(ClassUtils.getClassSet(sp));
            }

            //加载类
            CLASS_SET.add(ClassUtils.loadClass(DaoHelper.class.getName(), false));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Set<Class<?>> getClassSet(){
        return CLASS_SET;
    }

    public static Set<Class<?>> getComponent(){
        Set<Class<?>> classSet=new HashSet<Class<?>>();
        for(Class<?> cls : CLASS_SET){
            //判断类上是否有Component注释
            if(cls.isAnnotationPresent(Component.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    //与本文无关
    private static Set<Class<?>> getDB(){
        Set<Class<?>> classSet=new HashSet<Class<?>>();
        for(Class<?> cls : CLASS_SET){
            if(cls.isAnnotationPresent(DB.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    //与本文无关
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> classSet=getComponent();
        classSet.addAll(getDB());
        return classSet;
    }

}
