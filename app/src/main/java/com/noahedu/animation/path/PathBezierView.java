package com.noahedu.animation.path;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.noahedu.animation.path.evaluator.BezierEvaluator;

/**
 * © 2020 www.youxuepai.com
 *
 * @version 1.0
 * @file name：PathBezierView$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/12/23$ 12:40$
 */
public class PathBezierView extends View implements View.OnClickListener{
    //起点
    private int mStartPointX;
    private int mStartPointY;
    //终点
    private int mEndPointX;
    private int mEndPointY;
    //控制点
    private int mFlagPointX;
    private int mFlagPointY;
    //移动小球
    private int mMovePointX;
    private int mMovePointY;

    private Path mPath;
    private Paint mPaintPath;
    private Paint mPaintCircle;
    private PathMeasure mPathMeasure;
    /**
     * Path 长度
     */
    private float mPathLength;

    public PathBezierView(Context context) {
        super(context);
    }

    public PathBezierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaintPath = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPath.setStyle(Paint.Style.STROKE);
        mPaintPath.setStrokeWidth(8);
        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);

        mStartPointX = 100;
        mStartPointY = 600;

        //小球刚开始位置在起点
        mMovePointX = mStartPointX;
        mMovePointY = mStartPointY;

        mEndPointX = 600;
        mEndPointY = 100;

        mFlagPointX = 0;
        mFlagPointY = 500;

        setOnClickListener(this);
    }

    public PathBezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mStartPointX, mStartPointY, 20, mPaintCircle);
        canvas.drawCircle(mEndPointX, mEndPointY, 20, mPaintCircle);
        canvas.drawCircle(mMovePointX, mMovePointY, 20, mPaintCircle);

        mPath.reset();
        //绘制贝塞尔曲线，即运动路径
        mPath.moveTo(mStartPointX, mStartPointY);
        mPath.quadTo(mFlagPointX, mFlagPointY, mEndPointX, mEndPointY);
        // PathMeasure
        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mPath, false);

        // 此时为bezier的周长
        mPathLength = mPathMeasure.getLength();

        //画虚线
        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{60, 20}, mPathLength);

        mPaintPath.setPathEffect(dashPathEffect);
        canvas.drawPath(mPath, mPaintPath);
    }

    @Override
    public void onClick(View view) {
        //创建贝塞尔曲线坐标的换算类
        BezierEvaluator evaluator = new BezierEvaluator(new PointF(mFlagPointX, mFlagPointY));
        //指定动画移动轨迹
        ValueAnimator animator = ValueAnimator.ofObject(evaluator,
                new PointF(mStartPointX, mStartPointY),
                new PointF(mEndPointX, mEndPointY));
        animator.setDuration(600);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //改变小球坐标，产生运动效果
                PointF pointF = (PointF) valueAnimator.getAnimatedValue();
                mMovePointX = (int) pointF.x;
                mMovePointY = (int) pointF.y;
                //刷新UI
                invalidate();
            }
        });
        //添加加速插值器，模拟真实物理效果
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }
}
