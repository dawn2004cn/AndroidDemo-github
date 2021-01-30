package com.noahedu.animation;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.noahedu.demo.R;

import java.io.IOException;
import java.io.InputStream;


public class WebPActivity extends Activity {
    private static final String TAG = "WebPActivity";
    private AnimationImageView mGoogleImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_p);
        mGoogleImage = findViewById(R.id.google_sequence_image);
    }

    public void loadFromAssets(View v){
        mGoogleImage.setImageURI(Uri.parse("newyear.webp"));
    }

    public void loadFromDrawable(View v){
        mGoogleImage.setLoopDefault();
        mGoogleImage.setLoopCount(2);
        mGoogleImage.setImageResource(R.drawable.newyear);
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
            mGoogleImage.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
