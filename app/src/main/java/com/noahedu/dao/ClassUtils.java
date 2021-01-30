package com.noahedu.dao;

import java.io.File;
import java.io.FileFilter;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import com.noahedu.audiorecorder.base.MyApp;
import com.noahedu.common.util.LogUtils;
import dalvik.system.DexFile;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：ClassUtils$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/26$ 20:07$
 */
public class ClassUtils {
    private static final String TAG=ClassUtils.class.getSimpleName();

    public static ClassLoader getClassLoader(){
        //获得类加载器
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> loadClass(String className,boolean isInitialized){
        Class<?> cls = null;
        try{
            //加载类
            cls=Class.forName(className, isInitialized, getClassLoader());
        }catch(Exception e){
            e.printStackTrace();
        }
        return cls;
    }

    public static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classSet=new HashSet<Class<?>>();
        try{
            //Enumeration<URL> urls=getClassLoader().getResources(packageName.replaceAll("\\.", "/"));
            //获得app包环境，然后遍历加载类
            DexFile df = new DexFile(MyApp.getInstance().getPackageCodePath());
            Enumeration<String> urls=df.entries();
            while(urls.hasMoreElements()){
                String url=urls.nextElement();
                LogUtils.v(TAG,url);
                if(url!=null){
					/*
					String protocol=url.getProtocol();
					if(protocol.equals("file")){
					*/
                    //String packagePath=url.getPath().replaceAll("%20"," ");
                    if (url.contains(packageName)) {
                        addClass(classSet,url);
                    }
                    //String packagePath=url.replaceAll("%20"," ");
                    //addClass(classSet,packagePath,packageName);
					/*
					}else if(protocol.equals("jar")){
						JarURLConnection jarUrlConnection=(JarURLConnection) url.openConnection();
						if(jarUrlConnection!=null){
							JarFile jarFile=jarUrlConnection.getJarFile();
							if(jarFile!=null){
								Enumeration<JarEntry> jarEntries=jarFile.entries();
								while(jarEntries.hasMoreElements()){
									JarEntry jarEntry=jarEntries.nextElement();
									String jarEntryName=jarEntry.getName();
									if(jarEntryName.endsWith(".class")){
										String className=jarEntryName.substring(0,jarEntryName.lastIndexOf(".")).replaceAll(".", "/");
										doAddClass(classSet,className);
									}
								}
							}
						}
					}
					*/
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return classSet;
    }

    private static void addClass(Set<Class<?>> classSet,String className){
        doAddClass(classSet,className);
    }

    private static void addClass(Set<Class<?>> classSet,String packagePath,String packageName){
        File[] files=new File(packagePath).listFiles(new FileFilter(){

            @Override
            public boolean accept(File file) {
                return ((file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
            }

        });

        for(File file : files){
            if(file.isFile()){
                String className=file.getName().substring(0,file.getName().lastIndexOf("."));
                if(!packageName.isEmpty())
                    className=packageName+"."+className;
                doAddClass(classSet,className);
            }else{
                String subPackagePath=file.getName();
                String subPackageName=file.getName();
                if(!packagePath.isEmpty())
                    subPackagePath=packagePath+"/"+subPackagePath;
                if(!packageName.isEmpty())
                    subPackageName=packageName+"."+subPackageName;
                addClass(classSet,packagePath,packageName);
            }
        }
    }

    private static void doAddClass(Set<Class<?>> classSet, String className){
        Class<?> cls=loadClass(className,false);
        classSet.add(cls);
    }

}
