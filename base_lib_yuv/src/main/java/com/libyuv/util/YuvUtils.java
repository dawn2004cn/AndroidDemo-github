package com.libyuv.util;

import java.nio.ByteBuffer;

/**
 * 作者：请叫我百米冲刺 on 2017/8/28 上午11:05
 * 邮箱：mail@hezhilin.cc
 */

public class YuvUtils {

    static {
        System.loadLibrary("yuvutils_lib");
    }

    /**
     * 初始化
     *
     * @param width      原始的宽
     * @param height     原始的高
     * @param dst_width  输出的宽
     * @param dst_height 输出的高
     **/
    public static native void init(int width, int height, int dst_width, int dst_height);



    /**
     * YUV数据的基本的处理
     *
     * @param src        原始数据
     * @param width      原始的宽
     * @param height     原始的高
     * @param dst        输出数据
     * @param dst_width  输出的宽
     * @param dst_height 输出的高
     * @param mode       压缩模式。这里为0，1，2，3 速度由快到慢，质量由低到高，一般用0就好了，因为0的速度最快
     * @param degree     旋转的角度，90，180和270三种
     * @param isMirror   是否镜像，一般只有270的时候才需要镜像
     **/
    public static native void compressYUV(byte[] src, int width, int height, byte[] dst, int dst_width, int dst_height, int mode, int degree, boolean isMirror);

    /**
     * yuv数据的裁剪操作
     *
     * @param src        原始数据
     * @param width      原始的宽
     * @param height     原始的高
     * @param dst        输出数据
     * @param dst_width  输出的宽
     * @param dst_height 输出的高
     * @param left       裁剪的x的开始位置，必须为偶数，否则显示会有问题
     * @param top        裁剪的y的开始位置，必须为偶数，否则显示会有问题
     **/
    public static native void cropYUV(byte[] src, int width, int height, byte[] dst, int dst_width, int dst_height, int left, int top);

    /**
     * 将I420转化为NV21
     *
     * @param i420Src 原始I420数据
     * @param nv21Src 转化后的NV21数据
     * @param width   输出的宽
     * @param width   输出的高
     **/
    public static native void yuvI420ToNV21(byte[] i420Src, byte[] nv21Src, int width, int height);

    /**
     * 将I420缩放
     *
     * @param i420Src 原始I420数据
     * @param i420Dst 转化后的I420数据
     * @param width   输入的宽
     * @param height   输入的高
     * @param dstW   输出的宽
     * @param dsth   输出的高
     **/
    public static native void yuvI420Scale(byte[] i420Src,  int width, int height,byte[] i420Dst,int dstW,int dsth);

    /**
     * 将I420 mirro
     *
     * @param i420Src 原始I420数据
     * @param i420Dst 转化后的I420数据
     * @param width   输入的宽
     * @param height   输入的高
     **/
    public static native void yuvI420Mirror(byte[] i420Src,byte[] i420Dst,  int width, int height);

    /**
     * 将NV21转化为I420
     *
     * @param i420Src 原始NV21数据
     * @param nv21Src 转化后的I420数据
     * @param width   输出的宽
     * @param width   输出的高
     **/
    public static native void yuvNV21ToI420(byte[] nv21Src, byte[] i420Src, int width, int height);


    /**
     * 将I420转化为NV21
     *
     * @param nv21Src 原始NV21数据
     * @param nv21Dst 转化后的rgb数据
     * @param width   输入的宽
     * @param height   输入的高
     * @param dstW   输出的宽
     * @param dstH   输出的高
     **/
    public static native void yuvProcess(byte[] nv21Src, int width, int height, int[] rgbDst,int dstW,int dstH);

    /**
     * 将NV21转化为RGB
     *
     * @param nv21Src 原始NV21数据
     * @param rgbOut 转化后的RGB数据
     * @param width   输出的宽
     * @param width   输出的高
     **/
    public static native void yuvNV21ToRGBA(byte[] nv21Src, int[] rgbOut, int width, int height);


    /**
     * 将NV12转化为RGB
     *
     * @param nv12Src 原始NV21数据
     * @param rgbOut 转化后的RGB数据
     * @param width   输出的宽
     * @param width   输出的高
     **/
    public static native void yuvNV12ToRGBA(byte[] nv12Src, int[] rgbOut, int width, int height);

    /**
     * 将NV21转化为RGB
     *
     * @param yv12Src 原始YV21数据
     * @param rgbOut 转化后的RGB数据
     * @param width   输出的宽
     * @param width   输出的高
     **/
    public static native void yuvYV12ToRGBA(byte[] yv12Src, int[] rgbOut, int width, int height);


    /**
     * 将I420转化为RGB
     *
     * @param I420Src 原始I420数据
     * @param rgbOut  转化后的RGB数据
     * @param width   输出的宽
     * @param width   输出的高
     **/
    public static native void yuvI420ToRGBA(byte[] I420Src, int[] rgbOut, int width, int height);



    /**
     * 将I420SP转化为RGB
     *
     * @param rgb 原始rgb数据
     * @param out 转化后的bytebuffer数据
     * @param width   输出的宽
     * @param height   输出的高
     * @param mean   输出的宽
     * @param std   输出的高
     **/
    public static native ByteBuffer normalize(int[] rgb, int width, int height, float mean,float std,ByteBuffer out);
}
