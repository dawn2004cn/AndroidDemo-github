package com.noahedu.countdowntimer;

import android.app.Application;

import com.noahedu.common.util.LogUtils;

/**
 * © 2020 www.youxuepai.com
 *
 * @version 1.0
 * @file name：CountDownTimerManager$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/12/11$ 14:09$
 */
public class CountDownTimerManager {
    private static final String TAG = CountDownTimerManager.class.getSimpleName();
    private volatile static CountDownTimerManager instance;

    private Application context;
    private CountDownTimerManager() {
    }

    public static CountDownTimerManager getInstance() {
        if (instance == null) {
            synchronized (CountDownTimerManager.class) {
                if (instance == null) {
                    instance = new CountDownTimerManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     *
     * @param application Application
     * @param millisInFuture     是否开启日志
     */
    public void init(Application application, long millisInFuture) {
        this.context = application;
        CountDownTimerService.initCountdowning(context,millisInFuture);
    }

    public void start() {
        if (context == null) {
            LogUtils.e(TAG, "未进行初始化");
            return;
        }
        LogUtils.i(TAG, "start...");
        CountDownTimerService.startCountdowning(context);
    }

    public void stop() {
        if (context == null) {
            return;
        }
        LogUtils.i(TAG, "start...");
        CountDownTimerService.stopCountdowning(context);
    }

    public void resume() {
        if (context == null) {
            return;
        }
        CountDownTimerService.resumeCountdowning(context);
    }

    public void pause() {
        if (context == null) {
            return;
        }
        CountDownTimerService.pauseCountdowning(context);
    }

    public TimerState getTimerState(){
        return CountDownTimerService.getState();
    }


    /**
     * 计时状态监听回调
     */
    public void setCountDownTimerStateListener(OnCountDownTimerListener listener) {
        CountDownTimerService.setCountDownTimerStateListener(listener);
    }
}
