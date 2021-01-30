/**
 * © 2019 www.youxuepai.com
 * @file name：DingActivity.java
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2019-7-25下午7:00:40
 * @version 1.0
 */
package com.noahedu.demo.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.noahedu.common.http.network.RequestResult;
import com.noahedu.common.util.AssetsFileUtils;
import com.noahedu.common.util.LogUtils;
import com.noahedu.common.util.StringUtils;
import com.noahedu.demo.R;
import com.noahedu.demo.contants.Constants;
import com.noahedu.demo.model.NetModel;
import com.noahedu.demo.utils.DingDataBean;
import com.noahedu.demo.utils.RoboSysLogin;

import java.util.ArrayList;
import java.util.List;

/**
 *  * © 2019 www.youxuepai.com
 *  名称：DingActivity
 *  描述：简单描述该类的作用
 * @class name：DingActivity
 * @anthor : daisg
 * @time 2019-7-25下午7:00:40
 * @version V1.0
 */
public class DingActivity extends BaseActivity implements RequestResult {

    public static final String TAG                = DingActivity.class.getSimpleName();

    private ListView lv_app_list;
    private AppAdapter mAppAdapter;
    public Handler mHandler = new Handler();
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        //login
        NetModel.getInstance().roboSysLogin(this);

        lv_app_list = (ListView) findViewById(R.id.lv_app_list);
        mAppAdapter = new AppAdapter();
        lv_app_list.setAdapter(mAppAdapter);
        lv_app_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                DingDataBean.DataBean xqeBean=(DingDataBean.DataBean) lv_app_list.getItemAtPosition(position);
                launchData(xqeBean);
                //VodXqe.getInstance().openAlbumPage(xqeBean.getCid());
            }

        });
        initList();
    }

    /**
     * 启动第三方apk
     * 内嵌在当前apk内打开，每次启动都是新的apk,你会发现打开了两个apk
     * XXXXX ： 包名
     */
    public  void launchData(DingDataBean.DataBean appInfo) {

        String name = appInfo.getName();
        String uuid = appInfo.getUuid();
        String type = "course";

        String mode = "landscape";
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            mode = "portrait";
        } else {
            //横屏
            mode = "landscape";
        }

        String url = NetModel.getInstance().roboSysGetCourse(token,type,uuid,mode);
        /*Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName(packageName, className);
        intent.setComponent(cn);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/

        LogUtils.v(TAG,url);
        Intent intent = new Intent(DingActivity.this, WebViewActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }
    private void initList(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                final List<DingDataBean.DataBean> appInfos = AssetsFileUtils.ReadJsonFileFromAsset(DingActivity.this,"dingdang.txt");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAppAdapter.setData(appInfos);
                    }
                });
            }
        }.start();
    }

    class AppAdapter extends BaseAdapter {
        List<DingDataBean.DataBean> xqeBeans = new ArrayList<DingDataBean.DataBean>();

        public void setData(List<DingDataBean.DataBean> xqeBeans) {
            this.xqeBeans = xqeBeans;
            Log.v("ii","xqeBeans.size="+ xqeBeans.size());
            notifyDataSetChanged();
        }

        public List<DingDataBean.DataBean> getData() {
            return xqeBeans;
        }

        @Override
        public int getCount() {
            if (xqeBeans != null && xqeBeans.size() > 0) {
                return xqeBeans.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (xqeBeans != null && xqeBeans.size() > 0) {
                return xqeBeans.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DingActivity.AppAdapter.ViewHolder mViewHolder;
            DingDataBean.DataBean xqebean = xqeBeans.get(position);
            if (convertView == null) {
                mViewHolder = new DingActivity.AppAdapter.ViewHolder();
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_app_info, null);
                mViewHolder.iv_app_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
                mViewHolder.tx_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (DingActivity.AppAdapter.ViewHolder) convertView.getTag();
            }
            // mViewHolder.iv_app_icon.setImageDrawable(myAppInfo.getName());
            mViewHolder.tx_app_name.setText(xqebean.getName());
            return convertView;
        }

        class ViewHolder {
            ImageView iv_app_icon;
            TextView tx_app_name;
        }
    }

    @Override
    public void onRequestSuc(String type, String str) {
        switch (type) {
            case Constants.ROBO_SYS_LOGIN: //登录
                LogUtils.v(TAG,str);
                if (!StringUtils.isEmpty(str)) {
                    Gson gson = new Gson();

                    RoboSysLogin xqeBean = gson.fromJson(str.replace("\\xa0",""),RoboSysLogin.class);
                    token = xqeBean.getData().get(0);

                    } else {
                    }
                break;
            case Constants.ROBO_SYS_GET_COURSE: //获取课程
                LogUtils.v(str);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestErr(String type, String msg) {
        switch (type) {
            case Constants.ROBO_SYS_LOGIN: //登录
                LogUtils.v(TAG,msg);
                break;
            case Constants.ROBO_SYS_GET_COURSE: //課程
                LogUtils.v(msg);
                break;
            default:
                break;
        }
    }
}
