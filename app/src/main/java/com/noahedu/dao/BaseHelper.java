package com.noahedu.dao;

import android.content.Context;

import com.noahedu.demo.R;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：BaseHelper$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/26$ 20:19$
 */
@DB(db_name="myTask.db",db_version=13,db_xml_id= R.xml.db)
public class BaseHelper extends DBHelper{
    public BaseHelper(Context context) {
        super(context);
    }
}
