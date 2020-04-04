package com.noahedu.person.health.engine;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


/**
 * Created by 23061 on 2018/12/25.
 */

public class Engine {

	private static Engine sEngine = null;
	static{
		System.loadLibrary("personhealth");
	}
	

	private Engine() {
	}
	
	public static Engine getInstance() {
		if(sEngine == null) {
			sEngine = new Engine();
		}
		
		return sEngine;
	}
    public String GetContentCacheFile(Context ctx)
    {
    	String cachePath;
		File file = null;

		file = ctx.getCacheDir();

		if(file != null)
		{
			cachePath = file.getPath();
			return cachePath;
		}
		return null;    	
    }
    
    public Bitmap GetPictureLogo(int hd,String cacheFile,int logoAddr)
    {
    	File file=new File(cacheFile);  
    	Bitmap bitmap =  null;
    	if(!file.exists())    
    	{   
        	byte [] logo = null;
    		logo =  nativeGetPictureInfo(hd,cacheFile,logoAddr);
    		bitmap = BitmapFactory.decodeByteArray(logo, 0, logo.length);	      
    	}  
    	else
    	{

            bitmap=BitmapFactory.decodeFile(cacheFile);
    	}

        return bitmap;
    }
    /**
     * path为人格健康建议库的地址
     * 返回 hd 为nativeOpenPackages 打开的句柄
     * **/
    public static native int nativeOpenPackages(String path);//打开人格健康教育库，返回文件句柄id
    /**
     * hd 为nativeOpenPackages 打开的句柄
     * **/
    public static native void nativeClosePackages(int hd); //关闭人格健康教育库
    /**
     * hd 为nativeOpenPackages 打开的句柄
     * cachedir 是保存图片的缓冲区目录
     * **/
    public static native Content[] nativeReadContent(int hd,String cachedir); //获取各个类别的数据    
    
    /**
     * hd 为nativeOpenPackages 打开的句柄
     * cachefile 是Content 中 item 的cachefile,
     * addr为content item 的logoAddr
     * **/
    public static native byte[] nativeGetPictureInfo(int hd,String cachefile,int logoAddr);//根据地址获取图片
 
}
