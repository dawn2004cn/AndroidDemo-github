package com.noahedu.demo.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AppOpsManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TopWindowService extends Service {
    public static final String TAG = TopWindowService.class.getSimpleName();
    public static final String OPERATION = "operation";
    public static final String PACKAGENAME = "packagename";
    public static final int OPERATION_SHOW = 100;
    public static final int OPERATION_HIDE = 101;
    public static final int OPERATION_UPDATE = 102;

    private static final int HANDLE_SHOW_ACTIVITY = 200;
    private static final int HANDLE_HIDE_ACTIVITY = 201;
    private static final int HANDLE_UPDATE_ACTIVITY = 202;
    private static final int HANDLE_CHECK_ACTIVITY = 203;

    private boolean isAdded = false; // 是否已增加悬浮窗
    private boolean flag = false;
    private static WindowManager wm;
    private static WindowManager.LayoutParams params;
    private Button btn_floatView;

    private List<String> homeList; // 桌面应用程序包名列表
    private ActivityManager mActivityManager;
    private static String shortClassName;
    private static String className;
    private static String packageName;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        homeList = getHomes();
        //onInit();
        createFloatView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isAdded) {
            wm.removeView(btn_floatView);
            isAdded = false;
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    public void onInit(){
        //分线程中循环遍历
        new Thread() {
            @Override
            public void run() {
                flag = true;
                while (flag) {
                    //获取包名
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//版本小于lollipop的
                        ActivityManager am = ((ActivityManager) getSystemService(ACTIVITY_SERVICE));
                        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(5);
                        packageName = taskInfo.get(0).topActivity.getPackageName();
                        shortClassName = taskInfo.get(0).topActivity.getShortClassName();
                        className = taskInfo.get(0).topActivity.getClassName();

                    } else { //版本为Lollipop及以上
                        if (needPermissionForBlocking(getApplicationContext())) {
                            //如果用户没有授权，引导用户去设置页面授权
                            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                            //在service中开启activity需要为intent添加FLAG_ACTIVITY_NEW_TASK的flag
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            //得到包名
                            packageName = getTopPackage();
                        }
                        Log.e("TAG", "top app = " + packageName);
                        //有了包名之后就可以与应用锁数据库里面的包名做匹配
                        //如果匹配上就弹出输入密码的页面，酱紫就可以实现应用锁了(是不是有点想得太简单了= =)
                    }
                    SystemClock.sleep(1000);
                }
            }
        }.start();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        int operation = intent.getIntExtra(OPERATION, OPERATION_SHOW);
        Log.v(TAG,"onStartCommand:"+operation);
        switch (operation) {
            case OPERATION_SHOW: {
                mHandler.removeMessages(HANDLE_SHOW_ACTIVITY);
                Message msg = new Message();
                msg.what = HANDLE_SHOW_ACTIVITY;
                msg.obj = intent.getStringExtra(PACKAGENAME);
                mHandler.sendMessage(msg);
            }
            break;
            case OPERATION_HIDE: {
                mHandler.removeMessages(HANDLE_HIDE_ACTIVITY);
                Message msg = new Message();
                msg.what = HANDLE_HIDE_ACTIVITY;
                mHandler.sendMessage(msg);
            }
            break;
            case OPERATION_UPDATE: {
                mHandler.removeMessages(HANDLE_UPDATE_ACTIVITY);
                Message msg = new Message();
                msg.what = HANDLE_UPDATE_ACTIVITY;
                mHandler.sendMessage(msg);
            }
            break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLE_SHOW_ACTIVITY:
                    if (!isHome()) {
                        Log.v(TAG, "this is home,add this window" + msg.obj);
                        if (isAdded) {
                            wm.removeView(btn_floatView);
                            isAdded = false;
                        }
                        if (!isAdded) {
                            if (msg.obj != null) {
                                btn_floatView.setText((String) msg.obj);
                            }
                            wm.addView(btn_floatView, params);
                            isAdded = true;
                        }
                    } else {
                        Log.v(TAG, "not is home,remove this window");
                        if (isAdded) {
                            wm.removeView(btn_floatView);
                            isAdded = false;
                        }
                    }
                    mHandler.sendEmptyMessageDelayed(HANDLE_CHECK_ACTIVITY, 1000);
                    break;
                case HANDLE_HIDE_ACTIVITY: {
                    Log.v(TAG, "not is home,remove this window");
                    if (isAdded) {
                        wm.removeView(btn_floatView);
                        isAdded = false;
                    }
                }
                mHandler.sendEmptyMessageDelayed(HANDLE_CHECK_ACTIVITY, 1000);
                break;
                case HANDLE_UPDATE_ACTIVITY:
                    if (msg.obj != null) {
                        btn_floatView.setText((String) msg.obj);
                    }
                    mHandler.sendEmptyMessageDelayed(HANDLE_CHECK_ACTIVITY, 1000);
                    break;
                case HANDLE_CHECK_ACTIVITY:
                    RunningTaskInfo info = getRunningTaskInfo();
                    String shortClassNametmp = info.topActivity.getShortClassName(); //类名
                    String classNametmp = info.topActivity.getClassName(); //完整类名
                    String packageNametmp = info.topActivity.getPackageName(); //包名
                    Log.v(TAG,"top package name old:"+packageName+" isAdded:"+isAdded);
                    if(!shortClassNametmp.equals(shortClassName) ){
                        shortClassName = shortClassNametmp;
                        className = classNametmp;
                        packageName = packageNametmp;

                        if (!isAdded) {
                            wm.addView(btn_floatView, params);
                            wm.updateViewLayout(btn_floatView,params);
                            isAdded = true;
                        }
                        else {
                            btn_floatView.setText((String) packageName);
                        }
                    }
                    Log.v(TAG,"top package name:"+packageName+" isAdded:"+isAdded);
                    mHandler.sendEmptyMessageDelayed(HANDLE_CHECK_ACTIVITY, 1000);
                    break;
                default:
                    break;
            }
        }
    };


    public void setFloatView(String text) {
        btn_floatView.setText(text);
    }

    /**
     * 创建悬浮窗
     */
    private void createFloatView() {
        btn_floatView = new Button(getApplicationContext());
        btn_floatView.setText("警告");
        //btn_floatView.setAlpha(0.8f);
        btn_floatView.setBackgroundColor(Color.WHITE);
        //btn_floatView.setBackgroundResource(R.drawable.earth);

        wm = (WindowManager) getApplicationContext().getSystemService(
                Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();

        // 设置window type  //6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        //params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        /*
         * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
         * 即拉下通知栏不可见
         */

        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        // 设置Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
         * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
         * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
         */

        // 设置悬浮窗的长得宽
        params.width = 400;
        params.height = 80;

        // 设置悬浮窗的Touch监听
        final Button finalBtn_floatView = btn_floatView;
        // 设置悬浮窗的Touch监听
        btn_floatView.setOnTouchListener(new OnTouchListener() {
            int lastX, lastY;
            int paramX, paramY;

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params.x;
                        paramY = params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        params.x = paramX + dx;
                        params.y = paramY + dy;
                        // 更新悬浮窗位置
                        wm.updateViewLayout(btn_floatView, params);
                        break;
                }
                return true;
            }
        });

        wm.addView(btn_floatView, params);
        wm.updateViewLayout(finalBtn_floatView, params);
        isAdded = true;
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        // 属性
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
            Log.v(TAG,"list Main:"+ri.activityInfo.packageName);
        }
        return names;
    }

    /**
     * 判断当前界面是否是桌面
     */
    public boolean isHome() {
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        }
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(5);
        Log.v(TAG,"topActivity:"+rti.get(0).topActivity.getPackageName());
        //return true;
        return homeList.contains(rti.get(0).topActivity.getPackageName());
    }

    public RunningTaskInfo getRunningTaskInfo() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        RunningTaskInfo info = manager.getRunningTasks(5).get(0);
        String shortClassName = info.topActivity.getShortClassName(); //类名
        String className = info.topActivity.getClassName(); //完整类名
        String packageName = info.topActivity.getPackageName(); //包名
        return  info;
    }

    public UsageStats getTopActivity(){
        long ts = System.currentTimeMillis();
        UsageStatsManager mUsageStatsManager = (UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts-1000, ts);
        if (usageStats == null || usageStats.size() == 0) {//如果为空则返回""
            return null;
        }
        Collections.sort(usageStats, new RecentUseComparator());//mRecentComp = new RecentUseComparator()
        return usageStats.get(0);
    }

    /**
     * 获得top activity的包名
     *
     * @return
     */
    @TargetApi(21)
    public String getTopPackage() {
        long ts = System.currentTimeMillis();
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts - 1000, ts);
        if (usageStats == null || usageStats.size() == 0) {//如果为空则返回""
            return "";
        }
        Collections.sort(usageStats, new RecentUseComparator());
        return usageStats.get(0).getPackageName();
    }

    /**
     *
     */
    @TargetApi(21)
    static class RecentUseComparator implements Comparator<UsageStats> {
        @Override
        public int compare(UsageStats lhs, UsageStats rhs) {
            return (lhs.getLastTimeUsed() > rhs.getLastTimeUsed()) ? -1 : (lhs.getLastTimeUsed() == rhs.getLastTimeUsed()) ? 0 : 1;
        }
    }

    /**
     * 使用UsageStatsManager需要用户允许开启，该方法用于判断用户是否已经授权
     *
     * @param context
     * @return true:还没有授权 false:已经授权
     */
    @TargetApi(19)
    public static boolean needPermissionForBlocking(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode != AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }

}
