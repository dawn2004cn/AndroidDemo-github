package com.noahedu.animation;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.noahedu.demo.R;

/**
 * Created by Ansen on 2017/3/24 09:28.
 *
 * @E-mail: tomorrow_p@163.com
 * @Blog: http://blog.csdn.net/qq_25804863
 * @Github: https://github.com/ansen360
 * @PROJECT_NAME: FrameAnimation
 * @PACKAGE_NAME: com.ansen.frameanimation.sample
 * @Description: TODO
 */
public class FrameAnimationOne extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one);
        ImageView image = (ImageView) findViewById(R.id.image);


        AnimationDrawable animationDrawable = (AnimationDrawable) image.getDrawable();
        animationDrawable.start();
    }
}
