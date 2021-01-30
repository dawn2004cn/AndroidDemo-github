package com.noahedu.animation;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.noahedu.demo.R;

import java.io.IOException;
import java.io.InputStream;

public class FrescoWebPActivity extends Activity {
    private AnimationImageView mFrescoImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_web_p);
        mFrescoImage = findViewById(R.id.fresco_sequence_image);
    }

    public void loadFromAssets(View v){
        mFrescoImage.setImageURI(Uri.parse("newyear.webp"));
    }

    public void loadFromDrawable(View v){
        mFrescoImage.setLoopDefault();
        mFrescoImage.setLoopCount(2);
        mFrescoImage.setImageResource(R.drawable.newyear);
    }

    /**
     * 使用Drawable+ImageView方式,这里的mFrescoImage当作普通的View
     * @param v
     */
    public void loadFromInputStream(View v){
        InputStream in = null;
        try {
            in = getResources().getAssets().open("rmb.webp");
            BaseSequenceFactory factory = FrescoSequence.getSequenceFactory(FrescoSequence.WEBP);
            final AnimationSequenceDrawable drawable = new AnimationSequenceDrawable(factory.createSequence(in));
            drawable.setLoopCount(1);
            drawable.setLoopBehavior(AnimationSequenceDrawable.LOOP_FINITE);
            drawable.setOnFinishedListener(new AnimationSequenceDrawable.OnFinishedListener() {
                @Override
                public void onFinished(AnimationSequenceDrawable frameSequenceDrawable) {

                }
            });
            mFrescoImage.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
