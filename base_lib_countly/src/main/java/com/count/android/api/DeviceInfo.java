/*
Copyright (c) 2012, 2013, 2014 Countly

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.count.android.api;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import com.count.android.cache.PropertyUtils;
/**
 * This class provides several static methods to retrieve information about
 * the current device and operating environment.
 *
 * It is important to call setDeviceID early, before logging any session or custom
 * event data.
 */
class DeviceInfo {	
	public static String buildPath = "/system/";
	
    private static String deviceID_;
    
    private static String productID_ = null;
    
    private static String cpuName_ = null;
    
    private static String ipAddress_;    
    private static String city_;    
    private static String netSSID_;    
    private static String netMac_;    
    private static String localMac_;    
    
    private static String build_number = null;

	//String server = "http://hd.noahedu.com/hadRecord/record"; 
    //加密字串
    private static String server = "fKjW1FDE5Jr1mRb2hdCO0eR14yW1m0Jp3jhNToCi74i+MioB0B3Mew==";

    private static String secretKey = "37d5aed075525d4fa0fe635231cba447";
    
    private static String server_debug = "http://59.42.255.238:8081/hadRecord/record";
    private static String noah_debug = "http://192.168.8.244:8089/hadRecord/record";
    private static String server_release = "http://hd.noahedu.com/hadRecord/record";
    

	public static final String NOAHMODEL = "ro.product.noahmodel";
    private static boolean debug_test = false;
    
    //用户中心uri 
	public static final String TNAME = "personalinfo";
	public static final String AUTOHORITY = "com.noahedu.provider.personalinfo";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTOHORITY + "/" + TNAME);
	

	public static final String EDU_STAGE = "edu_stage";
	public static final String GRADE = "grade_id";
	private final static String PUSERID = "uid"; 
	private final static String ROLEID = "roleid"; 
	private final static String TEMP_ROLEID = "temp_roleid"; 
    private static final String TYPE = "type"; //
	
	private final static String Mode_Launcher = "mode_Launcher";  //主入口模式
	private final static String Mode_Explicit = "mode_Explicit";  //明确调用  ----ap内部调用
	private final static String Mode_Implicit = "mode_Implicit";  //隐式调用  --- ap之间调用
	

	private final static String COM_NOAHEDU_SYSTEM_NAME = "com.noahedu.system.SystemLibrary";
    /**
     * Returns the device ID set by the last call to setDeviceID.
     * It is important to set this to a non-null value that is unique per
     * device amongst all of an app's users.
     * @return the device ID set by the last call to setDeviceID, or OpenUDID no device ID has been provided at app startup
     */
    static String getDeviceID() {
        return deviceID_ == null ? OpenUDIDAdapter.getOpenUDID() : deviceID_;
    }

    static String getIPAddress() {
        return ipAddress_ == null ? IpGetter_manager.getOpenIpAddress() : ipAddress_;
    }
    static String getCity() {
        return city_ == null ? IpGetter_manager.getOpenCity() : city_;
    }
    static String getNetSSID() {
        return netSSID_ == null ? IpGetter_manager.getNetSSID() : netSSID_;
    }
    static String getNetMac() {
        return netMac_ == null ? IpGetter_manager.getNetMac() : netMac_;
    }
    static String getLocalMac() {
        return localMac_ == null ? IpGetter_manager.getLocalMac() : localMac_;
    }
    
    static String getNetSSID(Context ctx) {
    	
        if(netSSID_ == null ||netSSID_.equals("0x0"))
        {
			WifiManager mWifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);

			if (mWifi.isWifiEnabled()) {
				WifiInfo wifiInfo = mWifi.getConnectionInfo();
				netSSID_ = wifiInfo.getSSID(); //获取被连接网络的名称
			}
        }
        return netSSID_;
    }
    static String getNetMac(Context ctx) {

        if(netMac_ == null ||netMac_.equals("00:00:00:00:00:00"))
        {
			WifiManager mWifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);

			if (mWifi.isWifiEnabled()) {
				WifiInfo wifiInfo = mWifi.getConnectionInfo();
				netMac_ =  wifiInfo.getBSSID(); //获取被连接网络的mac地址
			}
        }
        return netMac_;
    }
    static String getLocalMac(Context ctx) {
		WifiManager mWifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);

        if(localMac_ == null ||localMac_.equals("00:00:00:00:00:00"))
		if (mWifi.isWifiEnabled()) {
			WifiInfo wifiInfo = mWifi.getConnectionInfo();
			localMac_ = wifiInfo.getMacAddress();// 获得本机的MAC地址  
		}
        return localMac_;
    }
    /**
     * Helper method for null safe comparison of current device ID and the one supplied to Countly.init
     * @return true if supplied device ID equal to the one registered before
     */
    static boolean deviceIPEqualsNullSafe(final String id) {
        final String deviceIp = ipAddress_ == null ? IpGetter_manager.getOpenIpAddress() : ipAddress_;
        return (deviceIp == null && id == null) || (deviceIp != null && deviceIp.equals(id));
    }
    /**
     * Sets the device ID that will be used in all data submissions to a Count.ly server.
     * It is important to set this to a non-null value that is unique per
     * device amongst all of an app's users.
     * @param deviceID unique ID representing the device that the app is running on
     */
    static void setDeviceID(final String deviceID) {
        deviceID_ = deviceID;
    }
  
    static String decodeSeverUrl()
    {
		DESEncrypt des = new DESEncrypt();
		String serverUrl = null;
		int flag = Countly.sharedInstance().getEnableDebug();
		if(flag == 1)
		{
			serverUrl = server_debug;
		}
		else if(Countly.sharedInstance().getNoahDebug() || flag == 2)
		{
			serverUrl = noah_debug;
		}
		else if(flag == 0)
		{
			serverUrl = server_release;
			try {
				serverUrl = des.decryption(server, secretKey);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
		
		return serverUrl;
    }
    /**
     * Helper method for null safe comparison of current device ID and the one supplied to Countly.init
     * @return true if supplied device ID equal to the one registered before
     */
    static boolean deviceIDEqualsNullSafe(final String id) {
        final String deviceId = deviceID_ == null ? OpenUDIDAdapter.getOpenUDID() : deviceID_;
        return (deviceId == null && id == null) || (deviceId != null && deviceId.equals(id));
    }

    /**
     * Returns the display name of the current operating system.
     */
    static String getOS() {
        return "Android";
    }

    /**
     * Returns the current operating system version as a displayable string.
     */
    static String getOSVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * Returns the current device.
     */
    static String getDevice() {
        return android.os.Build.DEVICE;
    }
    
    /**
     * Returns the current device.
     */
    static String getProductID() {
 		//String strProductId = getDeviceID();    
    	if(productID_ == null)
    	{
        	try {
        	            final Class<?> cls = Class.forName(COM_NOAHEDU_SYSTEM_NAME);
        	            final Method isInitializedMethod = cls.getMethod("getProductID", (Class[]) null);
        	            Object result = null;
    					try {
    						result = isInitializedMethod.invoke(null, (Object[]) null);
    					} catch (IllegalAccessException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					} catch (IllegalArgumentException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
        	            if (result instanceof String) {
        	            	productID_ = (String) result;
        	            }
        	   }
        	   catch (ClassNotFoundException ignored) { 
        	   }
        	   catch (NoSuchMethodException ignored) {
        	   }
        	   catch (InvocationTargetException ignored) {
        	   }
    	}
    	
        return productID_;
    }
    /**
     * Returns the current device model.
     */
    static String getDeviceModel() {
    	 String devName = android.os.Build.MODEL;
    	 String result = devName;
    	/*if(devName.equals("U20"))
    	{  
    		int type = NoahHardware.getPenDriveType();
    		if(type == NoahHardware.PENDRIVETYPE_INITIATIVE)
    		{
    			result = devName+"豪华版";
    		}
    	}
    	else if(devName.equals("NOAH_V1"))
    	{
    		result = PropertyUtils.getString(NOAHMODEL,"");
    		if(result.equals("V1"))
    		{
    			result = devName;
    		}
    	}*/
    	 try {
             result = java.net.URLEncoder.encode(result, "UTF-8");
         } catch (UnsupportedEncodingException ignored) {
             // should never happen because Android guarantees UTF-8 support
         }
        return result;
    }
    
    /**
     * Returns the current device board.
     */
    static String getDeviceBoard() {
        return android.os.Build.BOARD;
    }
    /**
     * Returns the current device brand.
     */
    static String getDeviceBrand() {
        return android.os.Build.BRAND;
    } 
    /**
     * Returns the current device Manufacturer.
     */
    static String getDeviceManufacturer() {
        return android.os.Build.MANUFACTURER;
    }
    /**
     * Returns the non-scaled pixel resolution of the current default display being used by the
     * WindowManager in the specified context.
     * @param context context to use to retrieve the current WindowManager
     * @return a string in the format "WxH", or the empty string "" if resolution cannot be determined
     */
    static String getResolution(final Context context) {
        // user reported NPE in this method; that means either getSystemService or getDefaultDisplay
        // were returning null, even though the documentation doesn't say they should do so; so now
        // we catch Throwable and return empty string if that happens
        String resolution = "";
        try {
            final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            final Display display = wm.getDefaultDisplay();
            final DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            resolution = metrics.widthPixels + "x" + metrics.heightPixels;
        }
        catch (Throwable t) {
            if (Countly.sharedInstance().isLoggingEnabled()) {
                Log.i(Countly.TAG, "Device resolution cannot be determined");
            }
        }
        return resolution;
    }

    /**
     * Maps the current display density to a string constant.
     * @param context context to use to retrieve the current display metrics
     * @return a string constant representing the current display density, or the
     *         empty string if the density is unknown
     */
    static String getDensity(final Context context) {
        String densityStr = "";
        final int density = context.getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
                densityStr = "LDPI";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                densityStr = "MDPI";
                break;
            case DisplayMetrics.DENSITY_TV:
                densityStr = "TVDPI";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                densityStr = "HDPI";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                densityStr = "XHDPI";
                break;
           /* case DisplayMetrics.DENSITY_400:
                densityStr = "XMHDPI";
                break;*/
            case DisplayMetrics.DENSITY_XXHIGH:
                densityStr = "XXHDPI";
                break;
           /* case DisplayMetrics.DENSITY_XXXHIGH:
                densityStr = "XXXHDPI";
                break;*/
        }
        return densityStr;
    }

    /**
     * Returns the display name of the current network operator from the
     * TelephonyManager from the specified context.
     * @param context context to use to retrieve the TelephonyManager from
     * @return the display name of the current network operator, or the empty
     *         string if it cannot be accessed or determined
     */
    static String getCarrier(final Context context) {
        String carrier = "";
        final TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager != null) {
            carrier = manager.getNetworkOperatorName();
        }
        if (carrier == null || carrier.length() == 0) {
            carrier = "";
            if (Countly.sharedInstance().isLoggingEnabled()) {
                Log.i(Countly.TAG, "No carrier found");
            }
        }
        return carrier;
    }

    /**
     * Returns the current locale (ex. "en_US").
     */
    static String getLocale() {
        final Locale locale = Locale.getDefault();
        return locale.getLanguage() + "_" + locale.getCountry();
    }
    /**
     * Returns the application version string stored in the specified
     * context's package info versionName field, or "1.0" if versionName
     * is not present.
     */
    static String getSDKVersion() {
        String result = Countly.COUNTLY_SDK_VERSION_STRING;
       
        return result;
    }
    static String getSDKType() {
        String result = "ANDROID";
       
        return result;
    }
    /**
     * Returns the application version string stored in the specified
     * context's package info versionName field, or "1.0" if versionName
     * is not present.
     */
    static String getAppVersion(final Context context) {
        String result = Countly.DEFAULT_APP_VERSION;
        if(context != null)
        {
            try {
                result = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            }
            catch (PackageManager.NameNotFoundException e) {
                if (Countly.sharedInstance().isLoggingEnabled()) {
                    Log.i(Countly.TAG, "No app version found");
                }
            }        	
        }
        return result;
    }

    /**
     * Returns the application version string stored in the specified
     * context's package info versionName field, or "1.0" if versionName
     * is not present.
     */
    static int getAppVersionCode(final Context context) {
        int result = Countly.DEFAULT_APP_VERSIONCode;
        try {
            result = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        }
        catch (PackageManager.NameNotFoundException e) {
            if (Countly.sharedInstance().isLoggingEnabled()) {
                Log.i(Countly.TAG, "No app version found");
            }
        }
        return result;
    }
    /**
     * Returns the application version string stored in the specified
     * context's package info versionName field, or "1.0" if versionName
     * is not present.
     */
    static String getAppPackageName(final Context context) {
        String result = Countly.DEFAULT_APP_PACKAGENAME;
        if(context != null)
        {
            try {
                result = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
            }
            catch (PackageManager.NameNotFoundException e) {
                if (Countly.sharedInstance().isLoggingEnabled()) {
                    Log.i(Countly.TAG, "No app version found");
                }
            }        	
        }
        return result;
    }
    /**
     * Returns the application version string stored in the specified
     * context's activity info name field, or DEFAULT_APP_PACKAGENAME if versionName
     * is not present.
     */
    static String getActivityName(final Context context) {
        String result = Countly.DEFAULT_APP_PACKAGENAME;
        try {
        	if(context instanceof Activity)
        	{
                result = context.getPackageManager().getActivityInfo(((Activity)context).getComponentName(), 0).name;        		
        	}
            else if(context instanceof Application)
            {
        		result = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
            }
            else if(context instanceof Service)
            {
            	result = ((Service)context).getPackageName();
            }
           
        }
        catch (PackageManager.NameNotFoundException e) {
            if (Countly.sharedInstance().isLoggingEnabled()) {
                Log.i(Countly.TAG, "No activity name found");
            }
        }
        return result;
    }
    /**
     * Returns the application version string stored in the specified
     * context's activity info name field, or DEFAULT_APP_PACKAGENAME if versionName
     * is not present.
     */
    static String getActivityLabel(final Context context) {
        int result = 0;
        String lable = "";
        try {
        	if(context instanceof Activity)
        	{
                result = context.getPackageManager().getActivityInfo(((Activity)context).getComponentName(), 0).labelRes;        		
        	}
            else if(context instanceof Application)
            {
        		result = 0;
            }
            else if(context instanceof Service)
            {
        		result = 0;
            }
           
        	if(result != 0)
        	{
        		lable = context.getResources().getString(result);
        	}
        }
        catch (PackageManager.NameNotFoundException e) {
            if (Countly.sharedInstance().isLoggingEnabled()) {
                Log.i(Countly.TAG, "No activity name found");
            }
        }
        return lable;
    } 
    /**
     * Returns the Activity intent string stored in the specified
     * context's activity info name field, or DEFAULT_APP_PACKAGENAME if versionName
     * is not present.
     */
    static String getActivityLauncher(final Context context)
    {
    	String result = null;
    	Intent intent = null;
        try {

        	if(context instanceof Activity)
        	{
            	intent = ((Activity)context).getIntent();        		
        	}
            else if(context instanceof Application)
        	{
            	intent = null;//((Activity)((Application)context).getBaseContext()).getIntent();
        	}
            else if(context instanceof Service)
            {
            	intent = null;
            }
        	if(intent != null)
        	{ 
        		result = intent.getAction();
        		Set<String> category = intent.getCategories();
                if (Countly.sharedInstance().isLoggingEnabled()) 
                {
            		Log.v(Countly.TAG, "action:"+result+";category:"+category);                	
                }
        		if(category != null && result != null
        			&& category.contains(Intent.CATEGORY_LAUNCHER)
        			&& result.contains(Intent.ACTION_MAIN))
        		{
        			return Mode_Launcher;
        		}
        		if(result != null 
        		  ||(category != null && category.contains(Intent.CATEGORY_DEFAULT)))
        		{
        			return Mode_Implicit;
        		}
        	}  
        	
        }
        catch (Exception e) {
            if (Countly.sharedInstance().isLoggingEnabled()) {
                Log.i(Countly.TAG, "No activity name found");
            }
        }
        return Mode_Explicit;    	
    }
    
    /**
     * Returns the product cpu string stored in the specified
     * context's package info versionName field, or "1.0" if versionName
     * is not present.
     */
    @SuppressWarnings("resource")
	public static String getCpuName() {
    	if(cpuName_ == null)
    	{
    		FileReader fr = null;
    		try {
    			fr = new FileReader("/proc/cpuinfo");
    			BufferedReader br = new BufferedReader(fr);
    			String text = br.readLine();
    			String[] array = text.split(":\\s+", 2);
    			for (int i = 0; i < array.length; i++) {
    			}
				cpuName_ = array[1];      
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}finally {  
                if (fr != null)  
                    try {  
                        fr.close();  
                    } catch (Exception e) {  
                    	e.printStackTrace();
                    }  
            	}  
    	}
		return cpuName_;
    } 
    /**
     * Returns the product system version string stored in the specified
     * context's package info versionName field, or "1.0" if versionName
     * is not present.
     */
    public static String getSysVersionInfo(String path){
    	if(build_number == null)
    	{
            FileInputStream inStream = null; 
    		File tmpFile = new File(path);
    		if(tmpFile.canRead())
    		{
    			File tmpFile2 = new File(path+".version");
    			if(tmpFile2.exists() && tmpFile2.canRead())
    			{
    				try {
    					inStream = new FileInputStream(tmpFile2);
    					BufferedReader br = new BufferedReader(
    							new InputStreamReader(inStream,"UTF-8"));
    					String data = null;
    					while((data = br.readLine())!=null)
    					{
    						build_number = data;
    					}
    					inStream.close();
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			}
    		}    		
    	}
		
		return build_number;
    } 
    /**
     * Returns the product version code stored in the specified
     * context's package info versionName field, or "1.0" if versionName
     * is not present.
     */
    public static int getSysVersion(String path){
    	if(build_number == null)
    	{
       	 	build_number = getSysVersionInfo(buildPath);    		
    	}
		
    	if(build_number != null)
    	{
        	String str[] = build_number.split("_");
        	
        	if(str.length > 1)
        	{
        		return Integer.parseInt(str[0]);    		
        	}
    	}
    	return Countly.COUNTLY_SDK_VERSION_CODE;
    } 
    /**
     * Returns a URL-encoded JSON string containing the device metrics
     * to be associated with a begin session event.
     * See the following link for more info:
     * https://count.ly/resources/reference/server-api
     */
    static String getDeviceString(final Context context) {
     	String result = null;
         try {

         	result = 
         			"product_type=" + DeviceInfo.getDeviceModel()
                     + "&device=" + DeviceInfo.getDeviceModel()
                     + "&cpu=" + java.net.URLEncoder.encode(getCpuName(), "UTF-8")
                     + "&carrier=" + java.net.URLEncoder.encode(getCarrier(context), "UTF-8")
                     + "&resolution=" + java.net.URLEncoder.encode(getResolution(context), "UTF-8")
                     + "&density=" + java.net.URLEncoder.encode(getDensity(context), "UTF-8")
                     + "&device_board=" + java.net.URLEncoder.encode(getDeviceBoard(), "UTF-8")
                     + "&device_brand=" + java.net.URLEncoder.encode(getDeviceBrand(), "UTF-8")
                     + "&device_manufacturer=" + java.net.URLEncoder.encode(getDeviceManufacturer(), "UTF-8")
                     + "&device_name=" + java.net.URLEncoder.encode(getDevice(), "UTF-8")
                     + "&rn=" + Countly.currentTimestamp();
         } catch (UnsupportedEncodingException ignored) {
             // should never happen because Android guarantees UTF-8 support
         }
        return result;
    }

    /**
     * Returns the package name of the app that installed this app
     */
    static String getStore(final Context context) {
        String result = "";
        if(android.os.Build.VERSION.SDK_INT >= 3 ) {
            try {
                result = context.getPackageManager().getInstallerPackageName(context.getPackageName());
            } catch (Exception e) {
                if (Countly.sharedInstance().isLoggingEnabled()) {
                    Log.i(Countly.TAG, "Can't get Installer package");
                }
            }
            if (result == null || result.length() == 0) {
                result = "";
                if (Countly.sharedInstance().isLoggingEnabled()) {
                    Log.i(Countly.TAG, "No store found");
                }
            }
        }
        return result;
    }
    /**
     * Returns a URL-encoded JSON string containing the device metrics
     * to be associated with a begin session event.
     * See the following link for more info:
     * https://count.ly/resources/reference/server-api
     */
    static String getMetrics(final Context context) {
        final JSONObject json = new JSONObject();

        fillJSONIfValuesNotEmpty(json,
        		"_device_id",getDeviceID(),
        		"_idmd5",MD5.getMD5(getDeviceID()),
        		"_ProductID",getProductID(),
                "_os", getOS(),
                "_os_version", getOSVersion(),
                "_ip",getIPAddress(),
                "_sys_version",getSysVersionInfo(buildPath),
                "_time_zone",TimeZone.getDefault().getID(),
                "_locale", getLocale(),
                "_sdk_type", getSDKType(),
                "_sdk_version", getSDKVersion(),
                "_uid", getLocalUid(context),
                "_edu_stage", getStageDigite(context),
                "_grade", getGradeDigite(context),
                "_roleid", getRoleDigite(context),
                "_temp_roleid", getTempRoleDigite(context),
                "_netSSID", getNetSSID(context),
                "_netMac", getNetMac(context),
                "_address",getCity()
                );

        String result = json.toString();

        try {
            result = java.net.URLEncoder.encode(result, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            // should never happen because Android guarantees UTF-8 support
        }

        return result;
    }
    /**
     * Utility method to fill JSONObject with supplied objects for supplied keys.
     * Fills json only with non-null and non-empty key/value pairs.
     * @param json JSONObject to fill
     * @param objects varargs of this kind: key1, value1, key2, value2, ...
     */
    static void fillJSONIfValuesNotEmpty(final JSONObject json, final String ... objects) {
        try {
            if (objects.length > 0 && objects.length % 2 == 0) {
                for (int i = 0; i < objects.length; i += 2) {
                    final String key = objects[i];
                    final String value = objects[i + 1];
                    if (value != null && value.length() > 0) {
                        json.put(key, value);
                    }
                }
            }
        } catch (JSONException ignored) {
            // shouldn't ever happen when putting String objects into a JSONObject,
            // it can only happen when putting NaN or INFINITE doubles or floats into it
        }
    }
    /**
     * Returns a URL-encoded JSON string containing the device metrics
     * to be associated with a begin session event.
     * See the following link for more info:
     * https://count.ly/resources/reference/server-api
     */
    static String getAppInfo(final Context context) {
        final JSONObject json = new JSONObject();

        fillJSONIfValuesNotEmpty(json,
                "_version", getAppVersion(context),
                "_version_index", String.valueOf(getAppVersionCode(context)),
                "_package_name", getAppPackageName(context),
                "_activity_name", getActivityName(context),
                "_activity_mode", getActivityLauncher(context)
 //               "_preactivity_name", getPreActivity(context)
                );

        String result = json.toString();

        try {
            result = java.net.URLEncoder.encode(result, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            // should never happen because Android guarantees UTF-8 support
        }

        return result;
    }
    
    public static String uid = null;
    
	/**
	 * 获取用户id
	 * */
	public static String getLocalUid(Context mContext) {
		if(uid == null) {
			uid = "04";
			//获取用户ID
			if(mContext != null)
			{
				ContentResolver resolver = mContext.getContentResolver();
				Cursor cursor = resolver.query(CONTENT_URI, null, null, null, null);
				if (cursor != null) {
					while (cursor.moveToNext()) {
						uid = cursor.getString(cursor.getColumnIndex(PUSERID));
					}
					cursor.close();
				}
				
			}
		}
		return uid;
	}

	/**
	 * 获取用户id所在学段数字
	 * */
	public static String getStageDigite(Context mContext) {
		String stage = "3";

		if(mContext != null)
		{
			//获取用户id所在学段数字
			ContentResolver resolver = mContext.getContentResolver();
	    	Cursor cursor = resolver.query(CONTENT_URI, null, null, null, null);
	    	if (cursor != null) {
	    		while (cursor.moveToNext()) {
	    			stage = cursor.getString(cursor.getColumnIndex(EDU_STAGE));
	    		}
	    		cursor.close();
	    	}  
		}
    	return stage;
	}	
	/**
	 *获取用户id所在年级数字
	 * */
	public static String getGradeDigite(Context mContext) {
		String stage = "3";

		if(mContext != null)
		{
			//获取用户id所在年级数字
			ContentResolver resolver = mContext.getContentResolver();
	    	Cursor cursor = resolver.query(CONTENT_URI, null, null, null, null);
	    	if (cursor != null) {
	    		while (cursor.moveToNext()) {
	    			stage = cursor.getString(cursor.getColumnIndex(GRADE));
	    		}
	    		cursor.close();
	    	}			
		}
    	
    	return stage;
	}
	/**
	 *获取用户id角色 roleid就是当前用户的实际角色
	 * */
	public static String getRoleDigite(Context mContext) {
		String stage = "3";

		if(mContext != null)
		{
			//获取用户id所在年级数字
			ContentResolver resolver = mContext.getContentResolver();
	    	Cursor cursor = resolver.query(CONTENT_URI, null, null, null, null);
	    	if (cursor != null) {
	    		while (cursor.moveToNext()) {
	    			stage = cursor.getString(cursor.getColumnIndex(ROLEID));
	    		}
	    		cursor.close();
	    	}			
		}
    	
    	return stage;
	}
	/**
	 *获取用户id 当前用户的操作权限角色

	 * */
	public static String getTempRoleDigite(Context mContext) {
		String stage = "3";

		if(mContext != null)
		{
			//获取用户id所在年级数字
			ContentResolver resolver = mContext.getContentResolver();
	    	Cursor cursor = resolver.query(CONTENT_URI, null, null, null, null);
	    	if (cursor != null) {
	    		while (cursor.moveToNext()) {
	    			stage = cursor.getString(cursor.getColumnIndex(TEMP_ROLEID));
	    		}
	    		cursor.close();
	    	}			
		}
    	
    	return stage;
	}
	/**
	 *获取用户id 当前用户的操作角色类型

	 * */
	public static int getTypeDigite(Context mContext) {
		int type = 0;

		if(mContext != null)
		{
			//获取用户id所在年级数字
			ContentResolver resolver = mContext.getContentResolver();
	    	Cursor cursor = resolver.query(CONTENT_URI, null, null, null, null);
	    	if (cursor != null) {
	    		while (cursor.moveToNext()) {
	    			type = cursor.getInt(cursor.getColumnIndex(TYPE));
	    		}
	    		cursor.close();
	    	}			
		}
    	
    	return type;
	}
	  /**
     * Returns the Activity intent string stored in the specified
     * context's activity info name field, or DEFAULT_APP_PACKAGENAME if versionName
     * is not present.
     */
    static String getPreActivity(final Context context)
    {
    	String result = "";
        try {
        	if(context instanceof Activity)
        	{
        		/*android.permission.GET_TASKS*/     
            	ActivityManager activityManager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);   
            	List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(2);  
                for(ActivityManager.RunningTaskInfo taskInfo:runningTasks){  
                    /*info+="一个任务信息开始：/n";  
                    info+="当前任务中正处于运行状态的activity数目:"+taskInfo.numRunning;  
                    info+="当前任务中的activity数目: "+taskInfo.numActivities;  */
                    result = taskInfo.baseActivity.getClassName();                
                }     
                if (Countly.sharedInstance().isLoggingEnabled()) 
                {
            		Log.v(Countly.TAG, result);                	
                }     
        	}
        	
        }
        catch (Exception e) {
            if (Countly.sharedInstance().isLoggingEnabled()) {
                Log.i(Countly.TAG, "No activity name found");
            }
        }
        return result;    	
    }
}
