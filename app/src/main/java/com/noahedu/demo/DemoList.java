package com.noahedu.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.noahedu.audiorecorder.MainActivity;
import com.noahedu.audiorecorder.WaveActivity;
import com.noahedu.baidu.tts.sample.BaiduTTSActivity;
import com.noahedu.common.filedownloader.base.Log;
import com.noahedu.common.player.Player;
import com.noahedu.demo.utils.SimplePlayer;
import com.noahedu.gpuimage.activity.ActivityMain;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * demo list list
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-6-17
 */
public class DemoList extends BaseActivity {

    public static final String    TAG      = "DemoList";

    private static final String[] mStrings = { "ImageSDCardCache Demo", "ImageCache Demo", "DropDownListView Demo",
            "onBottom onTop ScrollView Demo", "DownloadManager Demo", "SearchView Demo",
            "ViewPager Multi Fragment Demo", "Slide One Page Gallery Demo", "ViewPager Demo",
            "Service Demo","BroadcastReceiver Demo" ,"ImageFilter demo"  ,
            "loadImage demo "  ,"download file demo " ,"dingdang demo " ,
            "persion demo "  ,"baidutts demo ","appList demo "  ,"xqe demo "  ,
            "gpu filter demo ","webview demo " ,"wavrecorder demo "  };

    private static final int      total    = mStrings.length - 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.demo_list);

        ActionBar bar = getActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME);

        LinkedList<String> mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);

        ListView demoListView = (ListView)findViewById(R.id.simpleListView);
        demoListView.setAdapter(adapter);
        demoListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(DemoList.this, ImageSDCardCacheDemo.class);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(DemoList.this, ImageCacheDemo.class);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(DemoList.this, DropDownListViewDemo.class);
                    startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(DemoList.this, BorderScrollViewDemo.class);
                    startActivity(intent);
                } else if (position == 4) {
                    Intent intent = new Intent(DemoList.this, DownloadManagerDemo.class);
                    startActivity(intent);
                } else if (position == 5) {
                    Intent intent = new Intent(DemoList.this, SearchViewDemo.class);
                    startActivity(intent);
                } else if (position == 6) {
                    Intent intent = new Intent(DemoList.this, ViewPagerMulTiFragmentDemo.class);
                    startActivity(intent);
                } else if (position == 7) {
                    Intent intent = new Intent(DemoList.this, SlideOnePageGalleryDemo.class);
                    startActivity(intent);
                } else if (position == 8) {
                    Intent intent = new Intent(DemoList.this, ViewPagerDemo.class);
                    startActivity(intent);
                } else if (position == 9) {
                    Intent intent = new Intent(DemoList.this, ServiceDemo.class);
                    startActivity(intent);
                } else if (position == 10) {
                    Intent intent = new Intent(DemoList.this, BroadcastReceiverDemo.class);
                    startActivity(intent);
                }else if (position == 11) {
                    Intent intent = new Intent(DemoList.this, ImageFilterDemo.class);
                    startActivity(intent);
                }else if (position == 12) {
                	Intent intent = new Intent(DemoList.this, LoadBitmapDemo.class);
                	startActivity(intent);
                }else if (position == 13) {
                	Intent intent = new Intent(DemoList.this, DownloadFileDemo.class);
                	startActivity(intent);
                }else if (position == 14) {
                	Intent intent = new Intent(DemoList.this, DingDangActivity.class);
                	startActivity(intent);
                }else if (position == 15) {
                    Intent intent = new Intent(DemoList.this, AppListActivity.class);
                    startActivity(intent);
                }else if (position == 16) {
                	Intent intent = new Intent(DemoList.this, BaiduTTSActivity.class);
                	startActivity(intent);
                }else if (position == 17) {
                	Intent intent = new Intent(DemoList.this, AppActivity.class);
                	startActivity(intent);
                }else if (position == 18) {
                    Intent intent = new Intent(DemoList.this, XQEActivity.class);
                    startActivity(intent);
                }else if (position == 19) {
                    Intent intent = new Intent(DemoList.this, ActivityMain.class);
                    startActivity(intent);
                }
                else if (position == 20) {
                    Intent intent = new Intent(DemoList.this, WebViewActivity.class);
                    startActivity(intent);
                }
                else if (position == total) {
                    Intent intent = new Intent(DemoList.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


}
