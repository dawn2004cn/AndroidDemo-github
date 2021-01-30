package com.noahedu.countdowntimer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.noahedu.common.util.LogUtils;


/**
 * © 2020 www.youxuepai.com
 *
 * @version 1.0
 * @file name：CountDownTimerService$
 * @file describe：计时服务
 * @anthor :daisg
 * @create time 2020/12/11$ 13:35$
 */
public class CountDownTimerService extends Service {
    private static final String TAG = CountDownTimerService.class.getSimpleName();

    private final static String ACTION_NAME = "action_type";
    private final static String PARAM_TIME = "parar_time";

    private final static int ACTION_INVALID = 0;

    private final static int ACTION_INIT_COUNTDOWN = 1;

    private final static int ACTION_START_COUNTDOWN= 2;

    private final static int ACTION_STOP_COUNTDOWN = 3;

    private final static int ACTION_RESUME_COUNTDOWN = 4;

    private final static int ACTION_PAUSE_COUNTDOWN = 5;

    private CountDownTimerSupport mSupport;


    public CountDownTimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey(ACTION_NAME)) {
            switch (bundle.getInt(ACTION_NAME, ACTION_INVALID)) {
                case ACTION_INIT_COUNTDOWN:
                    doInitCountdowning(bundle.getLong(PARAM_TIME));
                    break;
                case ACTION_START_COUNTDOWN:
                    doStartCountdowning();
                    break;
                case ACTION_STOP_COUNTDOWN:
                    doStopCountdowning();
                    break;
                case ACTION_RESUME_COUNTDOWN:
                    doResumeCountdowning();
                    break;
                case ACTION_PAUSE_COUNTDOWN:
                    doPauseCountdowning();
                    break;
                default:
                    break;
            }
            return START_STICKY;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public static void initCountdowning(Context context,long millisInFuture) {
        Intent intent = new Intent(context, CountDownTimerService.class);
        intent.putExtra(ACTION_NAME, ACTION_INIT_COUNTDOWN);
        intent.putExtra(PARAM_TIME, millisInFuture);
        context.startService(intent);
    }

    public static void startCountdowning(Context context) {
        Intent intent = new Intent(context, CountDownTimerService.class);
        intent.putExtra(ACTION_NAME, ACTION_START_COUNTDOWN);
        context.startService(intent);
    }

    public static void stopCountdowning(Context context) {
        Intent intent = new Intent(context, CountDownTimerService.class);
        intent.putExtra(ACTION_NAME, ACTION_STOP_COUNTDOWN);
        context.startService(intent);
    }

    public static void resumeCountdowning(Context context) {
        Intent intent = new Intent(context, CountDownTimerService.class);
        intent.putExtra(ACTION_NAME, ACTION_RESUME_COUNTDOWN);
        context.startService(intent);
    }

    public static void pauseCountdowning(Context context) {
        Intent intent = new Intent(context, CountDownTimerService.class);
        intent.putExtra(ACTION_NAME, ACTION_PAUSE_COUNTDOWN);
        context.startService(intent);
    }

      /**
     * 获取当前的计时状态
     */
    public static TimerState getState() {
        return CountDownTimerSupport.getInstance().getTimerState();
    }

    public static void setCountDownTimerStateListener(OnCountDownTimerListener recordStateListener) {
        CountDownTimerSupport.getInstance().setOnCountDownTimerListener(recordStateListener);
    }

    private void doInitCountdowning(long millisInFuture) {
        LogUtils.v(TAG, "doInitCountdowning");
        if (mSupport == null)
        {
            mSupport = new CountDownTimerSupport(millisInFuture,1000);
        }
        //CountDownTimerSupport.getInstance().setMillisInFuture(millisInFuture);
    }

    private void doStartCountdowning() {
        LogUtils.v(TAG, "doResumeCountdowning");
        if (mSupport != null) {
            mSupport.start();
        }
    }

    private void doResumeCountdowning() {
        LogUtils.v(TAG, "doResumeCountdowning");
        if(mSupport != null){
            mSupport.resume();
        }
    }

    private void doPauseCountdowning() {
        LogUtils.v(TAG, "doPauseCountdowning");
        if(mSupport != null) {
            mSupport.pause();
        }
    }

    private void doStopCountdowning() {
        LogUtils.v(TAG, "doStopCountdowning");
        if (mSupport != null) {
            mSupport.stop();
        }
        stopSelf();
    }
}
