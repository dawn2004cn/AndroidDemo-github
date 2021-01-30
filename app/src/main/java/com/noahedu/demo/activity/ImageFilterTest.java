package com.noahedu.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.noahedu.common.filedownloader.base.Log;
import com.noahedu.common.image.filter.CleanGlassFilter;
import com.noahedu.common.image.filter.ComicFilter;
import com.noahedu.common.image.filter.FilmFilter;
import com.noahedu.common.image.filter.FocusFilter;
import com.noahedu.common.image.filter.Gradient;
import com.noahedu.common.image.filter.IImageFilter;
import com.noahedu.common.image.filter.Image;
import com.noahedu.common.image.filter.ImageBlender;
import com.noahedu.common.image.filter.LomoFilter;
import com.noahedu.common.image.filter.PaintBorderFilter;
import com.noahedu.common.image.filter.SceneFilter;
import com.noahedu.common.image.filter.ImageBlender;
import com.noahedu.common.util.ImageUtils;
import com.noahedu.demo.R;
import com.noahedu.utils.ImageTools;
import com.noahedu.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * © 2020 www.youxuepai.com
 *
 * @version 1.0
 * @file name：ImageFilterTest$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/12/25$ 14:54$
 */
public class ImageFilterTest extends Activity {

    private ImageView imageView;
    private TextView textView;
    private Bitmap bitmap ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        imageView= (ImageView) findViewById(R.id.imgfilter);
        textView = (TextView) findViewById(R.id.runtime);

        bitmap = BitmapFactory.decodeResource(ImageFilterTest.this.getResources(), R.drawable.image1);
        imageView.setImageBitmap(bitmap);

        LoadImageFilter();
    }

    /**
     * filter
     */
    private void LoadImageFilter() {
        Gallery gallery = (Gallery) findViewById(R.id.galleryFilter);
        final ImageFilterAdapter filterAdapter = new ImageFilterAdapter(
                ImageFilterTest.this);
        gallery.setAdapter(new ImageFilterAdapter(ImageFilterTest.this));
        gallery.setSelection(2);
        gallery.setAnimationDuration(3000);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                IImageFilter filter = (IImageFilter) filterAdapter.getItem(position);
                new processImageTask(ImageFilterTest.this, filter).execute();
            }
        });
    }

    public class processImageTask extends AsyncTask<Void, Void, Bitmap> {
        private IImageFilter filter;
        private Activity activity = null;
        public processImageTask(Activity activity, IImageFilter imageFilter) {
            this.filter = imageFilter;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            textView.setVisibility(View.VISIBLE);
        }

        public Bitmap doInBackground(Void... params) {
            Image img = null;
            try
            {
                //Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.text);
                img = new Image(bitmap);

                if (filter != null) {
                    if (filter instanceof FilmFilter)
                    {
                        Bitmap srcBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ok1);
                        Image src = new Image(srcBitmap);

                        /*ImageBlender blender = new ImageBlender();
                        //blender.Mode = BlendMode.Multiply;
                        img = blender.Blend(img,src);*/
                        //Bitmap dst = ImageTools.BlendBitmap(bitmap,srcBitmap);
                        Bitmap dst = ImageTools.paddingBitmap(bitmap);
                        Log.v("TAG","dst:w"+dst.getWidth()+"dst:h"+dst.getHeight());
                        //bitmap.recycle();
                        //Bitmap ds1t = ImageUtils.scaleImage(dst,320,320);
                        img =new Image(dst);
                    }
                    else
                    {
                        img = filter.process(img);
                    }
                    img.copyPixelsFromBuffer();
                }
                return img.getImage();
            }
            catch(Exception e){
                if (img != null && img.destImage.isRecycled()) {
                    img.destImage.recycle();
                    img.destImage = null;
                    System.gc();
                }
            }
            finally{
                if (img != null && img.image.isRecycled()) {
                    img.image.recycle();
                    img.image = null;
                    System.gc();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(result != null){
                super.onPostExecute(result);
                imageView.setImageBitmap(result);
            }
            textView.setVisibility(View.GONE);
        }
    }

    public class ImageFilterAdapter extends BaseAdapter {
        private class FilterInfo {
            public int filterID;
            public IImageFilter filter;

            public FilterInfo(int filterID, IImageFilter filter) {
                this.filterID = filterID;
                this.filter = filter;
            }
        }

        private Context mContext;
        private List<ImageFilterAdapter.FilterInfo> filterArray = new ArrayList<ImageFilterAdapter.FilterInfo>();

        public ImageFilterAdapter(Context c) {
            mContext = c;


            //v0.2

            filterArray.add(new ImageFilterAdapter.FilterInfo(R.drawable.ok1, new FilmFilter(80f)));
            filterArray.add(new ImageFilterAdapter.FilterInfo(R.drawable.invert_filter, new FocusFilter()));
            filterArray.add(new ImageFilterAdapter.FilterInfo(R.drawable.invert_filter, new CleanGlassFilter()));
            filterArray.add(new ImageFilterAdapter.FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0x00FF00)));//green
            filterArray.add(new ImageFilterAdapter.FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0x00FFFF)));//yellow
            filterArray.add(new ImageFilterAdapter.FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0xFF0000)));//blue
            filterArray.add(new ImageFilterAdapter.FilterInfo(R.drawable.invert_filter, new LomoFilter()));

            filterArray.add(new ImageFilterAdapter.FilterInfo(R.drawable.saturationmodity_filter,null));
        }

        public int getCount() {
            return filterArray.size();
        }

        public Object getItem(int position) {
            return position < filterArray.size() ? filterArray.get(position).filter
                    : null;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Bitmap bmImg = BitmapFactory
                    .decodeResource(mContext.getResources(),
                            filterArray.get(position).filterID);
            int width = 100;// bmImg.getWidth();
            int height = 100;// bmImg.getHeight();
            bmImg.recycle();
            ImageView imageview = new ImageView(mContext);
            imageview.setImageResource(filterArray.get(position).filterID);
            imageview.setLayoutParams(new Gallery.LayoutParams(width, height));
            imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return imageview;
        }
    };
}
