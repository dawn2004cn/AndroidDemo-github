package com.noahedu.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * 定义自己的Fragment，内容为TextView
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-11-15
 */
public class ViewPagerFragment extends Fragment {

    public ViewPagerFragment(){
        super();
    }

    /**
     * 覆盖此函数，先通过inflater inflate函数得到view最后返回
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_pager_fragment_demo1, container, false);
        TextView tv = (TextView)v.findViewById(R.id.viewPagerText);
        ImageView image = (ImageView)v.findViewById(R.id.viewPagerImage);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int upImageId = bundle.getInt("upImageId");
            if (upImageId != 0) {
                image.setImageResource(upImageId);
            }
            String text = bundle.getString("text");
            tv.setText(text);
        }
        return v;
    }
}
