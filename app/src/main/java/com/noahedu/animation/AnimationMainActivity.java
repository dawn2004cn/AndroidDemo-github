package com.noahedu.animation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.noahedu.demo.R;

public class AnimationMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void webClick(View v){
        Intent intent = new Intent(AnimationMainActivity.this,WebPActivity.class);
        startActivity(intent);
    }

    public void webFrescoClick(View v){
        Intent intent = new Intent(AnimationMainActivity.this,FrescoWebPActivity.class);
        startActivity(intent);
    }

    public void gifClick(View v){
        Intent intent = new Intent(AnimationMainActivity.this,GifActivity.class);
        startActivity(intent);
    }
}
