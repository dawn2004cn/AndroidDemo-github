package com.noahedu.animation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.noahedu.demo.R;
import com.noahedu.demo.activity.BaseActivity;

public class FrameAnimationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_animation);
    }

    public void clickOne(View v) {
        startActivity(new Intent(this, FrameAnimationOne.class));
    }

    public void clickTwo(View v) {
        startActivity(new Intent(this, FrameAnimationTwo.class));
    }
}
