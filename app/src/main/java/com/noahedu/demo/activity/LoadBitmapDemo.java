
package com.noahedu.demo.activity;


import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.noahedu.common.bitmap.thread.FileDownloader;
import com.noahedu.common.service.impl.BitmapManager;
import com.noahedu.demo.R;

public class LoadBitmapDemo extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_bitmap);
        final ImageView imageView = (ImageView) findViewById(R.id.image_test);
        BitmapManager manager = new BitmapManager(LoadBitmapDemo.this);
        manager.setDefaultBmp(BitmapFactory.decodeResource(getResources(),R.drawable.trinea));
        manager.loadBitmap("http://farm8.staticflickr.com/7403/9146300103_03423db0cc.jpg",imageView);
        findViewById(R.id.load).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        FileDownloader fileDownloader = new FileDownloader(LoadBitmapDemo.this,"http://farm4.staticflickr.com/3755/9148527824_6c156185ea.jpg", 3);
                        try {
							fileDownloader.download(null);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                        try {
                            Looper.prepare();
//                            Set<String> paths = new HashSet<String>();
//                            paths.add("http://img.blog.csdn.net/20140414134054375?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvd2FuZ2ppbnl1NTAx/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast");
//                            paths.add("http://cms.csdnimg.cn/article/201406/30/53b0aa885c941.jpg");
//                            paths.add("http://cms.csdnimg.cn/article/201406/30/53b0ff9b141a4.jpg");
//                            paths.add("http://cms.csdnimg.cn/article/201406/30/53b0cc425c78e.jpg");
//                            BitmapConfig.getInstance().setDownloadUrlList(paths);
                            BitmapManager bitmapManager = new BitmapManager(LoadBitmapDemo.this);
//                            bitmapManager.preDownloadImage();
                            
                            bitmapManager.loadBitmap("http://farm4.staticflickr.com/3755/9148527824_6c156185ea.jpg", imageView);
                            
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

}
