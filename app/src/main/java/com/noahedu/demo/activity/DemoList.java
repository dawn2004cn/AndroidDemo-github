package com.noahedu.demo.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.noahedu.audiorecorder.WaveActivity;
import com.noahedu.common.filedownloader.base.Log;
import com.noahedu.common.util.UserUtils;
import com.noahedu.countdowntimer.CountDownActivity;
import com.noahedu.demo.R;
import com.noahedu.demo.adapter.XAdapter;
import com.noahedu.demo.service.TopWindowService;
import com.noahedu.demo.svga.AnimationSvgaActivity;
import com.noahedu.gpuimage.activity.ActivityMain;

import java.util.LinkedList;
import java.util.List;

/**
 * demo list list
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-6-17
 */
public class DemoList extends BaseActivity {
    public static final String TAG = DemoList.class.getSimpleName();

    private XAdapter<Data> mAdapter;
    private ListView demoListView;
    LinkedList<Data> mListItems = null;

    public Handler mHandler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.demo_list);

        demoListView = (ListView) findViewById(R.id.simpleListView);

        ActionBar bar = getActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME);

        requestOverlayPermission();

        init();
        UserUtils.UserInfo  userInfo = UserUtils.getUserInfo(this);
        Log.v(TAG,userInfo.toString());
    }

    void init() {
        initData();
        mAdapter = new XAdapter<Data>(R.layout.item_app_info) {
            @Override
            public void bindView(XAdapter.ViewHolder holder, Data obj) {
                //holder.setImageDrawable(R.id.iv_app_icon,obj.getAppIcon());
                holder.setText(R.id.tv_app_name,obj.showName);
            }
        };

        demoListView.setAdapter(mAdapter);
        demoListView.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Data appInfo=(Data) demoListView.getItemAtPosition(position);

                Intent intent = new Intent(DemoList.this, appInfo.clazz);
                startActivity(intent);
            }

        });
        initList();
    }

    void initData() {
        mListItems = new LinkedList<>();
        add("ImageSDCardCache Demo", ImageSDCardCacheDemo.class);
        add("ImageCache Demo", ImageCacheDemo.class);
        add("DropDownListView Demo", DropDownListViewDemo.class);
        add("onBottom onTop ScrollView Demo", BorderScrollViewDemo.class);
        add("DownloadManager Demo", DownloadManagerDemo.class);
        add("SearchView Demo", SearchViewDemo.class);
        add("ViewPager Multi Fragment Demo", ViewPagerMulTiFragmentDemo.class);
        add("Slide One Page Gallery Demo", SlideOnePageGalleryDemo.class);
        add("ViewPager Demo", ViewPagerDemo.class);
        add("Service Demo", ServiceDemo.class);
        add("BroadcastReceiver Demo", BroadcastReceiverDemo.class);
        add("ImageFilter demo", ImageFilterDemo.class);
        add("loadImage demo ", LoadBitmapDemo.class);
        add("download file demo ", DownloadFileDemo.class);
        add("dingdang demo ", DingDangActivity.class);
        add("persion demo", PersionHealthListActivity.class);
        add("appList demo ", AppActivity.class);
        add("xqe demo", XQEActivity.class);
        add("gpu filter demo ", ActivityMain.class);
        add("webview demo ", WebViewActivity.class);
        add("wavrecorder demo", WaveActivity.class);
        add("class demo", ClassActivity.class);
        add("Ding demo", DingActivity.class);
        add("okhttp demo", OkhttpActivity.class);
        add("wave demo", WaveActivity.class);
        add("svga demo", AnimationSvgaActivity.class);
        add("countdown time demo", CountDownActivity.class);
        add("recyleview demo", PersionHealthRecylerActivity.class);
        add("test filter",ImageFilterTest.class);
    }
    void initList(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                final List<Data> appInfos = mListItems;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(appInfos);
                    }
                });
            }
        }.start();
    }

    private static final int REQUEST_OVERLAY = 4444;

    /**
     * Requesting camera permission
     * This uses single permission model from dexter
     * Once the permission granted, opens the camera
     * On permanent denial opens settings dialog
     */
    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(DemoList.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_OVERLAY);
            } else {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OVERLAY) {
            startService(new Intent(DemoList.this,
                    TopWindowService.class));
        }
    }

    private void add(String showName, Class<?> clazz) {
        Data data = new Data();
        data.clazz = clazz;
        data.showName = showName;
        mListItems.add(data);
    }


    private class Data {
        String showName;
        Class<?> clazz;
    }

}
