package com.noahedu.demo.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;

import com.noahedu.demo.R;
import com.noahedu.demo.utils.AppUtils;
import com.noahedu.demo.view.WaterView;
import com.watermark.androidwm.WatermarkBuilder;
import com.watermark.androidwm.bean.WatermarkImage;

import java.util.List;


/**
 * BaseActivity
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-6-1
 */
public abstract class BaseActivity extends Activity {

    private Button trineaInfoTv;
    private  WaterView view;

    protected void onCreate(Bundle savedInstanceState, int layoutResID) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID);

        AppUtils.initTrineaInfo(this, trineaInfoTv, getClass());

        ActionBar bar = getActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStart() {
        addWaterMarkView();
        super.onStart();
    }

    private void addWaterMarkView() {
        view=new WaterView(this,"水印");
        getRootView().addView(view,-1);
    }

    private ViewGroup getRootView() {
        ViewGroup rootView = (ViewGroup)findViewById(android.R.id.content);
        return rootView;
    }
}
