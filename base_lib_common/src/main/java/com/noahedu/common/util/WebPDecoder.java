/**
 * © 2019 www.youxuepai.com
 * @file name：WebPDecoder.java.java
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2019-11-8下午1:47:38
 * @version 1.0
 */
package com.noahedu.common.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

/**
 *  * © 2019 www.youxuepai.com
 *  名称：WebPDecoder
 *  描述：简单描述该类的作用
 * @class name：WebPDecoder
 * @anthor : daisg
 * @time 2019-11-8下午1:47:38
 * @version V1.0
 */
public class WebPDecoder {
	 private static WebPDecoder instance = null;

	    private WebPDecoder() {
	        System.loadLibrary("webp_evme");
	    }

	    public static WebPDecoder getInstance() {
	        if (instance==null) {
	            synchronized (WebPDecoder.class) {
	                if (instance==null) {
	                    instance = new WebPDecoder();
	                }
	            }
	        }

	        return instance;
	    }

	    public static void main(String args[]) {
	        String string = "http://ximalayaos.cos.xmcdn.com/storages/d717-ximalayaos/08/14/CMCoC10BfI3vAA7pQgABL_Mg.mp3?sign=1572255064-uhy1vywzo-0-76113b2b6b4dafbed44a4ae5dc43457a";  
	        //查找指定字符是在字符串中的下标。在则返回所在字符串下标；不在则返回-1.  
			LogUtils.v(string.indexOf("?")+""); // indexOf(String str); 返回结果：-1，"b"不存在
	 
	        int ind = string.indexOf("?") ;
			LogUtils.v(string.substring(0, ind));
	        // 从第四个字符位置开始往后继续查找，包含当前位置  
			LogUtils.v(string.indexOf("a",3)+"");//indexOf(String str, int fromIndex); 返回结果：6
	 
	        //（与之前的差别：上面的参数是 String 类型，下面的参数是 int 类型）参考数据：a-97,b-98,c-99  
	 
	        // 从头开始查找是否存在指定的字符  
			LogUtils.v(string.indexOf(99)+"");//indexOf(int ch)；返回结果：7
			LogUtils.v(string.indexOf('c')+"");//indexOf(int ch)；返回结果：7
	 
	        //从fromIndex查找ch，这个是字符型变量，不是字符串。字符a对应的数字就是97。  
			LogUtils.v(string.indexOf(97,3)+"");//indexOf(int ch, int fromIndex); 返回结果：6
			LogUtils.v(string.indexOf('a',3)+"");//indexOf(int ch, int fromIndex); 返回结果：6
	    }
	    
}
