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
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.noahedu.common.filedownloader.base.Log;
import com.noahedu.common.http.network.GsonHelper;
import com.noahedu.common.http.network.RequestResult;
import com.noahedu.common.util.DeviceUtils;
import com.noahedu.common.util.ImageUtils;
import com.noahedu.common.util.LogUtils;
import com.noahedu.common.util.StringUtils;
import com.noahedu.demo.R;
import com.noahedu.demo.adapter.QuickAdapter;
import com.noahedu.demo.adapter.QuickViewHolder;
import com.noahedu.demo.contants.Constants;
import com.noahedu.demo.model.NetModel;
import com.noahedu.demo.model.VideoModel;
import com.noahedu.person.health.engine.Content;
import com.noahedu.person.health.engine.Engine;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

public class PersionHealthActivity extends BaseActivity implements RequestResult {
    private static String TAG = PersionHealthActivity.class.getSimpleName();
    private static String BASE_URL = "https://resource.youxuepai.com/ures/";
    private static String GET_BAATCH_URL = BASE_URL+"svc/famouseCource/getBatchUrlByCodes";
    //?modelCode=V2C&machine_no=1234567890&open=1&ratio=720&data=["this is the way"];
    private  static String path = Environment.getExternalStorageDirectory().getPath()+"/幼教/成语故事.bin";
    private ListView lv_app_list;
    private QuickAdapter<Content.Item> mAppAdapter;
    private Context ctx;
    public Handler mHandler = new Handler();
    private GridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        ctx =this;
        LogUtils.v(TAG,DeviceUtils.getProductName());
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        lv_app_list = (ListView) findViewById(R.id.lv_app_list);
      /*  mLayoutManager = new GridLayoutManager(this,3);
        lv_app_list.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        lv_app_list.setHasFixedSize(true);*/
        mAppAdapter = new QuickAdapter<Content.Item>(this,R.layout.item_app_info) {
            @Override
            protected void convert(QuickViewHolder holder, Content.Item obj, int position) {
                Drawable drawable = ImageUtils.byteToDrawable(obj.logo);
                holder.setImageDrawable(R.id.iv_app_icon,drawable);
                holder.setText(R.id.tv_app_name,obj.name);
            }
        };
        lv_app_list.setAdapter(mAppAdapter);
        lv_app_list.setOnItemClickListener(new OnItemClickListener(){
			@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
                LogUtils.v("onItemClick:view"+view+"position:"+position);
                Content.Item appInfo=(Content.Item) lv_app_list.getItemAtPosition(position);
				launchAPK(appInfo);
			}
        });
        requestStoragePermission();
        //initAppList();

/*        String vodeoId = "日本明治维新";
        String className = "日本明治维新";

        NetModel.getInstance().getTencentVideo(vodeoId, this);*/
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
                                LogUtils.v(b.videoId);
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
                    Log.v(TAG,"open "+path+" file fail ");

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

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        processData(intent);
    }

    private void processData(Intent intent) {
        String action = intent.getAction();
        Uri uri = intent.getData();

        if (uri != null) {

            String scheme= uri.getScheme();

            String host=uri.getHost();

            String port=uri.getPort()+"";

            String path=uri.getPath();

            String query=uri.getQuery();
            Log.v(TAG,"processData PATH:"+path);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(PersionHealthActivity.this);
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
