package com.noahedu.audiorecorder.base;

import android.app.Application;
import android.content.Context;

import com.noahedu.common.http.network.RetrofitFactory;
import com.noahedu.recorderlib.recorder.wav.WavUtils;
import com.noahedu.recorderlib.utils.ByteUtils;
import com.noahedu.recorderlib.utils.Logger;

import org.apache.ibatis.MybatisCommon;


/**
 * @author zlw on 2018/7/4.
 */
public class MyApp extends Application {

    private static final String TAG =  MyApp.class.getSimpleName();
    private static MyApp instance;
    protected String userAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //MultiDex.install(this);
        RetrofitFactory.setmContext(instance);


        //userAgent = Util.getUserAgent(this, "ExoPlayerDemo");

        Logger.w("zlwTest", "TEST-----------------");
        byte[] header1 = WavUtils.generateWavFileHeader(1024, 16000, 1, 16);
        byte[] header2 = WavUtils.generateWavFileHeader(1024, 16000, 1, 16);

        Logger.d("zlwTest", "Wav1: %s", WavUtils.headerToString(header1));
        Logger.d("zlwTest", "Wav2: %s", WavUtils.headerToString(header2));

        Logger.w("zlwTest", "TEST-2----------------");

        Logger.d("zlwTest", "Wav1: %s", ByteUtils.toString(header1));
        Logger.d("zlwTest", "Wav2: %s", ByteUtils.toString(header2));

        MybatisCommon.init(this);



    }

    public static MyApp getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

}
