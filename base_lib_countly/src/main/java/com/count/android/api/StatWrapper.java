package com.count.android.api;

import java.util.Map;

import android.content.Context;

public class StatWrapper {	

	/**设置log是否打开默认不打开*/
	public static void setLoggingEnabled(boolean enableLogging)
	{
		Countly.sharedInstance().setLoggingEnabled(enableLogging);
	}
	
	/**设置异常统计打开默认不打开*/
	public static void setCatchUncaughtExceptions(boolean enableCaught)
	{
		Countly.sharedInstance().setCatchUncaughtExceptions(enableCaught);
	}
	
	/**在activity或者application 的onCreate中调用初始化函数,appkey 在meta中定义*/
	public static void init(final Context context){
		if(context == null)
		{
			throw new IllegalStateException("Countly.sharedInstance().init  contet is null");			
		}
		Context ctx = context.getApplicationContext();
		String appID = Countly.sharedInstance().readApplicationInfo(ctx);
    	/**
    	 * 此处调用初始化代码
    	 */
    	Countly.sharedInstance().init(ctx, DeviceInfo.decodeSeverUrl(), appID);	
	}
	
	/**在activity或者application 的onCreate中调用初始化函数，appkey由AP开发者在网站申请*/
	public static void init(final Context context, final String appKey){

		Countly.sharedInstance().readApplicationInfo(context);

    	/**
    	 * 此处调用初始化代码
    	 */
    	Countly.sharedInstance().init(context, DeviceInfo.decodeSeverUrl(), appKey);	  
	}
	
	/**在activity或者application 的onCreate中调用初始化函数，serverURL为服务器上传网址，appkey由AP开发者在网站申请*/
	public static void init(final Context context, final String serverURL, final String appKey){

		Countly.sharedInstance().readApplicationInfo(context);
		/**
		 * 此处调用初始化代码
		 */
		Countly.sharedInstance().init(context, serverURL, appKey);
	}
	
	public static void onResume(final Context context) {

		/**
		 * 此处调用基本统计代码
		 */
		//Countly.sharedInstance().onStart(context);
	}

	public static void onPause(final Context context) {

		/**
		 * 此处调用基本统计代码
		 */
		//Countly.sharedInstance().onStop(context);
	}
	
	 public static void onEvent(final Context context,final String key){
		/**
		 * 此处调用基本统计代码
		 */
		Countly.sharedInstance().recordEvent(context,key);
	 }
	 
	 public static void onEvent(final Context context,final String key, final int count){
		/**
		 * 此处调用基本统计代码
		 */
		Countly.sharedInstance().recordEvent(context,key,count);
	 }
	 
	 public static void onEvent(final Context context,final String key, final int count, final double sum){
		/**
		 * 此处调用基本统计代码
		 */
		Countly.sharedInstance().recordEvent(context,key,count,sum);
	 }

	 public static void onEvent(final Context context,final String key, final Map<String, String> segmentation, final int count){
		/**
		 * 此处调用基本统计代码
		 */
		Countly.sharedInstance().recordEvent(context,key,segmentation,count);
	 }

	 public static void onEvent(final Context context,final String key, final Map<String, String> segmentation, final int count, final double sum){
		/**
		 * 此处调用基本统计代码
		 */
		Countly.sharedInstance().recordEvent(context,key,segmentation,count,sum);
	 }
	 
	 public static void onEvent(final Context context,final String key, final Map<String, String> segmentation, final int count, final double sum,final boolean delay){
		/**
		 * 此处调用基本统计代码
		 */
		Countly.sharedInstance().recordEvent(context,key,segmentation,count,sum,delay);
	 }
	 
	 public static void onError(final Context context,final String error)
	 {
		 /**
		  * 此处调用基本统计代码
		  */
		Countly.sharedInstance().recordError(context,error);
		 
	 }
	 public static void onError(final Context context,final Throwable e)
	 {
		 /**
		  * 此处调用基本统计代码
		  */
		Countly.sharedInstance().recordError(context,e);
	 }
	    /**
	     * 此函数定义下载事件.
	     * @param resName 为资源的名称
	     * @param resType 为资源的类型
	     * @param resId 为资源ID
	     * @param resSuffix 为资源的后缀
	     * @param netType 为下载资源使用的网络类型
	     * @throws IllegalStateException if Countly SDK has not been initialized
	     * @throws IllegalArgumentException if key is null or empty
	     */
	 public static void onDownloadRes(final Context context,final String resName,final String resType,final String resId,final String resSuffix,final String netType)
	 {
		 /**
		  * 此处调用基本统计代码
		  */
		Countly.sharedInstance().recordDownload(context,resName,resType,resId,resSuffix,netType);
	 }
	 
	    /**
	     * 此函数定义资源访问事件.
	     * @param resName 为资源的名称
	     * @param resType 为资源的类型
	     * @param resId 为资源ID
	     * @param resName 为资源的来源
	     * @throws IllegalStateException if Countly SDK has not been initialized
	     * @throws IllegalArgumentException if key is null or empty
	     */
	 public static void onVisitRes(final Context context,final String resName,final String resType,final String resId,final String resFrom)
	 {
		 /**
		  * 此处调用基本统计代码
		  */
		Countly.sharedInstance().recordVisit(context,resName,resType,resId,resFrom);
	 }
	 
	 /**
	     * 此函数定义开机事件.
	     * @throws IllegalStateException if Countly SDK has not been initialized
	     * @throws IllegalArgumentException if key is null or empty
	     */
	 public static void onPowerOn(final Context context)
	 {
		 /**
		  * 此处调用基本统计代码
		  */
		Countly.sharedInstance().recordPowerOn(context);
	 } 
	 /**
	     * 此函数定义app启动事件.
	     * @param packageName 为应用的包名
	     * @param activityName 为应用启动activity的名称
	     * @param appName 为应用的名称
	     * @param start 为启动时间
	     * @param end 为结束时间
	     * @param duration 为ap停留时间
	     * @throws IllegalStateException if Countly SDK has not been initialized
	     * @throws IllegalArgumentException if key is null or empty
	     */
	 public static void onAppDuration(final Context context,String packageName,String activityName,String appName,long start,long end,long duration)
	 {
		 /**
		  * 此处调用基本统计代码
		  */
		Countly.sharedInstance().recordAppDuration(context,packageName,activityName,appName,start,end,duration);
	 }
	 /**
	     * 此函数定义app启动事件.
	     * @param packageName 为应用的包名
	     * @param activityName 为应用启动activity的名称
	     * @param appName 为应用的名称
	     * @param start 为启动时间
	     * @param end 为结束时间
	     * @param duration 为ap停留时间
	     * @param unlock 为结束时间
	     * @throws IllegalStateException if Countly SDK has not been initialized
	     * @throws IllegalArgumentException if key is null or empty
	     */
	 public static void onAppDuration(final Context context,String packageName,String activityName,String appName,long start,long end,long duration,long unlock)
	 {
		 /**
		  * 此处调用基本统计代码
		  */
		Countly.sharedInstance().recordAppDuration(context,packageName,activityName,appName,start,end,duration,unlock);
	 }
	 
	 /**
	     * 此函数定义app搜索事件.
	     * @param content 为搜索的关键字
	     * @param type 为搜索的类型
	     * @throws IllegalStateException if Countly SDK has not been initialized
	     * @throws IllegalArgumentException if key is null or empty
	     */
	 public static void onSearchEvent(final Context context,String content,String type)
	 {
		 /**
		  * 此处调用基本统计代码
		  */
		Countly.sharedInstance().recordSearchEvent(context,content,type);
	 }
	 /**
	     * 此函数定义app搜索事件.
	     * @param content 为点击事件的内容
	     * @throws IllegalStateException if Countly SDK has not been initialized
	     * @throws IllegalArgumentException if key is null or empty
	     */
	 public static void onClickEvent(final Context context,String content)
	 {
		 /**
		  * 此处调用基本统计代码
		  */
		Countly.sharedInstance().recordClickEvent(context,content);
	 }
	 /**
	     * 此函数定义版本升级事件.
	     * @param pre_version 为升级前版本
	     * @param cur_version 为升级后版本
	     * @param type 为升级类型
	     * @throws IllegalStateException if Countly SDK has not been initialized
	     * @throws IllegalArgumentException if key is null or empty
	     */
	 public static void onUpgradeEvent(final Context context,String pre_version,String cur_version, String type)
	 {
		 /**
		  * 此处调用基本统计代码
		  */
		Countly.sharedInstance().recordUpgradeEvent(context,pre_version,cur_version,type);
	 }
	 
	 /**
	     * 此函数定义做题事件.
	     * @param questionId 为试题ID
	     * @param correctFlag 为答题结果
	     * @param duration 为本题做题时间
	     * @param errorReason 为错误原因
	     * @param resFrom 为试题来源与那个AP
	     * @param resSubject 为试题对应的科目
	     * @param resGrade 为试题对应的年级
	     * @throws IllegalStateException if Countly SDK has not been initialized
	     * @throws IllegalArgumentException if key is null or empty
	     */
	 public static void onDoQuestionEvent(final Context context,String questionId,  
			 int correctFlag,long duration,String errorReason,String resFrom,int resGrade,int resSubject)
	 {
		 /**
		  * 此处调用基本统计代码
		  */
		Countly.sharedInstance().recordDoQuestionEvent(context,questionId,correctFlag,duration,errorReason,resFrom,resGrade,resSubject);
	 } 
	 /**
	     * 此函数定义播放事件.
	     * @param resName 为资源名称
	     * @param resType 为资源类型
	     * @param resId 为资源ID
	     * @param resFrom 为资源来源
	     * @param start 为播放开始时间
	     * @param end 为播放结束时间
	     * @param duration 为总播放时间
	     * @param finish 为完成率
	     * @throws IllegalStateException if Countly SDK has not been initialized
	     * @throws IllegalArgumentException if key is null or empty
	     */
	 public static void onPlayResEvent(final Context context,
			 final String resName,final String resType,final String resId,final String resFrom,  
			 long start,long end,long duration,long total)
	 {
		 /**
		  * 此处调用基本统计代码
		  */
		Countly.sharedInstance().recordPlayEvent(context,resName,resType,resId,resFrom,start,end,duration,total);
	 }
	 /**
	     * 此函数定义打电话事件.
	     * @throws IllegalStateException if Countly SDK has not been initialized
	     * @throws IllegalArgumentException if key is null or empty
	     */
	 public static void onPhoneEvent(final Context context)
	 {
		 /**
		  * 此处调用基本统计代码
		  */
		Countly.sharedInstance().recordPhoneEvent(context);
	 } 
	 /**
	     * 此函数定义流量使用事件.
	     * @throws IllegalStateException if Countly SDK has not been initialized
	     * @throws IllegalArgumentException if key is null or empty
	     */
	 public static void onDataPlanEvent(final Context context,String data)
	 {
		 /**
		  * 此处调用基本统计代码
		  */
		Countly.sharedInstance().recordDataPlanEvent(context,data);
	 }
	 /**
	     * 此函数定义机器使用位置事件.
	     * @param province_name 省
	     * @param city_name 市
	     * @param distinct_name 区
	     * @param address 位置
	     * @param longitude 经度
	     * @param latitude 维度
	     * @throws IllegalStateException if Countly SDK has not been initialized
	     * @throws IllegalArgumentException if key is null or empty
	     */
	 public static void onPositionEvent(final Context context,String province_name,String city_name,
			 	String distinct_name,String address,String longitude,String latitude)
	 {
		 /**
		  * 此处调用基本统计代码
		  */
		Countly.sharedInstance().recordPositionEvent(context,province_name,city_name,distinct_name,address,longitude,latitude);
	 }
	 
	 /**
	     * 此函数拍书事件.
	     * @param time 拍书耗时
	     * @param province_name 省
	     * @param city_name 市
	     * @param distinct_name 区
	     * @param address 位置
	     * @param result 拍书结果 0 未结果，1 有结果
	     * @throws IllegalStateException if Countly SDK has not been initialized
	     * @throws IllegalArgumentException if key is null or empty
	     */
	 public static void onSearchBookEvent(final Context context,String province_name,String city_name,
			 	String distinct_name,String address,int result,int time)
	 {
		 /**
		  * 此处调用基本统计代码
		  */
		Countly.sharedInstance().recordSearchBookEvent(context,province_name,city_name,distinct_name,address,result,time);
	 }
}
