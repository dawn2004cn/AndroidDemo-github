package com.noahedu.demo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


/**
 * BaseActivity
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-6-1
 */
public abstract class AbsBaseActivity extends Activity {
    //获取TAG的Activity名称
    protected final String TAG = this.getClass().getSimpleName();
    //是否显示标题栏
    private boolean isShowTitle = true;
    //是否显示状态栏
    private boolean isShowStatusBar = true;
    //是否允许旋转屏幕
    private boolean isAllowScreenRoate = true;
    //封装Toast
    private static Toast toast;
    public Context context;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        //activityi管理
        ActivityCollector.addActivity(this);
        if (!isShowTitle){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        if (!isShowStatusBar){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        //初始化布局
        //设置屏幕是否可旋转
        if (!isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //设置布局
        setContentView(layoutId());
        //初始化控件
        initView();
        //设置数据
        initData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化布局
     * @return 布局id
     */
    protected abstract int layoutId();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 设置数据
     */
    protected abstract void initData();

    /**
     * 设置是否显示标题栏
     *
     * @param showTitle true or false
     */
    public void setShowTitle(boolean showTitle) {
        isShowTitle = showTitle;
    }

    /**
     * 设置是否显示状态栏
     *
     * @param showStatusBar true or false
     */
    public void setShowStatusBar(boolean showStatusBar) {
        isShowStatusBar = showStatusBar;
    }

    /**
     * 是否允许屏幕旋转
     *
     * @param allowScreenRoate true or false
     */
    public void setAllowScreenRoate(boolean allowScreenRoate) {
        isAllowScreenRoate = allowScreenRoate;
    }

    /**
     * 保证同一按钮在1秒内只会响应一次点击事件
     */
    public abstract class OnSingleClickListener implements View.OnClickListener {
        //两次点击按钮之间的间隔，目前为1000ms
        private static final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime;

        public abstract void onSingleClick(View view);

        @Override
        public void onClick(View view) {
            long curClickTime = System.currentTimeMillis();
            if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                lastClickTime = curClickTime;
                onSingleClick(view);
            }
        }
    }

    /**
     * 同一按钮在短时间内可重复响应点击事件
     */
    public abstract class OnMultiClickListener implements View.OnClickListener {
        public abstract void onMultiClick(View view);

        @Override
        public void onClick(View v) {
            onMultiClick(v);
        }
    }

    /**
     * 显示提示  toast
     *
     * @param msg 提示信息
     */
    @SuppressLint("ShowToast")
    public void showToast(String msg) {
        try {
            if (null == toast) {
                toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            } else {
                toast.setText(msg);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    //活动管理器
    public static class ActivityCollector {

        public static List<Activity> activitys = new ArrayList<Activity>();

        /**
         * 向List中添加一个活动
         *
         * @param activity 活动
         */
        public static void addActivity(Activity activity) {

            activitys.add(activity);
        }

        /**
         * 从List中移除活动
         *
         * @param activity 活动
         */
        public static void removeActivity(Activity activity) {

            activitys.remove(activity);
        }

        /**
         * 将List中存储的活动全部销毁掉
         */
        public static void finishAll() {

            for (Activity activity : activitys) {

                if (!activity.isFinishing()) {

                    activity.finish();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activity管理
        ActivityCollector.removeActivity(this);
    }
}
