/**
 * © 2019 www.youxuepai.com
 * @file name：TTSActivity.java.java
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2019-7-25下午7:00:40
 * @version 1.0
 */
package com.noahedu.demo.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.noahedu.common.http.network.GsonHelper;
import com.noahedu.common.http.network.RequestResult;
import com.noahedu.common.util.DeviceUtils;
import com.noahedu.common.util.ImageUtils;
import com.noahedu.common.util.LogUtils;
import com.noahedu.common.util.StringUtils;
import com.noahedu.demo.R;
import com.noahedu.demo.adapter.XAdapter;
import com.noahedu.demo.contants.Constants;
import com.noahedu.demo.model.NetModel;
import com.noahedu.demo.model.VideoModel;
import com.noahedu.demo.utils.ResouceBean;
import com.noahedu.person.health.engine.Content;
import com.noahedu.person.health.engine.Engine;
import com.socks.library.KLog;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *  * © 2019 www.youxuepai.com
 *  名称：TTSActivity
 *  描述：简单描述该类的作用
 * @class name：TTSActivity
 * @anthor : daisg
 * @time 2019-7-25下午7:00:40
 * @version V1.0
 */
public class OkhttpActivity extends BaseActivity implements RequestResult{

    private static String TAG = OkhttpActivity.class.getSimpleName();
    private static String BASE_URL = "https://resource.youxuepai.com/ures/";
    private static String GET_BAATCH_URL = BASE_URL+"svc/famouseCource/getBatchUrlByCodes";
    //?modelCode=V2C&machine_no=1234567890&open=1&ratio=720&data=["this is the way"];
    private  static String path = Environment.getExternalStorageDirectory().getPath()+"/幼教/成语故事.bin";
    private ListView lv_app_list;
    private XAdapter<Content.Item> mAppAdapter;
    private Context ctx;
    public Handler mHandler = new Handler();

    OkHttpClient okHttpClient = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        ctx =this;
        LogUtils.v(TAG,DeviceUtils.getProductName());
        lv_app_list = (ListView) findViewById(R.id.lv_app_list);
        mAppAdapter = new XAdapter<Content.Item>(R.layout.item_app_info) {
            @Override
            public void bindView(XAdapter.ViewHolder holder, Content.Item obj) {
                Drawable drawable = ImageUtils.byteToDrawable(obj.logo);
                holder.setImageDrawable(R.id.iv_app_icon,drawable);
                holder.setText(R.id.tv_app_name,obj.name);
            }
        };
        lv_app_list.setAdapter(mAppAdapter);
        lv_app_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Content.Item appInfo=(Content.Item) lv_app_list.getItemAtPosition(position);
                launchAPK(appInfo);
            }

        });
        requestStoragePermission();
    }

    /**
     * 启动第三方apk
     * 内嵌在当前apk内打开，每次启动都是新的apk,你会发现打开了两个apk
     * XXXXX ： 包名
     */
    public  void launchAPK(Content.Item appInfo) {
        String vodeoId = appInfo.videoId;
        String className = appInfo.cacheFile;
        //vodeoId = "日本明治维新";
        getResource(appInfo);
        NetModel.getInstance().getTencentVideo(vodeoId, this);
    }
    private void initAppList(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                int hd = Engine.getInstance().nativeOpenPackages(path);
                if (hd !=0)
                {
                    Content[] contents = Engine.getInstance().nativeReadContent(hd,Engine.getInstance().GetContentCacheFile(ctx));

                    final List<Content.Item> listItem = new ArrayList<>();
                    if (contents !=null &&contents.length >0) {
                        for (Content a : contents) {
                            for (Content.Item b : a.items) {
                                listItem.add(b);
                            }
                        }
                    }
                    Engine.getInstance().nativeClosePackages(hd);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAppAdapter.setData(listItem);
                        }
                    });

                }
                else{
                    com.noahedu.common.filedownloader.base.Log.v(TAG,"open "+path+" file fail ");

                }
            }
        }.start();
    }

    @Override
    public void onRequestSuc(String type, String str) {
        switch (type) {
            case Constants.GET_TENCENT_VIDEO: //区块点读
                KLog.json(str);
                if (!StringUtils.isEmpty(str)) {
                    VideoModel  vm= GsonHelper.fromJson(str, VideoModel.class);
                    if (null != vm && "1".equals(vm.getStatus())) {
                        VideoModel.ValueEntity value = vm.getValue();

                        startActivity(
                                value.buildIntent(
                                        /* context= */ this));
                    } else {
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestErr(String type, String msg) {
        switch (type) {
            case Constants.GET_TENCENT_VIDEO: //区块点读
                KLog.json(msg);
                break;
            default:
                break;
        }
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
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
// check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();

                            initAppList();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(OkhttpActivity.this);
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

    /*
    *使用okhttp 与gson 获取数据*/
    private void getResource(Content.Item item)
    {
    	String url = "https://resapi.youxuepai.com/search/api/searchsource?productids=249&keyword="+"山村咏怀";
    	Log.v(TAG,"url:"+url);
    	final Request request = new Request.Builder()
    	        .url(url)
    	        .get()//默认就是GET请求，可以不写
    	        .build();
    	Call call = okHttpClient.newCall(request);
    	call.enqueue(new Callback() {
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
    	        Log.d(TAG, "onFailure: "+arg1.getMessage());
			}

			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				// TODO Auto-generated method stub
                final  String res = response.body().string();
                Log.d(TAG, "onResponse: " + res);
                try {
                    Gson gson = new Gson() ;
                    ResouceBean xqeBean = gson.fromJson(res, ResouceBean.class);
                    Log.v(TAG,xqeBean.getKeys().get(0).getUrl());
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
    	});
    }
}
