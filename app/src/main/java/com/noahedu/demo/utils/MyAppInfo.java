/**
 * © 2019 www.youxuepai.com
 * @file name：MyAppInfo.java.java
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020-1-8下午2:37:57
 * @version 1.0
 */
package com.noahedu.demo.utils;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 *  * © 2019 www.youxuepai.com
 *  名称：MyAppInfo
 *  描述：简单描述该类的作用
 * @class name：MyAppInfo
 * @anthor : daisg
 * @time 2020-1-8下午2:37:57
 * @version V1.0
 */
public class MyAppInfo {

	    private String appName;
	    private String packageName;
	    private String activityName;
		 public String versionName="";
		 public int versionCode=0;
		 public Drawable appIcon=null;
		 public Intent launchIntent = null;
	 
	    public MyAppInfo(Drawable appIcon, String appName) {
	        this.appIcon = appIcon;
	        this.appName = appName;
	    }
	    public MyAppInfo() {
	 
	    }
	 
	 
	    public String getAppName() {
	        return appName;
	    }
	 
	    public void setAppName(String appName) {
	        this.appName = appName;
	    }
		/**
		 * @return the packageName
		 */
		public String getPackageName() {
			return packageName;
		}
		/**
		 * @param packageName the packageName to set
		 */
		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}
		/**
		 * @return the activityName
		 */
		public String getActivityName() {
			return activityName;
		}
		/**
		 * @param activityName the activityName to set
		 */
		public void setActivityName(String activityName) {
			this.activityName = activityName;
		}
		/**
		 * @return the versionName
		 */
		public String getVersionName() {
			return versionName;
		}
		/**
		 * @param versionName the versionName to set
		 */
		public void setVersionName(String versionName) {
			this.versionName = versionName;
		}
		/**
		 * @return the versionCode
		 */
		public int getVersionCode() {
			return versionCode;
		}
		/**
		 * @param versionCode the versionCode to set
		 */
		public void setVersionCode(int versionCode) {
			this.versionCode = versionCode;
		}
		/**
		 * @return the appIcon
		 */
		public Drawable getAppIcon() {
			return appIcon;
		}
		/**
		 * @param appIcon the appIcon to set
		 */
		public void setAppIcon(Drawable appIcon) {
			this.appIcon = appIcon;
		}
		/**
		 * @return the launchIntent
		 */
		public Intent getLaunchIntent() {
			return launchIntent;
		}
		/**
		 * @param launchIntent the launchIntent to set
		 */
		public void setLaunchIntent(Intent launchIntent) {
			this.launchIntent = launchIntent;
		}
	    
}
