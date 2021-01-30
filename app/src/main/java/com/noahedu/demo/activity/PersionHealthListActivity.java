package com.noahedu.demo.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.noahedu.common.util.DeviceUtils;
import com.noahedu.common.util.FileUtils;
import com.noahedu.common.util.LogUtils;
import com.noahedu.common.util.StringUtils;
import com.noahedu.demo.R;
import com.noahedu.demo.adapter.XAdapter;

import java.util.LinkedList;
import java.util.List;

public class PersionHealthListActivity extends BaseActivity  {
    private static String TAG = PersionHealthListActivity.class.getSimpleName();
    //?modelCode=V2C&machine_no=1234567890&open=1&ratio=720&data=["this is the way"];
    private  static String path = Environment.getExternalStorageDirectory().getPath()+"/幼教/";
    private static String suffix = ".bin";
    private ListView lv_app_list;
    private XAdapter<Data> mAppAdapter;
    private Context ctx;
    public Handler mHandler = new Handler();
    LinkedList<Data> mListItems = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        ctx =this;
        LogUtils.v(TAG,DeviceUtils.getProductName());
        lv_app_list = (ListView) findViewById(R.id.lv_app_list);
        mAppAdapter = new XAdapter<Data>(R.layout.item_app_info) {
            @Override
            public void bindView(ViewHolder holder, Data obj) {
                //Drawable drawable = ImageUtils.byteToDrawable(obj.logo);
                //holder.setImageDrawable(R.id.iv_app_icon,drawable);
                holder.setText(R.id.tv_app_name,obj.showName);
            }
        };
        lv_app_list.setAdapter(mAppAdapter);
        lv_app_list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
                Data appInfo=(Data) lv_app_list.getItemAtPosition(position);
				launchAPK(appInfo);
			}
        	
        });
        requestStoragePermission();
        //initAppList();
    }

    /**
     * 启动第三方apk
     * 内嵌在当前apk内打开，每次启动都是新的apk,你会发现打开了两个apk
     * XXXXX ： 包名
     */
    public  void launchAPK(Data appInfo) {
        Intent intent = new Intent(PersionHealthListActivity.this,PersionHealthActivity.class);
        intent.putExtra("path",appInfo.pathName);
        startActivity(intent);

    }
    private void initAppList(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                final List<String> listItem = FileUtils.getFilesAllName(path,suffix);
                if (listItem != null) {
                    for (String item : listItem) {
                        add(item);
                    }
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAppAdapter.setData(mListItems);
                    }
                });
            }
        }.start();
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
    }

    private void add(String pathName) {
        Data data = new Data();
        data.pathName = pathName;

        int lastindex = pathName .lastIndexOf("/");
        //获取具体文件名称
        String showName= pathName.substring(lastindex+1,pathName.length() - suffix.length());

        data.showName = showName;
        LogUtils.v(showName);
        mListItems.add(data);
    }
    private class Data {
        String showName;
        String pathName;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PersionHealthListActivity.this);
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
