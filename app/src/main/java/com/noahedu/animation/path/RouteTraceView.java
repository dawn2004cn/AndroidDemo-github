package com.noahedu.animation.path;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.noahedu.animation.path.evaluator.BezierEvaluator;
import com.noahedu.common.filedownloader.base.Log;
import com.noahedu.demo.R;

/**
 * © 2020 www.youxuepai.com
 *
 * @version 1.0
 * @file name：RouteTraceView $
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/12/22$ 20:57$
 */
public class RouteTraceView extends View {
    private final String TAG = RouteTraceView.class.getSimpleName();

    static final int DIRECTION_TYPE_LEFT = 0;
    static final int DIRECTION_TYPE_RIGHT = 1;

    //起点
    private int mStartPointX;
    private int mStartPointY;
    //终点
    private int mEndPointX;
    private int mEndPointY;
    //控制点
    private int mFlagPointX;
    private int mFlagPointY;

    //移动点
    private int mMovePointX;
    private int mMovePointY;

    /**
     * 颜色
     */
    private int mStartColor = Color.RED;
    private int mEndColor = Color.YELLOW;

    /**
     * 画笔宽度
     */
    private float mStrokeWidth = 8f;


    private Path mDstPath;
    private float mEndDistance;
    private PathMeasure mPathMeasure;
    /**
     * bezier路径
     */
    private Path mBezierPath;
    /**
     * Path 长度
     */
    private float mPathLength;

    /**
     * 动画估值
     */
    private float mAnimatorValue;


    private Paint mPaint;

    //画小优
    private Paint mPaintBitmap;
    /**
     * bezier动画
     */
    private ValueAnimator mBezierValueAnimator;

    /**
     * bezier是否已经加载过
     */
    private boolean mIsHasBezier = false;

    /**
     * role是否已经加载过
     */
    private boolean  mIsHasRole = false;


    /**
     * 默认大小
     */
    private static final int DEFAULT_SIZE = 600;

    /**
     * 动画执行时间
     */
    public static final int ANIMATOR_TIME = 1000;

    public static final int BITMAP_SIZE  = 30;

    private final Context mContext;

    private Bitmap mBitmap;
    private int directionType = DIRECTION_TYPE_LEFT;

    public RouteTraceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RouteTraceView, 0, 0);
            try {
                directionType = a.getInt(R.styleable.RouteTraceView_direction_type, directionType);
            } finally {
                a.recycle();
            }
        }
        this.mContext = context;
        // 初始化画笔
        initPaint();
        // 初始化动画
        initBezierAnimator();


        // 利用 post 获取 View 的宽高
        // post 内任务，会在第2次执行 onMeasure() 方法后执行
        post(() -> {
            // 初始化bezierPath
            initBezierPath();

            // 初始化线性渐变
            // 由于要使用 mRealSize ，放 post 内
            initShader();
        });
    }
    /**
     * 画笔
     */
    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaintBitmap = new Paint(Paint.ANTI_ALIAS_FLAG);

        //mBitmap = ImageTools.reduceSize(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.anim_report_00),60,60);
    }
    public void setBitmap(Bitmap bitmap){
        if (mBitmap !=null) {
            mBitmap.recycle();
            mBitmap = null;
        }
        mBitmap = bitmap;
    }
    /**
     * 绘制路径
     */
    private void initBezierPath() {
        // 获取View 的宽
        int realw = getWidth();
        int realh = getHeight();

        // 添加贝塞尔路径
        mBezierPath = new Path();
        if (directionType == DIRECTION_TYPE_LEFT) {
            mStartPointX = realw / 6;
            mStartPointY = realh;


            mEndPointX = realw;
            mEndPointY = realh / 6;


            mFlagPointX = 0;
            mFlagPointY = realh / 6 * 5;
        }
        else {
            mStartPointX = realw ;
            mStartPointY = realh /6 *5;


            mEndPointX = realw/6;
            mEndPointY = realh/6;


            mFlagPointX = realh / 6 * 5;
            mFlagPointY = 0;

        }
        Log.v(TAG,"realw:"+realw+"realh:"+realh);

        mBezierPath.moveTo(mStartPointX, mStartPointY);
        mBezierPath.quadTo(mFlagPointX, mFlagPointY, mEndPointX, mEndPointY);

        // PathMeasure
        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mBezierPath, false);

        // 此时为bezier的周长
        mPathLength = mPathMeasure.getLength();

        // Path dst 用来存储截取的Path片段
        mDstPath = new Path();
    }

    /**
     * 线性渐变
     */
    private void initShader() {
        // 使用线性渐变
        LinearGradient shader = new LinearGradient(0, 0, getWidth(), getHeight(),
                mStartColor, mEndColor, Shader.TileMode.REPEAT);
        mPaint.setShader(shader);
    }
    /**
     * 初始化圆形动画
     */
    private void initBezierAnimator() {
        // bezier动画
       /* BezierEvaluator evaluator = new BezierEvaluator(new PointF(mFlagPointX, mFlagPointY));
        mBezierValueAnimator = ValueAnimator.ofObject(evaluator,
                new PointF(mStartPointX, mStartPointY),
                new PointF(mEndPointX, mEndPointY));*/

        mBezierValueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        // 动画过程
        mBezierValueAnimator.addUpdateListener(animation -> {
            mAnimatorValue = (float) animation.getAnimatedValue();

            //PointF pointF = (PointF) animation.getAnimatedValue();
            //mMovePointX = (int) pointF.x;
            //mMovePointY = (int) pointF.y;
            invalidate();
        });

        // 动画时间
        mBezierValueAnimator.setDuration(ANIMATOR_TIME);

        // 插值器
        mBezierValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        // bezier结束后，
        mBezierValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsHasBezier = true;
            }
        });
    }

    /**
     * 开启动画
     */
    public void startAnimator() {
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mStartColor);
        mBezierValueAnimator.start();
    }

    /**
     * 开启动画
     */
    public void startRoleAnimator() {
        mIsHasRole = false;
        //创建贝塞尔曲线坐标的换算类
        BezierEvaluator evaluator = new BezierEvaluator(new PointF(mFlagPointX, mFlagPointY));
        //指定动画移动轨迹
        ValueAnimator animator = ValueAnimator.ofObject(evaluator,
                new PointF(mStartPointX, mStartPointY),
                new PointF(mEndPointX, mEndPointY));
        animator.setDuration(ANIMATOR_TIME);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //改变小球坐标，产生运动效果
                PointF pointF = (PointF) valueAnimator.getAnimatedValue();
                mMovePointX = (int) pointF.x-BITMAP_SIZE;
                mMovePointY = (int) pointF.y-BITMAP_SIZE;
                //刷新UI
                invalidate();
            }
        });
        // bezier结束后，
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBitmap.recycle();
                mIsHasRole = true;
            }
        });
        //添加加速插值器，模拟真实物理效果
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }
    /**
     * 设置颜色
     */
    public void setColor(@ColorInt int startColor, @ColorInt int endColor) {
        this.mStartColor = startColor;
        this.mEndColor = endColor;
    }

    /**
     * 设置画笔粗细
     */
    public void setStrokeWidth(float strokeWidth) {
        this.mStrokeWidth = strokeWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        // 取宽高中小的，强制设置成为正方形
        int realSize = Math.min(wSpecSize, hSpecSize);

        // 宽高模式是否有一个为 AT_MOST
        boolean isAnyOneAtMost =
                (wSpecMode == MeasureSpec.AT_MOST || hSpecMode == MeasureSpec.AT_MOST);

        if (!isAnyOneAtMost) {
            // 将宽高中小的值 realSize 与 150px 比较，取大的值
            realSize = Math.max(realSize, DEFAULT_SIZE);
            setMeasuredDimension(realSize, realSize);
        } else {
            setMeasuredDimension(DEFAULT_SIZE, DEFAULT_SIZE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDstPath == null) {
            return;
        }
        if(mBitmap != null && !mBitmap.isRecycled()) {
            canvas.drawBitmap(mBitmap, mMovePointX, mMovePointY, mPaintBitmap);
        }
        // 绘制已经记录过的bezier Path
        if (mIsHasBezier) {
            canvas.drawPath(mBezierPath, mPaint);
        }

        // 刷新当前截取 Path
        mDstPath.reset();

        // 避免硬件加速的Bug
        mDstPath.lineTo(0, 0);

        // 截取片段
        float stop = mPathLength * mAnimatorValue;
        mPathMeasure.getSegment(0, stop, mDstPath, true);

        //画虚线
        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{60, 20}, mPathLength);

        mPaint.setPathEffect(dashPathEffect);
        // 绘制截取的片段
        canvas.drawPath(mDstPath, mPaint);
    }

}
