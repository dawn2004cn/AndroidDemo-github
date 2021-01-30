package com.noahedu.demo.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.noahedu.dao.ClassUtils;
import com.noahedu.demo.R;
import com.noahedu.demo.adapter.XAdapter;
import com.noahedu.demo.utils.MyAppInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClassActivity extends BaseActivity{
    private ListView lv_app_list;
    private XAdapter<Class<?>> mAppAdapter;
    public Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        lv_app_list = (ListView) findViewById(R.id.lv_app_list);

        mAppAdapter = new XAdapter<Class<?>>(R.layout.item_app_info) {
            @Override
            public void bindView(ViewHolder holder, Class<?> obj) {
                //holder.setImageDrawable(R.id.iv_app_icon,obj.getAppIcon());
                holder.setText(R.id.tv_app_name,obj.getName());
            }
        };

        lv_app_list.setAdapter(mAppAdapter);
        lv_app_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Class<?> appInfo=(Class<?>) lv_app_list.getItemAtPosition(position);
                //launchAPK(appInfo);
            }

        });

        initAppList();
    }

    /**
     * 启动第三方apk
     * 内嵌在当前apk内打开，每次启动都是新的apk,你会发现打开了两个apk
     * XXXXX ： 包名
     */
    public  void launchAPK(MyAppInfo appInfo) {

        String packageName = appInfo.getPackageName();
        String className = appInfo.getActivityName();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName(packageName, className);
        intent.setComponent(cn);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void initAppList(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                final Set<Class<?>> appInfos = ClassUtils.getClassSet(getPackageName());
                List<Class<?>> appInfo = new ArrayList<>(appInfos);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAppAdapter.setData(appInfo);
                    }
                });
            }
        }.start();
    }

}
