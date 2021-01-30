package com.noahedu.demo.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.noahedu.demo.R;
import com.noahedu.demo.adapter.XAdapter;
import com.noahedu.demo.mybatis.core.MybatisUtils;
import com.noahedu.demo.utils.ApkUtils;
import com.noahedu.demo.utils.MyAppInfo;

import java.io.IOException;
import java.util.List;

public class AppActivity extends BaseActivity {
    private ListView lv_app_list;
    private XAdapter<MyAppInfo> mAppAdapter;
    public Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        lv_app_list = (ListView) findViewById(R.id.lv_app_list);

        mAppAdapter = new XAdapter<MyAppInfo>(R.layout.item_app_info) {
            @Override
            public void bindView(ViewHolder holder, MyAppInfo obj) {
                holder.setImageDrawable(R.id.iv_app_icon,obj.getAppIcon());
                holder.setText(R.id.tv_app_name,obj.getAppName());
            }
        };

        lv_app_list.setAdapter(mAppAdapter);
        lv_app_list.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                MyAppInfo appInfo=(MyAppInfo) lv_app_list.getItemAtPosition(position);
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
                final List<MyAppInfo> appInfos = ApkUtils.queryAppInfo(AppActivity.this);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAppAdapter.setData(appInfos);
                    }
                });
            }
        }.start();
    }


/*
    class AppAdapter extends BaseAdapter {

        List<MyAppInfo> myAppInfos = new ArrayList<MyAppInfo>();

        public void setData(List<MyAppInfo> myAppInfos) {
            this.myAppInfos = myAppInfos;
            Log.v("ii","myAppInfos.size="+ myAppInfos.size());
            notifyDataSetChanged();
        }

        public List<MyAppInfo> getData() {
            return myAppInfos;
        }

        @Override
        public int getCount() {
            if (myAppInfos != null && myAppInfos.size() > 0) {
                return myAppInfos.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (myAppInfos != null && myAppInfos.size() > 0) {
                return myAppInfos.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            MyAppInfo myAppInfo = myAppInfos.get(position);
            if (convertView == null) {
                mViewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_app_info, null);
                mViewHolder.iv_app_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
                mViewHolder.tx_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            mViewHolder.iv_app_icon.setImageDrawable(myAppInfo.getAppIcon());
            mViewHolder.tx_app_name.setText(myAppInfo.getAppName());
            return convertView;
        }

        class ViewHolder {
            ImageView iv_app_icon;
            TextView tx_app_name;
        }
    }*/


}
