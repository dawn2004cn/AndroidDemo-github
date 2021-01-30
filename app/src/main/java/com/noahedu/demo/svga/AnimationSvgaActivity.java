package com.noahedu.demo.svga;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.noahedu.animation.FrameAnimationOne;
import com.noahedu.animation.FrameAnimationTwo;
import com.noahedu.animation.FrescoWebPActivity;
import com.noahedu.animation.GifActivity;
import com.noahedu.animation.RightMarkActivity;
import com.noahedu.animation.WebPActivity;
import com.noahedu.animation.path.activity.svg.SvgActivity;
import com.opensource.svgaplayer.SVGAParser;

import java.util.ArrayList;

class SampleItem {

    String title;
    Intent intent;

    public SampleItem(String title, Intent intent) {
        this.title = title;
        this.intent = intent;
    }
}

public class AnimationSvgaActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<SampleItem> items = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setupData();
        this.setupListView();
        this.setupSVGAParser();
        setContentView(listView);
    }

    void setupData() {
        this.items.add(new SampleItem("Animation From Assets", new Intent(this, AnimationFromAssetsActivity.class)));
        this.items.add(new SampleItem("Animation From Network", new Intent(this, AnimationFromNetworkActivity.class)));
        this.items.add(new SampleItem("Animation From Layout XML", new Intent(this, AnimationFromLayoutActivity.class)));
        this.items.add(new SampleItem("Animation With Dynamic Image", new Intent(this, AnimationWithDynamicImageActivity.class)));
        this.items.add(new SampleItem("Animation With Dynamic Click", new Intent(this, AnimationFromClickActivity.class)));
        this.items.add(new SampleItem("Frame Animation From Assets", new Intent(this, FrameAnimationOne.class)));
        this.items.add(new SampleItem("Frame Animation From FrameLayout", new Intent(this, FrameAnimationTwo.class)));
        this.items.add(new SampleItem("Frame Animation From Webp", new Intent(this, WebPActivity.class)));
        this.items.add(new SampleItem("Frame Animation From Gif", new Intent(this, GifActivity.class)));
        this.items.add(new SampleItem("Frame Animation From FrescoWebP", new Intent(this, FrescoWebPActivity.class)));
        this.items.add(new SampleItem("path Animation From RightMark", new Intent(this, RightMarkActivity.class)));
        this.items.add(new SampleItem("path Animation From svg", new Intent(this, SvgActivity.class)));
    }

    void setupListView() {
        this.listView = new ListView(this);
        this.listView.setAdapter(new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEnabled(int i) {
                return false;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public int getCount() {
                return AnimationSvgaActivity.this.items.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(final int i, View view, ViewGroup viewGroup) {
                LinearLayout linearLayout = new LinearLayout(AnimationSvgaActivity.this);
                TextView textView = new TextView(AnimationSvgaActivity.this);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AnimationSvgaActivity.this.startActivity(AnimationSvgaActivity.this.items.get(i).intent);
                    }
                });
                textView.setText(AnimationSvgaActivity.this.items.get(i).title);
                textView.setTextSize(24);
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                linearLayout.addView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (55 * getResources().getDisplayMetrics().density)));
                return linearLayout;
            }

            @Override
            public int getItemViewType(int i) {
                return 1;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        });
        this.listView.setBackgroundColor(Color.WHITE);
    }
    void setupSVGAParser() {
        SVGAParser.Companion.shareParser().init(this);
    }


}
