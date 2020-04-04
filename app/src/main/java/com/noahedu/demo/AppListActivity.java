package com.noahedu.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.noahedu.common.http.network.GsonHelper;
import com.noahedu.common.http.network.RequestResult;
import com.noahedu.common.util.ImageUtils;
import com.noahedu.common.util.StringUtils;
import com.noahedu.demo.adapter.XAdapter;
import com.noahedu.demo.contants.Constants;
import com.noahedu.demo.model.NetModel;
import com.noahedu.demo.model.VideoModel;
import com.noahedu.person.health.engine.Content;
import com.noahedu.person.health.engine.Engine;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

public class AppListActivity extends BaseActivity implements RequestResult {
    private static String TAG = AppListActivity.class.getSimpleName();
    private static String BASE_URL = "https://resource.youxuepai.com/ures/";
    private static String GET_BAATCH_URL = BASE_URL+"svc/famouseCource/getBatchUrlByCodes";
    //?modelCode=V2C&machine_no=1234567890&open=1&ratio=720&data=["this is the way"];
    private  static String path = Environment.getExternalStorageDirectory().getPath()+"/幼教/成语故事.bin";
    private ListView lv_app_list;
    private XAdapter<Content.Item> mAppAdapter;
    private Context ctx;
    public Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        ctx =this;
        lv_app_list = (ListView) findViewById(R.id.lv_app_list);
        mAppAdapter = new XAdapter<Content.Item>(R.layout.item_app_info) {
            @Override
            public void bindView(ViewHolder holder, Content.Item obj) {
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
                Content.Item appInfo=(Content.Item) lv_app_list.getItemAtPosition(position);
				launchAPK(appInfo);
			}
        	
        });
        initAppList();
    }

    /**
     * 启动第三方apk
     * 内嵌在当前apk内打开，每次启动都是新的apk,你会发现打开了两个apk
     * XXXXX ： 包名
     */
    public  void launchAPK(Content.Item appInfo) {
        String vodeoId = appInfo.videoId;
        String className = appInfo.cacheFile;

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

                    for(Content a : contents){
                        for (Content.Item b:a.items)
                        {
                            listItem.add(b);
                        }
                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAppAdapter.setData(listItem);
                        }
                    });

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
                break;
            default:
                break;
        }
    }


}
