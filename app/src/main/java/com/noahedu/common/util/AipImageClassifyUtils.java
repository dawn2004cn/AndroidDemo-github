/**
 * © 2019 www.youxuepai.com
 * @file name：AipImageClassifyUtils.java.java
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2019-6-26上午8:57:46
 * @version 1.0
 */
package com.noahedu.common.util;

import java.util.HashMap;

import org.json.JSONObject;

import com.baidu.aip.imageclassify.AipImageClassify;

/**
 *  * © 2019 www.youxuepai.com
 *  名称：AipImageClassifyUtils
 *  描述：简单描述该类的作用
 * @class name：AipImageClassifyUtils
 * @anthor : daisg
 * @time 2019-6-26上午8:57:46
 * @version V1.0
 */
public class AipImageClassifyUtils {
	
	public final static String API_KEY=  "MfWtEAzEtfgwxSYkBEhqD83V";
	public final static String SECRET_KEY = "bGX1qyam00sZmD70MmY7vImlZplnPOd9";
	public final static String APP_ID = "16637658";
	
	private static AipImageClassify client = null;
	
		
	private AipImageClassifyUtils()
	{
		
	}
	
	public static AipImageClassify getInstance(){
		if (client == null)
		{
			   // 初始化一个AipImageClassify
		    AipImageClassify client = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);

		    // 可选：设置网络连接参数
		    client.setConnectionTimeoutInMillis(2000);
		    client.setSocketTimeoutInMillis(60000);

		    // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
		    //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
		    //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
			
		}
		
		return client;		
	}
	
	public static JSONObject detectObject(String path)
	{
	    // 传入可选参数调用接口
	    HashMap<String, String> options = new HashMap<String, String>();
	    options.put("baike_num", "5");
		JSONObject res = getInstance().objectDetect(path, options);
		return res;
	}
	
	public static JSONObject detectObject(byte[] image)
	{
	    // 传入可选参数调用接口
	    HashMap<String, String> options = new HashMap<String, String>();
	    options.put("baike_num", "5");
		JSONObject res = getInstance().objectDetect(image, options);
		return res;
	}
	
	
	   public static void main(String[] args){
		   detectObject("D:\\project\\codeject\\imageprocess\\android-image-filter-master\\android-image-filter-master\\screenshot\\img1.png");
	    }
}
