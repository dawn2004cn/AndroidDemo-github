package com.noahedu.common.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.noahedu.common.constant.SizeConstants;


import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 设备属性相关工具类
 * Created by tsy on 16/7/25.
 */
public class DeviceUtils {

    private static final String TAG = "DeviceUtils";
   

    /**
     * 获取手机设备id 需要READ_PHONE_STATE权限
     * @param context 全局context
     * @return device id
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取手机sim卡id 需要READ_PHONE_STATE权限
     * @param context 全局context
     * @return sim id
     */
    public static String getSubscriberId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSubscriberId();
    }

    /**
     * 判断是否平板设备
     * @param context 全局context
     * @return true:平板,false:手机
     */
    public static boolean isTabletDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 判断sd卡是否存在
     * @return true:存在；false：不存在
     */
    public static boolean isSdcardExisting() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取手机内部存储剩余空间 单位byte
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getAvailableInternalStorageSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());

        if(Build.VERSION.SDK_INT >= 18) {
            return stat.getAvailableBytes();
        } else {
            return (long) stat.getAvailableBlocks() * stat.getBlockSize();
        }
    }

    /**
     * 获取手机内部总存储空间 单位byte
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getTotalInternalStorageSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());

        if(Build.VERSION.SDK_INT >= 18) {
            return stat.getTotalBytes();
        } else {
            return (long) stat.getBlockCount() * stat.getBlockSize();
        }
    }

    /**
     * 获取SDCARD剩余存储空间 单位byte
     * @return
     */
    public static long getAvailableExternalStorageSize() {
        if (isSdcardExisting()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());

            if(Build.VERSION.SDK_INT >= 18) {
                return stat.getAvailableBytes();
            } else {
                return (long) stat.getAvailableBlocks() * stat.getBlockSize();
            }
        } else {
            return 0L;
        }
    }

    /**
     * 获取SDCARD总的存储空间 单位byte
     * @return
     */
    public static long getTotalExternalStorageSize() {
        if (isSdcardExisting()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());

            if(Build.VERSION.SDK_INT >= 18) {
                return stat.getTotalBytes();
            } else {
                return (long) stat.getBlockCount() * stat.getBlockSize();
            }
        } else {
            return 0;
        }
    }
    public static long getTotalRAM() {
    	RandomAccessFile reader = null;
    	long totalMemory = 0;
        String load = null;
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
            }
            reader.close();

            totalMemory = Long.parseLong(value) / SizeConstants.KB_2_BYTE;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return totalMemory;
    }

    /**
     * Returns the current model.
     */
    public static String getModel() {
        if(android.os.Build.VERSION.SDK_INT < 21 )
            return Build.MODEL;
        else
            return android.os.Build.MODEL;
    }

    /**
     * Returns the current device cpu.
     */
    public static String getCpu() {
        if(android.os.Build.VERSION.SDK_INT < 21 )
            return android.os.Build.CPU_ABI;
        else
            //return Build.SUPPORTED_ABIS[0];
            return android.os.Build.CPU_ABI;
    }

    /**
     * Returns the current device openGL version.
     */
    public static String getOpenGL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        FeatureInfo[] featureInfos = packageManager.getSystemAvailableFeatures();
        if (featureInfos != null && featureInfos.length > 0) {
            for (FeatureInfo featureInfo : featureInfos) {
                // Null feature name means this feature is the open gl es version feature.
                if (featureInfo.name == null) {
                    if (featureInfo.reqGlEsVersion != FeatureInfo.GL_ES_VERSION_UNDEFINED) {
                        return Integer.toString((featureInfo.reqGlEsVersion & 0xffff0000) >> 16);
                    } else {
                        return "1"; // Lack of property means OpenGL ES version 1
                    }
                }
            }
        }
        return "1";
    }

    /**
     * Returns the current device RAM amount.
     */
    public static String getRamCurrent(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return Long.toString(getTotalRAM() - (mi.availMem / SizeConstants.MB_2_BYTE));
    }

    /**
     * Returns the total device RAM amount.
     */
    public static String getRamTotal(Context context) {
        return Long.toString(getTotalRAM());
    }
    
    /**
     * Returns the current operating system version as a displayable string.
     */
    public static String getOSVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * Returns the current device.
     */
    public static String getDevice() {
        return android.os.Build.DEVICE;
    }
    
    /**
     * Returns the current device board.
     */
    public static String getDeviceBoard() {
        return android.os.Build.BOARD;
    }
    /**
     * Returns the current device brand.
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    } 
    /**
     * Returns the current device Manufacturer.
     */
    public static String getDeviceManufacturer() {
        return android.os.Build.MANUFACTURER;
    }   

    /**
     * Returns the display name of the current network operator from the
     * TelephonyManager from the specified context.
     * @param context context to use to retrieve the TelephonyManager from
     * @return the display name of the current network operator, or the empty
     *         string if it cannot be accessed or determined
     */
    public static String getCarrier(final Context context) {
        String carrier = "";
        final TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager != null) {
            carrier = manager.getNetworkOperatorName();
        }
        if (carrier == null || carrier.length() == 0) {
            carrier = "";
            Log.i(TAG, "No carrier found");
        }
        return carrier;
    }

    /**
     * Returns the current locale (ex. "en_US").
     */
    public static String getLocale() {
        final Locale locale = Locale.getDefault();
        return locale.getLanguage() + "_" + locale.getCountry();
    }
    
	private final static String COM_NOAHEDU_SYSTEM_NAME = "com.noahedu.fw.noahsys.util.NoahProduct";
    /**
     * Returns the current device.
     * load <uses-library android:name="com.noahedu" />
     */
    public static String getProductID() {
 		String productID_ = null;
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
        		   ignored.printStackTrace();
        	   }
        	   catch (NoSuchMethodException ignored) {
        		   ignored.printStackTrace();
        	   }
        	   catch (InvocationTargetException ignored) {
        		   ignored.printStackTrace();
        	   }
    	}
    	
        return productID_;
    }

    /**
     * Returns the current device.
     * load <uses-library android:name="com.noahedu" />
     */
    public static String getProductName() {
        String productName_ = null;
        if(productName_ == null)
        {
            try {
                final Class<?> cls = Class.forName(COM_NOAHEDU_SYSTEM_NAME);
                final Method isInitializedMethod = cls.getMethod("getProductName", (Class[]) null);
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
                    productName_ = (String) result;
                }
            }
            catch (ClassNotFoundException ignored) {
                ignored.printStackTrace();
            }
            catch (NoSuchMethodException ignored) {
                ignored.printStackTrace();
            }
            catch (InvocationTargetException ignored) {
                ignored.printStackTrace();
            }
        }

        return productName_;
    }
}
