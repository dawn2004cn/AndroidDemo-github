/**
 * © 2019 www.youxuepai.com
 * @file name：PropertiesUtils.java
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020-9-7下午3:05:57
 * @version 1.0
 */
package com.noahedu.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 *  * © 2019 www.youxuepai.com
 *  名称：PropertiesUtils
 *  描述：简单描述该类的作用
 * @class name：PropertiesUtils
 * @anthor : daisg
 * @time 2020-9-7下午3:05:57
 * @version V1.0
 */
public class PropertiesUtils {
	private final static String TAG = PropertiesUtils.class.getSimpleName();

	private String mPath;
	private String mFile;
	private Properties mProp;
	private static PropertiesUtils mPropUtil = null;
	
	public static PropertiesUtils getInstance() {
		if (mPropUtil == null) {
			mPropUtil = new PropertiesUtils();
			mPropUtil.mPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/tmp";
			mPropUtil.mFile = "countly.ini";
		}
		return mPropUtil;
	}
	
	public PropertiesUtils setPath(String path) {
		mPath = path;
		return this;
	}
 
	public PropertiesUtils setFile(String file) {
		mFile = file;
		return this;
	}
	
	public PropertiesUtils init() {
		Log.d(TAG, "path="+mPath+"/"+mFile);
		try {
			File dir = new File(mPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, mFile);
			if (!file.exists()) {
				file.createNewFile();
			}
			InputStream is = new FileInputStream(file);
			mProp = new Properties();
			if (mProp != null){
				mProp.load(is);
			}
			else{
				Log.d(TAG, "init: mProp is null");
			}
				
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public void commit() {
		try {
			File file = new File(mPath + "/" + mFile);
			OutputStream os = new FileOutputStream(file);
			if (mProp != null) {
				mProp.store(os, "");
			}
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(mProp != null) {
			mProp.clear();
		}
	}
 
	public void clear() {
		if (mProp != null){
			mProp.clear();
		}
	}
	
	public void open() {
		if (mProp != null){
			mProp.clear();
		}
		try {
			File file = new File(mPath + "/" + mFile);
			InputStream is = new FileInputStream(file);
			mProp = new Properties();
			if (mProp != null) {
				mProp.load(is);
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public void writeString(String name, String value) {
		if (mProp != null){
			mProp.setProperty(name, value);
		}
    }
 
    public String readString(String name, String defaultValue) {
		if (mProp != null){
			return mProp.getProperty(name, defaultValue);
		}
		return defaultValue;
    }
 
    public void writeInt(String name, int value) {
		if (mProp != null){
			mProp.setProperty(name, ""+value);
		}
    }
 
    public int readInt(String name, int defaultValue) {
		if (mProp != null){
			return Integer.parseInt(mProp.getProperty(name, ""+defaultValue));
		}
		return  defaultValue;
    }
 
    public void writeBoolean(String name, boolean value) {
		if (mProp != null) {
			mProp.setProperty(name, "" + value);
		}
    }
 
    public boolean readBoolean(String name, boolean defaultValue) {
		if (mProp != null) {
			return Boolean.parseBoolean(mProp.getProperty(name, "" + defaultValue));
		}
		return  defaultValue;
    }
 
    public void writeDouble(String name, double value) {
		if (mProp != null) {
			mProp.setProperty(name, "" + value);
		}
    }
 
    public double readDouble(String name, double defaultValue) {
		if (mProp != null) {
			return Double.parseDouble(mProp.getProperty(name, "" + defaultValue));
		}
		return  defaultValue;
    }

    /*
     * 读
     * PropertiesUtil mProp = PropertiesUtil.getInstance(this).init();
			mProp.open();
			String name = mProp.readString("name", "");
			int age = mProp.readInt("age", 0);
			boolean married = mProp.readBoolean("married", false);
			double weight = mProp.readDouble("weight", 0f);
			String time = mProp.readString("time", "");
			
			
			写
			PropertiesUtil mProp = PropertiesUtil.getInstance(this).init();
			mProp.writeString("name", "Mr Lee");
			mProp.writeInt("age", (int)(Math.random()*100%100));
			mProp.writeBoolean("married", true);
			mProp.writeDouble("weight", 100f);
			mProp.writeString("time", Utils.getNowDateTime());
			mProp.commit();*/
}
