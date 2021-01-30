/**
 * © 2019 www.youxuepai.com
 * @file name：TTSActivity.java.java
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2019-7-25下午7:00:40
 * @version 1.0
 */
package com.noahedu.demo.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;

import com.noahedu.common.constant.TTSConstants;
import com.noahedu.common.service.impl.ImageCache;
import com.noahedu.common.service.impl.RemoveTypeLastUsedTimeFirst;
import com.noahedu.common.service.impl.ImageMemoryCache.OnImageCallbackListener;
import com.noahedu.demo.R;

/**
 *  * © 2019 www.youxuepai.com
 *  名称：TTSActivity
 *  描述：简单描述该类的作用
 * @class name：TTSActivity
 * @anthor : daisg
 * @time 2019-7-25下午7:00:40
 * @version V1.0
 */
public class TTSActivity extends BaseActivity{

    /** column number **/
    public static final int    COLUMNS                  = 2;
    /** imageView default height **/
    public static final int    IMAGEVIEW_DEFAULT_HEIGHT = 400;
    public static final String TAG_CACHE                = "TTSActivity";
    public static final String TAG                = "TTSActivity";
    

    private RelativeLayout     parentLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.image_cache_demo);

        Context context = getApplicationContext();
        parentLayout = (RelativeLayout)findViewById(R.id.image_cache_parent_layout);
        initImageUrlList();
        IMAGE_CACHE.initData(this, TAG_CACHE);
        IMAGE_CACHE.setContext(context);

        int count = 0, viewId = 0x7F24FFF0;
        int verticalSpacing, horizontalSpacing;
        verticalSpacing = horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.dp_4);
        Display display = getWindowManager().getDefaultDisplay();
        int imageWidth = (display.getWidth() - (COLUMNS + 1) * horizontalSpacing) / COLUMNS;
        for (String imageUrl : imageUrlList) {
            ImageView imageView = new ImageView(context);
            imageView.setId(++viewId);
            imageView.setScaleType(ScaleType.CENTER);
            imageView.setBackgroundResource(R.drawable.image_border);
            parentLayout.addView(imageView);

            // set imageView layout params
            LayoutParams layoutParams = (RelativeLayout.LayoutParams)imageView.getLayoutParams();
            layoutParams.width = imageWidth;
            layoutParams.topMargin = verticalSpacing;
            layoutParams.rightMargin = horizontalSpacing;
            int column = count % COLUMNS;
            int row = count / COLUMNS;
            if (row > 0) {
                layoutParams.addRule(RelativeLayout.BELOW, viewId - COLUMNS);
            }
            if (column > 0) {
                layoutParams.addRule(RelativeLayout.RIGHT_OF, viewId - 1);
            }

            // get image
            if (!IMAGE_CACHE.get(imageUrl, imageView)) {
                imageView.setImageResource(R.drawable.trinea);
                layoutParams.height = IMAGEVIEW_DEFAULT_HEIGHT;
            }
            count++;
        }
    }

    @Override
    protected void onDestroy() {
        IMAGE_CACHE.saveDataToDb(this, TAG_CACHE);
        super.onDestroy();
    }

    /** icon cache **/
    public static final ImageCache IMAGE_CACHE = new ImageCache(128, 512);

    static {
        /** init icon cache **/
        OnImageCallbackListener imageCallBack = new OnImageCallbackListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void onImageLoaded(String imageUrl, Drawable imageDrawable, View view, boolean isInCache) {
                if (view != null && imageDrawable != null) {
                    ImageView imageView = (ImageView)view;
                    imageView.setImageDrawable(imageDrawable);
                    // first time show with animation
                    if (!isInCache) {
                        imageView.startAnimation(getInAlphaAnimation(2000));
                    }

                    // auto set height accroding to rate between height and weight
                    LayoutParams imageParams = (LayoutParams)imageView.getLayoutParams();
                    imageParams.height = imageParams.width * imageDrawable.getIntrinsicHeight()
                                         / imageDrawable.getIntrinsicWidth();
                    imageView.setScaleType(ScaleType.FIT_XY);
                }
            }
        };
        IMAGE_CACHE.setOnImageCallbackListener(imageCallBack);
        IMAGE_CACHE.setCacheFullRemoveType(new RemoveTypeLastUsedTimeFirst<Drawable>());

        IMAGE_CACHE.setHttpReadTimeOut(10000);
        IMAGE_CACHE.setOpenWaitingQueue(true);
        IMAGE_CACHE.setValidTime(-1);
    }

    public static AlphaAnimation getInAlphaAnimation(long durationMillis) {
        AlphaAnimation inAlphaAnimation = new AlphaAnimation(0, 1);
        inAlphaAnimation.setDuration(durationMillis);
        return inAlphaAnimation;
    }

    private List<String> imageUrlList;

    private void initImageUrlList() {
        imageUrlList = new ArrayList<String>();
        imageUrlList.add("http://farm8.staticflickr.com/7403/9146300103_03423db0cc.jpg");
        imageUrlList.add("http://farm4.staticflickr.com/3755/9148527824_6c156185ea.jpg");
        imageUrlList.add("http://farm8.staticflickr.com/7409/9148527822_36fa37d7ca_z.jpg");
        imageUrlList.add("http://farm8.staticflickr.com/7403/9146300103_03423db0cc.jpg");
        imageUrlList.add("http://farm8.staticflickr.com/7318/9148527808_e804baef0b.jpg");
        imageUrlList.add("http://farm3.staticflickr.com/2857/9148527928_3063544889.jpg");
        imageUrlList.add("http://farm8.staticflickr.com/7318/9146300275_5fe995d123.jpg");
        imageUrlList.add("http://farm8.staticflickr.com/7351/9148527976_8a4e75ae87.jpg");
        imageUrlList.add("http://farm4.staticflickr.com/3679/9146300263_5c2191232a_o.jpg");
        imageUrlList.add("http://farm3.staticflickr.com/2863/9148527892_31f9377351_o.jpg");
        imageUrlList.add("http://farm3.staticflickr.com/2888/9148527996_f05118d7de_o.jpg");
        imageUrlList.add("http://farm8.staticflickr.com/7310/9148528008_8e8f51997a.jpg");
        imageUrlList.add("http://farm3.staticflickr.com/2849/9148528108_dfcda19507.jpg");
        imageUrlList.add("http://farm4.staticflickr.com/3739/9148528022_e9bf03058f.jpg");
        imageUrlList.add("http://farm4.staticflickr.com/3696/9146300409_dfa9d7c603.jpg");
        imageUrlList.add("http://farm8.staticflickr.com/7288/9146300469_bd3420c75b_z.jpg");
    }
    
    private void getTTSTocken()    
    {
    	String url = TTSConstants.TTS_TOKEN_URL;
    	OkHttpClient okHttpClient = new OkHttpClient();
    	final Request request = new Request.Builder()
    	        .url(url)
    	        .get()//默认就是GET请求，可以不写
    	        .build();
    	Call call = okHttpClient.newCall(request);
    	call.enqueue(new Callback() {

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
    	        Log.d(TAG, "onFailure: ");
			}

			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				// TODO Auto-generated method stub
    	        Log.d(TAG, "onResponse: " + response.body().string());
    	        try {
					JSONObject obj = new JSONObject(response.body().string());
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
    	});
    }
}
