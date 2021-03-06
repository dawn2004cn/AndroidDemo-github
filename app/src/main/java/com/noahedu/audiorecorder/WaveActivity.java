package com.noahedu.audiorecorder;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.noahedu.audiorecorder.util.MusicSimilarityUtil;
import com.noahedu.audiorecorder.util.U;
import com.noahedu.demo.R;
import com.noahedu.wavelibrary.draw.WaveCanvas;
import com.noahedu.wavelibrary.utils.SamplePlayer;
import com.noahedu.wavelibrary.utils.SoundFile;
import com.noahedu.wavelibrary.view.WaveSurfaceView;
import com.noahedu.wavelibrary.view.WaveformView;

import java.io.File;
import java.io.IOException;
import java.util.List;
/*
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
*/


/**
 *@author:cokus
 *@email:czcoku@gmail.com
 *
 * 根据自己研究发现sfv不适合做这个波形实时绘制的例子，因为每次清屏要重新绘制，所以我想了想为啥不用view呢？
 * 期待吧。
 */
//@RuntimePermissions
public class WaveActivity extends Activity {
    WaveSurfaceView waveSfv;
    Button switchBtn;
    TextView status;
    WaveformView waveView;
    Button playBtn;
    Button scoreBtn;

    private static final int FREQUENCY = 16000;// 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    private static final int CHANNELCONGIFIGURATION = AudioFormat.CHANNEL_IN_MONO;// 设置单声道声道
    private static final int AUDIOENCODING = AudioFormat.ENCODING_PCM_16BIT;// 音频数据格式：每个样本16位
    public final static int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;// 音频获取源
    private int recBufSize;// 录音最小buffer大小
    private AudioRecord audioRecord;
    private WaveCanvas waveCanvas;
    private String mFileName = "test";//文件名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_wave);
        //ButterKnife.bind(this);
        waveSfv = (WaveSurfaceView)findViewById(R.id.wavesfv);
        switchBtn = (Button)findViewById(R.id.switchbtn);
        playBtn = (Button)findViewById(R.id.play);
        scoreBtn = (Button)findViewById(R.id.socreaudio);
        waveView = (WaveformView)findViewById(R.id.waveview);
        status = (TextView)findViewById(R.id.status);
        switchBtn.setOnClickListener(new Button.OnClickListener(){
            @Override

            public void onClick(View v) {
                    click(v);
            }
        });
        playBtn.setOnClickListener(new Button.OnClickListener(){
            @Override

            public void onClick(View v) {
                click(v);
            }
        });

        scoreBtn.setOnClickListener(new Button.OnClickListener(){
            @Override

            public void onClick(View v) {
                click(v);
            }
        });
        if(waveSfv != null) {
            waveSfv.setLine_off(42);
            //解决surfaceView黑色闪动效果
            waveSfv.setZOrderOnTop(true);
            waveSfv.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        }
        waveView.setLine_offset(42);
        requestStoragePermission();
    }


    //@OnClick({R.id.switchbtn,R.id.play,R.id.socreaudio})
    void click(View view){
        switch (view.getId()) {
            case R.id.switchbtn:
            if (waveCanvas == null || !waveCanvas.isRecording) {
                status.setText("录音中...");
                switchBtn.setText("停止录音");
                waveSfv.setVisibility(View.VISIBLE);
                waveView.setVisibility(View.INVISIBLE);
                initAudio();
                startAudio();

            } else {
                status.setText("停止录音");
                switchBtn.setText("开始录音");
                waveCanvas.Stop();
                waveCanvas = null;
                initWaveView();
            }
                break;
            case R.id.play:
                   onPlay(0);//播放 从头开始播放
                break;
            case R.id.socreaudio:
                float sim = 0;
                try {
                    // new FileInputStream(new File(DATA_DIRECTORY + mFileName + ".wav"))
                    sim = MusicSimilarityUtil.getScoreByCompareFile(getResources().getAssets().open("coku1.wav"), getResources().getAssets().open("coku2.wav"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(WaveActivity.this,sim+"", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void  initWaveView(){
     loadFromFile();
    }

    File mFile;
    Thread mLoadSoundFileThread;
    SoundFile mSoundFile;
    boolean mLoadingKeepGoing;
    SamplePlayer mPlayer;
    /** 载入wav文件显示波形 */
    private void loadFromFile() {
        try {
            Thread.sleep(300);//让文件写入完成后再载入波形 适当的休眠下
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mFile = new File(U.DATA_DIRECTORY + mFileName + ".wav");
        mLoadingKeepGoing = true;
        // Load the sound file in a background thread
        mLoadSoundFileThread = new Thread() {
            public void run() {
                try {
                    mSoundFile = SoundFile.create(mFile.getAbsolutePath(),null);
                    if (mSoundFile == null) {
                        return;
                    }
                    mPlayer = new SamplePlayer(mSoundFile);
                } catch (final Exception e) {
                    e.printStackTrace();
                    return;
                }
                if (mLoadingKeepGoing) {
                    Runnable runnable = new Runnable() {
                        public void run() {
                            finishOpeningSoundFile();
                            waveSfv.setVisibility(View.INVISIBLE);
                            waveView.setVisibility(View.VISIBLE);
                        }
                    };
                    WaveActivity.this.runOnUiThread(runnable);
                }
            }
        };
        mLoadSoundFileThread.start();
    }



    float mDensity;
    /**waveview载入波形完成*/
    private void finishOpeningSoundFile() {
        waveView.setSoundFile(mSoundFile);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mDensity = metrics.density;
        waveView.recomputeHeights(mDensity);
    }

    /**
     * 开始录音
     */
    private void startAudio(){
        waveCanvas = new WaveCanvas();
        waveCanvas.baseLine = waveSfv.getHeight() / 2;
        waveCanvas.Start(audioRecord, recBufSize, waveSfv, mFileName, U.DATA_DIRECTORY, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return true;
            }
        });
    }

    /**
     * 初始化权限
     */
    public void initPermission(){
        //WaveActivityPermissionsDispatcher.initAudioWithCheck(this);
        /*AndPermission.with(this)
                .runtime()
                .permission(new String[]{Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE,
                        Permission.RECORD_AUDIO})
                .start();*/

    }


    /**
     * 初始化录音  申请录音权限
     */
//    @NeedsPermission({Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
    public void initAudio(){
        recBufSize = AudioRecord.getMinBufferSize(FREQUENCY,
                CHANNELCONGIFIGURATION, AUDIOENCODING);// 录音组件
        audioRecord = new AudioRecord(AUDIO_SOURCE,// 指定音频来源，这里为麦克风
                FREQUENCY, // 16000HZ采样频率
                CHANNELCONGIFIGURATION,// 录制通道
                AUDIO_SOURCE,// 录制编码格式
                recBufSize);// 录制缓冲区大小 //先修改
        U.createDirectory();
    }




//    @OnShowRationale({Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
/*    void showRationaleForRecord(final PermissionRequest request){
        new AlertDialog.Builder(this)
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage("是否开启录音权限")
                .show();
    }*/

//    @OnPermissionDenied({Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRecordDenied(){
        Toast.makeText(WaveActivity.this,"拒绝录音权限将无法进行挑战", Toast.LENGTH_LONG).show();
    }

//    @OnNeverAskAgain({Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
    void onRecordNeverAskAgain() {
        /*new AlertDialog.Builder(this)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 2016/11/10 打开系统设置权限
                        dialog.cancel();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage("您已经禁止了录音权限,是否现在去开启")
                .show();*/
    }

    private int mPlayStartMsec;
    private int mPlayEndMsec;
    private final int UPDATE_WAV = 100;
    /**播放音频，@param startPosition 开始播放的时间*/
    private synchronized void onPlay(int startPosition) {
        if (mPlayer == null)
            return;
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            updateTime.removeMessages(UPDATE_WAV);
        }
            mPlayStartMsec = waveView.pixelsToMillisecs(startPosition);
            mPlayEndMsec = waveView.pixelsToMillisecsTotal();
            mPlayer.setOnCompletionListener(new SamplePlayer.OnCompletionListener() {
                @Override
                public void onCompletion() {
                    waveView.setPlayback(-1);
                    updateDisplay();
                    updateTime.removeMessages(UPDATE_WAV);
                    Toast.makeText(getApplicationContext(),"播放完成", Toast.LENGTH_LONG).show();
                }
            });
            mPlayer.seekTo(mPlayStartMsec);
            mPlayer.start();
            Message msg = new Message();
            msg.what = UPDATE_WAV;
            updateTime.sendMessage(msg);
    }

    Handler updateTime = new Handler() {
        public void handleMessage(Message msg) {
            updateDisplay();
            updateTime.sendMessageDelayed(new Message(), 10);
        };
    };

    /**更新upd
     * ateview 中的播放进度*/
    private void updateDisplay() {
            int now = mPlayer.getCurrentPosition();// nullpointer
            int frames = waveView.millisecsToPixels(now);
            waveView.setPlayback(frames);//通过这个更新当前播放的位置
            if (now >= mPlayEndMsec ) {
                waveView.setPlayFinish(1);
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.pause();
                    updateTime.removeMessages(UPDATE_WAV);
                }
            }else{
                waveView.setPlayFinish(0);
            }
            waveView.invalidate();//刷新整个视图
    }

    /**
     * Requesting multiple permissions (storage and location) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WaveActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}
