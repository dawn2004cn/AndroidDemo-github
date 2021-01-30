package com.noahedu.dao;

import android.app.Application;
import android.content.Context;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：ApplicationSelf$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/26$ 20:16$
 */
public class ApplicationSelf extends Application {
    public static Context context;

    @Override
    public void onCreate(){
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
