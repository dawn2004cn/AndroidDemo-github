package com.noahedu.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.noahedu.demo.R;


/**
 * © 2020 www.youxuepai.com
 *
 * @version 1.0
 * @file name：WaterView$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/12/24$ 9:06$
 */
public class WaterView extends View {
    private String water_name;//需要回传的值
    private Bitmap mBitmap;
    private Bitmap dstBmp, srcBmp;
    private RectF dstRect, srcRect;
    private Xfermode mXfermode;
    private PorterDuff.Mode mPorterDuffMode = PorterDuff.Mode.MULTIPLY;


    public WaterView(Context context, String user_name) {
        super(context);
        this.water_name = user_name;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ok1);
        mXfermode = new PorterDuffXfermode(mPorterDuffMode);
    }

    public WaterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    public WaterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        Paint paint = new Paint();
        if (mBitmap != null) {
            Bitmap imageBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas comboImage = new Canvas(imageBitmap);
            comboImage.drawBitmap(mBitmap, 0f, 0f, paint);
            paint.setXfermode(mXfermode);
        }

        paint.setColor(Color.argb(191, 200, 215, 200));
        //设置字体大小
        paint.setTextSize(46);
        //让画出的图形是空心的
        paint.setStyle(Paint.Style.STROKE);
        //设置画出的线的 粗细程度
        paint.setStrokeWidth(1);
        // 以文字中心轴旋转
        canvas.rotate(-20, this.getWidth() / 2f, this.getHeight() / 2f);
        // 按照指定点的坐标绘制文本(在这里画了四个);water_name 不能为空
        canvas.drawText(water_name + "", this.getWidth() / 4f, this.getHeight() / 2f, paint);
        canvas.drawText(water_name + "", this.getWidth() / 1.5f, this.getHeight() / 2f, paint);
        canvas.drawText(water_name + "", this.getWidth() / 1.5f, this.getHeight() / 4f, paint);
        canvas.drawText(water_name + "", this.getWidth() / 4f, this.getHeight() / 4f, paint);
        super.onDraw(canvas);
       // canvas.restore();
    }

    public void setWater_name(String water_name) {
        this.water_name = water_name;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = w <= h ? w : h;
        int centerX = w/2;
        int centerY = h/2;
        int quarterWidth = width /4;
        srcRect = new RectF(centerX-quarterWidth, centerY-quarterWidth, centerX+quarterWidth, centerY+quarterWidth);
        dstRect = new RectF(centerX-quarterWidth, centerY-quarterWidth, centerX+quarterWidth, centerY+quarterWidth);

    }

}
