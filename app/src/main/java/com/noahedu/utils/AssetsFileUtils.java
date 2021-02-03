package com.noahedu.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.noahedu.common.filedownloader.base.Log;
import com.noahedu.demo.utils.DingDataBean;
import com.noahedu.demo.utils.XQEBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：AssetsFileUtils$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/1/9$ 14:06$
 */
public class AssetsFileUtils {
    public static List<XQEBean> ReadTxtFileFromAsset(Context context, String assetsPath)
    {
        List<XQEBean> newList=new ArrayList<XQEBean>();
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(assetsPath);
            if (is != null)
            {
                InputStreamReader inputreader = new InputStreamReader(is);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //分行读取
                while (( line = buffreader.readLine()) != null) {
                    //newList.add(line+"\n");
                    Gson gson = new Gson() ;
                    Log.v("ii",line+"\n");
                    XQEBean xqeBean = gson.fromJson(line.replace("\\xa0",""),XQEBean.class);
                    newList.add(xqeBean);
                }
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newList;
    }

    public static List<DingDataBean.DataBean> ReadJsonFileFromAsset(Context context, String assetsPath)
    {
        List<DingDataBean.DataBean> newList=new ArrayList<DingDataBean.DataBean>();
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(assetsPath);
            if (is != null)
            {
                InputStreamReader inputreader = new InputStreamReader(is);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                String message = "";
                //分行读取
                while (( line = buffreader.readLine()) != null) {
                    //newList.add(line+"\n");
                    message += line;
                }
                is.close();


                Gson gson = new Gson() ;
                Log.v("ii",message+"\n");
                DingDataBean xqeBean = gson.fromJson(message.replace("\\xa0",""),DingDataBean.class);
                newList.addAll(xqeBean.getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newList;
    }
}
