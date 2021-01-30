package com.noahedu.demo.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;


import com.noahedu.common.jsbridge.BridgeHandler;
import com.noahedu.common.jsbridge.BridgeWebView;
import com.noahedu.common.jsbridge.CallBackFunction;
import com.noahedu.common.jsbridge.DefaultHandler;
import com.noahedu.common.util.StringUtils;
import com.noahedu.demo.R;
import com.socks.library.KLog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 所有显示H5的页面，都可以使用这个activity
 */
public class WebViewActivity extends BaseActivity {
    public static Activity webViewActivity;
    private BridgeWebView bridgeWebView;
    public CallBackFunction resetSodukuCallBackFunction;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private IntentFilter myFilter;
    private String baseUrl;


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webViewActivity = this;
        bridgeWebView = (BridgeWebView)findViewById(R.id.jsBridgeWebView);

        WebSettings webSettings = bridgeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        webSettings.setSupportZoom(true); //支持屏幕缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false); //不显示webview缩放按钮

        initData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (bridgeWebView != null) {
            bridgeWebView.onResume();
            //恢复pauseTimers状态
            bridgeWebView.resumeTimers();
            bridgeWebView.reload();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bridgeWebView != null) {
            bridgeWebView.onPause();
            //它会暂停所有webview的layout，parsing，javascripttimer。降低CPU功耗
            bridgeWebView.pauseTimers();
        }
    }

    @Override
    protected void onDestroy() {
        if (bridgeWebView != null) {
            bridgeWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            bridgeWebView.clearHistory();

            ((ViewGroup) bridgeWebView.getParent()).removeView(bridgeWebView);
            bridgeWebView.destroy();
            bridgeWebView = null;
        }
        super.onDestroy();
    }

    public void initData() {
        Intent it = getIntent();
        String url = it.getStringExtra("url");
        if(StringUtils.isEmpty(url)){
            url = getBaseUrl(2);
        }
        //String url = getBaseUrl(2);
        if (!TextUtils.isEmpty(url)) {
            bridgeWebView.setDefaultHandler(new DefaultHandler());
            bridgeWebView.setWebChromeClient(new WebChromeClient() {
                //获取网站标题
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    KLog.v("标题在这里"+title);
                    //mtitle.setText(title);
                }


                //获取加载进度
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                   /* if (newProgress < 100) {
                        String progress = newProgress + "%";
                        loading.setText(progress);
                    } else if (newProgress == 100) {
                        String progress = newProgress + "%";
                        loading.setText(progress);
                    }*/
                }
            });
            bridgeWebView.loadUrl(url);
        } else {
            //GToast.show(WebViewActivity.this, "未找到H5地址");
        }

       //js调用Android的方法，并获取数据
        bridgeWebView.registerHandler("resetsoduku", new BridgeHandler() {
            @Override
            public void handler(String s, CallBackFunction callBackFunction) {
                WebViewActivity.this.resetSodukuCallBackFunction = callBackFunction;
                resetSoduku();
            }
        });
        /*
        //js调用Android的方法，并获取数据
        bridgeWebView.registerHandler("startScan", new BridgeHandler() {
            @Override
            public void handler(String s, CallBackFunction callBackFunction) {
                WebViewActivity.this.startScanCallBackFunction = callBackFunction;
                KLog.e("huandhsj","----js调用原生开始扫码-----> ");
            }
        });

        *//**
         * js向前端获取机器码
         *//*
        bridgeWebView.registerHandler("getMachineCode", new BridgeHandler() {
            @Override
            public void handler(String s, CallBackFunction callBackFunction) {
                WebViewActivity.this.getMachineCodeCallBackFunction = callBackFunction;
                KLog.e("huandhsj","----js调用原生获取机器码-----> ");
            }
        });

        *//**
         * 干掉webview
         *//*
        bridgeWebView.registerHandler("finishWebView", new BridgeHandler() {
            @Override
            public void handler(String s, CallBackFunction callBackFunction) {
                KLog.e("huandhsj","----干掉webview-----> ");
                WebViewActivity.this.finish();
            }
        });*/
      /*  resetSoduku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //android 调用js方法
                bridgeWebView.callHandler("functionInJs", "Android调用js", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        KLog.e("huangjialimn", "---js返回过来的数据--> " + data);
                    }
                });
            }
        });*/
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && bridgeWebView.canGoBack()) {
            bridgeWebView.goBack();//返回上个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);//退出H5界面
    }

    @Override
    public void onBackPressed() {
        if (bridgeWebView.canGoBack()) {
            bridgeWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    /*    switch (requestCode) {
            case Constants.TAKE_PICTURE_REQUEST_CODE: //拍照成功

                if (null == mUploadCallbackAboveL && null == mUploadMessage) {
                    return;
                }
                if (mUploadCallbackAboveL != null) {
                    onActivityResultAboveL(requestCode, resultCode, data);
                } else if (null != mUploadMessage) {
                    if (null != data) {
                        String imagePath = data.getStringExtra("result");
                        if (!TextUtils.isEmpty(imagePath)) {
                            Uri uri = Uri.fromFile(new File(imagePath));
                            mUploadMessage.onReceiveValue(uri);
                        }
                    }
                }

                break;
            case Constants.START_SCAN_REQUEST_CODE: //扫码成功
                if (null != data) {
                    String str = data.getStringExtra(Intents.Scan.RESULT);
                    if (null != startScanCallBackFunction && !TextUtils.isEmpty(str)) {
                        startScanCallBackFunction.onCallBack(str);
                    }
                }
                break;
        }*/
    }


    private final static int FILECHOOSER_RESULTCODE = 100;// 表单的结果回调</span>
    private Uri imageUri;

    @SuppressWarnings("null")
    @TargetApi(Build.VERSION_CODES.BASE)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (null != data && resultCode == Activity.RESULT_OK) {
            String imagePath = data.getStringExtra("result");
            if (!TextUtils.isEmpty(imagePath)) {
                Uri uri = Uri.fromFile(new File(imagePath));
                results = new Uri[]{uri};
            }
        } else {
            Uri uri = Uri.fromFile(new File(""));
            results = new Uri[]{uri};
        }
        if (results != null) {
            if (null != mUploadCallbackAboveL) {
                mUploadCallbackAboveL.onReceiveValue(results);
                mUploadCallbackAboveL = null;
            }
        }
        return;
    }

    private String getBaseUrl(int level)
    {
        //1、普通的时间转换
        String string = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
        //2、日历类的时间操作
        Calendar calendar = Calendar.getInstance();

        String url = "http://www.cn.sudokupuzzle.org/online2.php?"+"nd="+level+
        "&y="+calendar.get(Calendar.YEAR) +"&m="+(calendar.get(Calendar.MONTH)+1)
                +"&d="+calendar.get(Calendar.DATE);
        return url;
    }

    /**
     * 开始拍照
     */
    public void resetSoduku() {
        if(bridgeWebView != null)
        {
            bridgeWebView.reload();
        }

        resetSodukuCallBackFunction.onCallBack(null);
    }


}
