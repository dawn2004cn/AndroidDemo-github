package com.count.android.api;

/**
 * CountlyUtils
 * 
 * @author daisg 2016-3-30
 */
public class CountlyUtils {

	/**
	 * 升级事件的类型*/
	public static final String UPDATE_TYPE_OTA =  "OTA_UPDATE";
	public static final String UPDATE_TYPE_APP =  "APP_UPDATE";
	public static final String UPDATE_TYPE_FULL = "FULL_UPDATE";
	
	
	/**
	 * 搜索事件的类型*/
	public static final String SEARCH_TYPE_VIDEO   =  "语音";
	public static final String SEARCH_TYPE_PICTURE =  "图片";
	public static final String SEARCH_TYPE_CONTENT =  "文字";	
	

	/**
	 * 做题事件的正确标记类型*/
	public static final int QUESTION_CORRECT_FLAG_CORRECT     =  1;//正确
	public static final int QUESTION_CORRECT_FLAG_ERROR       =  2;//错误
	public static final int QUESTION_CORRECT_FLAG_MIDCORRECT  =  3;//半对
	public static final int QUESTION_CORRECT_FLAG_OTHER 	  =  4;//主观题or其他


	/**
	 * 播放事件的类型*/
	public static final String PLAY_RES_VIDEO     				 =  "视频";
	public static final String PLAY_RES_FLASH      				 =  "动漫";
	public static final String PLAY_RES_AUDIO  					 =  "音频";
	
	/**
	 * 访问资源的类型  暂时不确定，是否会有，先定义
	 * */
	public static final String VISIT_RES_RECOMMEND     				 =  "推荐";
	public static final String VISIT_RES_PUSH      					 =  "推送";
	public static final String VISIT_RES_FAVORITE  					 =  "收藏";
	public static final String VISIT_RES_SEARCH  					 =  "搜索";
	public static final String VISIT_RES_SHARE  					 =  "分享";
	public static final String VISIT_RES_DEFAULT  					 =  "默认";	
	
}
