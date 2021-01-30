package com.noahedu.demo.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.noahedu.utils.ImageUtil;
import com.noahedu.utils.ImageUtils;

/**
 * apk util
 * <br/>
 * Apk文件操作相关工具类
 *
 * @author wlf(Andy)
 * @email 411086563@qq.com
 */
public class ApkUtils {

    static  String TAG = ApkUtils.class.getSimpleName();
    public static List<MyAppInfo> mLocalInstallApps = null;

    public  static String Path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/tmp";
    /**
     * get UnInstallApkPackageName
     *
     * @param context Context
     * @param apkPath apkPath
     * @return apk PackageName
     */
    public static String getUnInstallApkPackageName(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            if (appInfo != null) {
                return appInfo.packageName;
            }
        }
        return null;
    }

    /**
     * install an apk bu apkPath
     *
     * @param context Context
     * @param apkPath apkPath
     */
    public static final void installApk(Context context, String apkPath) {
        if (TextUtils.isEmpty(apkPath)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * check whether app installed
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean checkAppInstalled(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_INSTRUMENTATION);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }
    
 
    public static List<MyAppInfo> scanLocalInstallAppList(PackageManager packageManager) {
        List<MyAppInfo> myAppInfos = new ArrayList<MyAppInfo>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤掉系统app
/*	            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
	                continue;
	            }*/
                MyAppInfo myAppInfo = new MyAppInfo();
                myAppInfo.setAppName(packageInfo.packageName);
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
                myAppInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(packageManager));
                myAppInfos.add(myAppInfo);
            }
        }catch (Exception e){
            Log.e(TAG,"===============获取应用包信息失败");
        }
        return myAppInfos;
    }
    
    
    public static List<MyAppInfo> queryAppInfo(Context ctx) {
    	List<MyAppInfo> mlistAppInfo = new ArrayList<MyAppInfo>();  
        PackageManager pm = ctx.getPackageManager();  
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);  
        //mainIntent.addCategory(Intent.CATEGORY_DEFAULT);  
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);  
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0);  

        Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));  
          
        if (mlistAppInfo != null) {  
            mlistAppInfo.clear();  
            for(ResolveInfo resolveInfo : resolveInfos) {  
                String activityName = resolveInfo.activityInfo.name; 
                String pkgName = resolveInfo.activityInfo.packageName;  
                String appLabel = (String)resolveInfo.loadLabel(pm);   
                Drawable icon = resolveInfo.loadIcon(pm);
                Bitmap bt = ImageUtils.drawableToBitmap(icon);
                String name = appLabel+"-"+activityName+".jpg";
                try {
                    ImageUtils.saveImageToSD(ctx.getCacheDir().getAbsolutePath(),name,bt,100);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.v("ii", "类名:"+activityName+";包名:"+pkgName +";应用名:"+appLabel);  
              
                Intent launchIntent = new Intent();  
                launchIntent.setComponent(new ComponentName(pkgName,activityName));  
                  
               
                MyAppInfo appInfo = new MyAppInfo();  
                appInfo.setAppName(appLabel);  
                appInfo.setPackageName(pkgName);  
                appInfo.setAppIcon(icon);  
                appInfo.setActivityName(activityName);
                appInfo.setLaunchIntent(launchIntent);  
                //if((resolveInfo.activityInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0)
                {
                	mlistAppInfo.add(appInfo);
                }              
            }  
        }  
          
        return mlistAppInfo;  
    }  
}
