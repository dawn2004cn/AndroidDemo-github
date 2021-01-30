package com.noahedu.animation.path.evaluator;

import android.graphics.PointF;
import android.animation.TypeEvaluator;
import com.noahedu.animation.path.utils.BezierUtils;

/**
 * © 2020 www.youxuepai.com
 *
 * @version 1.0
 * @file name：BezierEvaluator$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/12/23$ 12:39$
 */
public class BezierEvaluator implements TypeEvaluator<PointF> {

    private PointF mFlagPoint;

    public BezierEvaluator(PointF flagPoint) {
        mFlagPoint = flagPoint;
    }

    @Override
    public PointF evaluate(float v, PointF pointF, PointF t1) {
        return BezierUtils.CalculateBezierPointForQuadratic(v, pointF, mFlagPoint, t1);
    }
}
