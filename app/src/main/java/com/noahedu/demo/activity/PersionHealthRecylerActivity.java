package com.noahedu.demo.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.noahedu.common.util.FileUtils;
import com.noahedu.common.util.LogUtils;
import com.noahedu.demo.R;
import com.noahedu.demo.adapter.QuickAdapter;
import com.noahedu.demo.adapter.QuickMultiSupport;
import com.noahedu.demo.adapter.QuickViewHolder;
import com.noahedu.demo.stack.IStick;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PersionHealthRecylerActivity extends BaseActivity  {
    private static String TAG = PersionHealthRecylerActivity.class.getSimpleName();
    //?modelCode=V2C&machine_no=1234567890&open=1&ratio=720&data=["this is the way"];
    private  static String path = Environment.getExternalStorageDirectory().getPath()+"/幼教/";
    private static String suffix = ".bin";
    private RecyclerView lv_app_list;
    private QuickAdapter<IViewType> mAppAdapter;
    private Context ctx;
    private GridLayoutManager mLayoutManager;
    public Handler mHandler = new Handler();
    LinkedList<IViewType> mListItems = new LinkedList<>();
    private List<IViewType> mData = new ArrayList<>();
    private QuickMultiSupport<IViewType> mQuickSupport;

    /**
     * 多条目的ViewType
     */

    public interface IViewType {
        int getItemViewType();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyleview_list);
        initData();
        initViews();
    }

    /**
     * 多条目的ViewType
     */
    private void initData() {
        final List<String> listItem = FileUtils.getFilesAllName(path,suffix);
        if (listItem != null) {
            for (String item : listItem) {
                add(item);
            }
        }
        // 多条目支持
        mQuickSupport = new QuickMultiSupport<IViewType>() {
            // 条目总共两种类型
            @Override
            public int getViewTypeCount() {
                return 2;
            }

            // 根据不用的JavaBean返回不同的布局
            @Override
            public int getLayoutId(IViewType data) {
                if (data instanceof MultiBean) {
                    return R.layout.item;
                }
                else if (data instanceof Data) {
                    return R.layout.item_recyle_info;
                }

                return R.layout.item_recyle_info;
            }

            @Override
            public int getItemViewType(IViewType data) {
                return data.getItemViewType();
            }

            @Override
            public boolean isSpan(IViewType data) {
                // 是否占用一个条目，针对RecyclerView
                if (data instanceof MultiBean) {
                    return true;
                }
                return false;
            }
        };
    }


    private void initViews() {
        ListView listView = findViewById(R.id.list_view);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // ListView设置Adapter
        listView.setAdapter(new CommAdapter(this, mData, mQuickSupport));
        // RecyclerView设置Adapter
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new CommAdapter(this, mData, mQuickSupport));
    }

    /**
     * 启动第三方apk
     * 内嵌在当前apk内打开，每次启动都是新的apk,你会发现打开了两个apk
     * XXXXX ： 包名
     */
    public  void launchAPK(Data appInfo) {
        Intent intent = new Intent(PersionHealthRecylerActivity.this,PersionHealthActivity.class);
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
               /* mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAppAdapter.setData(mListItems);
                    }
                });*/
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
        mData.add(data);
    }

    public class Data  implements IViewType{
        String showName;
        String pathName;

        @Override
        public String toString() {
            return showName;
        }

        @Override
        public int getItemViewType() {
            return 0;
        }
    }

    public class MultiBean implements IViewType {
        public String name;

        @Override
        public int getItemViewType() {
            return 1;
        }


        @Override
        public String toString() {
            return name;
        }
    }



    class CommAdapter extends QuickAdapter<IViewType> implements IStick {
        public CommAdapter(Context context, List<IViewType> data, int layoutId) {
            super(context,layoutId, data, null);
        }

        public CommAdapter(Context context, List<IViewType> data, QuickMultiSupport<IViewType> support) {
            super(context,0, data, support);
        }

        @Override
        protected void convert(QuickViewHolder holder, final IViewType item, final int position) {
            holder.setText(R.id.lv_name, item.toString());
            holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击移除当前条目
                    //Toast.makeText(MainActivity.this, item.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }

        // 下面是悬浮View的测试
        @Override
        public int getStickPosition() {
            // 指定第10个位置要悬浮
            return 10;
        }

        @Override
        public int getStickViewType() {
            // 指定悬浮的条目类型
            return getItemViewType(getStickPosition());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PersionHealthRecylerActivity.this);
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
