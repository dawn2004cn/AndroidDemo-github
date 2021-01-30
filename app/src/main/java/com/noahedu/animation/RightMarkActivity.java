package com.noahedu.animation;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.noahedu.animation.path.RightMarkView;
import com.noahedu.animation.path.RouteTraceView;
import com.noahedu.demo.R;
import com.noahedu.demo.activity.BaseActivity;
import com.noahedu.utils.ImageTools;

import com.noahedu.utils.ImageTools;

public class RightMarkActivity extends BaseActivity  implements View.OnClickListener {

    private RightMarkView markView ;
    private RouteTraceView routeView;
    private RouteTraceView routeView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right_mark);
        initView();
    }
    private void initView(){
      /*  markView =
                (RightMarkView) findViewById(R.id.activity_right_mark_rmv);
        // 设置开始和结束两种颜色
        markView.setColor(Color.parseColor("#FF4081"), Color.YELLOW);
        // 设置画笔粗细
        markView.setStrokeWidth(10f);*/

        routeView = (RouteTraceView)findViewById(R.id.activity_route_trace);
        // 设置开始和结束两种颜色
        routeView.setColor(Color.parseColor("#FF4081"), Color.YELLOW);
        // 设置画笔粗细
        routeView.setStrokeWidth(10f);
        routeView.setOnClickListener(this);
        routeView2 = (RouteTraceView)findViewById(R.id.activity_route_trace2);
        // 设置开始和结束两种颜色
        routeView2.setColor(Color.parseColor("#FF4081"), Color.YELLOW);
        // 设置画笔粗细
        routeView2.setStrokeWidth(10f);
        routeView2.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //markView.startAnimator();
        routeView.startAnimator();
        routeView2.startAnimator();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_route_trace) {
            routeView.setBitmap(ImageTools.reduceSize(BitmapFactory.decodeResource(getResources(), R.drawable.anim_report_00), 60, 60));
            routeView.startRoleAnimator();
        }
        else{
            routeView2.setBitmap(ImageTools.reduceSize(BitmapFactory.decodeResource(getResources(), R.drawable.anim_report_00), 60, 60));
            routeView2.startRoleAnimator();
        }
    }
}
