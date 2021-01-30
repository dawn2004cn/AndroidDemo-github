package com.noahedu.demo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aispeech.vod.xqe.VodXqe;
import com.noahedu.common.util.AssetsFileUtils;
import com.noahedu.demo.R;
import com.noahedu.demo.utils.XQEBean;

import java.util.ArrayList;
import java.util.List;

public class XQEActivity extends BaseActivity {
    private ListView lv_app_list;
    private AppAdapter mAppAdapter;
    public Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        lv_app_list = (ListView) findViewById(R.id.lv_app_list);
        mAppAdapter = new AppAdapter();
        lv_app_list.setAdapter(mAppAdapter);
        lv_app_list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				XQEBean xqeBean=(XQEBean) lv_app_list.getItemAtPosition(position);
				//launchAPK(appInfo);
                VodXqe.getInstance().openAlbumPage(xqeBean.getCid());
			}
        	
        });
        initXqeList();
    }
    private void initXqeList(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                final List<XQEBean> appInfos = AssetsFileUtils.ReadTxtFileFromAsset(XQEActivity.this,"child.json");
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

        List<XQEBean> xqeBeans = new ArrayList<XQEBean>();

        public void setData(List<XQEBean> xqeBeans) {
            this.xqeBeans = xqeBeans;
            Log.v("ii","xqeBeans.size="+ xqeBeans.size());
            notifyDataSetChanged();
        }

        public List<XQEBean> getData() {
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
            ViewHolder mViewHolder;
            XQEBean xqebean = xqeBeans.get(position);
            if (convertView == null) {
                mViewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_app_info, null);
                mViewHolder.iv_app_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
                mViewHolder.tx_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
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
    protected void onResume() {
        VodXqe.getInstance().init(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        VodXqe.getInstance().release();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
