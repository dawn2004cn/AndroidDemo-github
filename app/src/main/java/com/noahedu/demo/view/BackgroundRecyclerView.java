package com.noahedu.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.noahedu.demo.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * © 2020 www.youxuepai.com
 *
 * @version 1.0
 * @file name：BackgroundRecyclerView$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2021/1/6$ 20:52$
 */
public class BackgroundRecyclerView extends RecyclerView {

    static public String TAG = BackgroundRecyclerView.class.getSimpleName();
    static final int DIRECTION_TYPE_HORIZONTAL = 0;
    static final int DIRECTION_TYPE_VERTICAL = 1;
    private InputStream inputStream; // 图片的inputStream
    private BitmapRegionDecoder bitmapRegionDecoder; // 用来解析图片的部分区域
    private int bitmapWidth; // 图片的总宽度
    private int bitmapHeight; // 图片的总高度
    private Rect regionRect; // 图片资源Rect， 配合BitmapRegionDecoder使用截取图片
    private Rect dstRect; // 图片位置Rect;
    private Rect srcRect; // 图片展示Rect
    private int scrollY; // RecyclerView的滚动距离
    private int scrollX; // RecyclerView的滚动距离
    private float ratio; // 图片缩放比
    private float drawHeight; // 需要绘制的高度
    private float drawWidth; // 需要绘制的宽度
    private Bitmap bitmap; // 从inputstream中使用BitmapRegionDecoder截取的bitmap
    BitmapFactory.Options options; // BitmapFactory参数
    private Paint paint; // 绘制图片的画笔
    private int mResourceId;
    private int direction = DIRECTION_TYPE_VERTICAL;

    public BackgroundRecyclerView(@NonNull Context context) {
        this(context,null,0);
    }

    public BackgroundRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BackgroundRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (attrs != null) {
            TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BackgroundRecyclerView, defStyle, 0);

            try {
                mResourceId = array.getResourceId(R.styleable.BackgroundRecyclerView_src, R.drawable.homework_level_bg1);
            }finally {
                array.recycle();
            }
        }
        init();
    }

    private void init() {
        // 设置RecyclerView滚动监听
        this.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrollY += dy;
                scrollX += dx;
            }
        });
        try {
            // 从Assets中解析图片到inputStream
            inputStream = getResources().openRawResource(mResourceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            options = new BitmapFactory.Options();
            // 只decode图片属性
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            // 获取图片原始宽高
            bitmapHeight = options.outHeight;
            bitmapWidth = options.outWidth;
            // 关闭只读取图片属性选项
            options.inJustDecodeBounds = false;
            // 开启复用内存，节省内存占用
            options.inMutable = true;
            // 指定复用内存的bitmap
            options.inBitmap = bitmap;
            try {
                // 初始化BitmapRegionDecoder
                bitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 初始化几个Rect
            srcRect = new Rect();
            dstRect = new Rect();
            regionRect = new Rect();
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int width = MeasureSpec.getSize(widthSpec);
        int height = MeasureSpec.getSize(heightSpec);
        dstRect = new Rect(0, 0, width, height);
        Log.v("zyl","bitmapWidth:"+bitmapWidth+"bitmapHeight:"+bitmapHeight);
        // 因为背景图片要铺满背景，因此需要一个缩放比例
        if (bitmapHeight > bitmapWidth) {
            direction = DIRECTION_TYPE_VERTICAL;
            ratio = width / (float) bitmapWidth;

            // 根据缩放比计算实际要draw的高度
            drawHeight = bitmapHeight * ratio;
            // 初始化bitmapRegionDecoder得出的bitmap所占Rect的大小
            srcRect = new Rect(0, 0, bitmapWidth, (int) (height / ratio));
        }
        else {
            direction = DIRECTION_TYPE_HORIZONTAL;
            ratio = height / (float) bitmapHeight;
            // 根据缩放比计算实际要draw的高度
            drawWidth =  bitmapWidth * ratio;

            // 初始化bitmapRegionDecoder得出的bitmap所占Rect的大小
            srcRect = new Rect(0, 0, (int)(bitmapWidth/ratio), (int) (height));
        }

    }


    @Override
    public void onDraw(Canvas canvas) {
        if (bitmapRegionDecoder == null) {
            super.onDraw(canvas);
            return;
        }
        int top=0,bottom=0,left = 0,right=0;
        if (direction == DIRECTION_TYPE_VERTICAL) {
            //向下滚动
            if (scrollY > 0) {
                top = (int) ((scrollY % drawHeight) / ratio);
                bottom = (int) ((canvas.getHeight() + (scrollY % drawHeight)) / ratio);

                //移动到了超过图片尺寸
                if ((canvas.getHeight() + (scrollY % drawHeight)) > drawHeight) {
                    top = (int) ((canvas.getHeight() + scrollY % drawHeight - drawHeight) / ratio);
                    bottom = (int) ((2 * canvas.getHeight() + (scrollY % drawHeight) - drawHeight) / ratio);
                }
            } else {
                //向上滚动
                top = (int) ((drawHeight - canvas.getHeight() + (scrollY % drawHeight)) / ratio);
                bottom = (int) ((drawHeight + (scrollY % drawHeight)) / ratio);
                //移动到了超过图片尺寸
                if ((drawHeight - canvas.getHeight() + (scrollY % drawHeight)) < 0) {
                    top = (int) ((drawHeight - canvas.getHeight() + scrollY % drawHeight + canvas.getHeight()) / ratio);
                    bottom = (int) ((drawHeight - canvas.getHeight() + scrollY % drawHeight + 2 * canvas.getHeight()) / ratio);
                }
            }
            left = 0;
            right = bitmapWidth;
        }
        else {
            if (scrollX > 0){
                left = (int) ((scrollX % drawWidth) / ratio);
                right = (int) ((canvas.getWidth() + (scrollX % drawWidth)) / ratio);

                //移动到了超过图片尺寸
                if ((canvas.getWidth() + (scrollX % drawWidth)) > drawWidth) {
                    left = (int) ((canvas.getWidth() + scrollX % drawWidth - drawWidth) / ratio);
                    right = (int) ((2 * canvas.getWidth() + (scrollX % drawWidth) - drawWidth) / ratio);
                }
            }
            else{
                //向左滚动
                left = (int) ((drawWidth - canvas.getWidth() + (scrollX % drawWidth)) / ratio);
                right = (int) ((drawWidth + (scrollX % drawWidth)) / ratio);
                //移动到了超过图片尺寸
                if ((drawWidth - canvas.getWidth() + (scrollX % drawWidth)) < 0) {
                    left = (int) ((drawWidth - canvas.getWidth() + scrollX % drawWidth + canvas.getWidth()) / ratio);
                    right = (int) ((drawWidth - canvas.getWidth() + scrollX % drawWidth + 2 * canvas.getWidth()) / ratio);
                }
            }
            top = 0;
            bottom = bitmapHeight;
        }
        Log.d("zyl", "computeVerticalScrollOffset = " + scrollY+"computeHScrollOffset = " + scrollX+"direction:"+direction);
        Log.d("zyl", "canvasWidth = " + canvas.getWidth() + "     " + "canvasHeight = " + canvas.getHeight());

        // 计算截取图片Rect的ltrb
        regionRect.left = left;
        regionRect.top = top;
        regionRect.right = right;
        regionRect.bottom = bottom;
        Log.v("zyl",regionRect.toString());
        // 从原始图片的InputStream中截取Bitmap
        bitmap = bitmapRegionDecoder.decodeRegion(regionRect, options);
        // 绘制该Bitmap 保证Bitmap正好铺满背景
        canvas.drawBitmap(bitmap, srcRect, dstRect, paint);
        super.onDraw(canvas);
    }
}
