/*
 * 图像处理之调整亮度与对比度 
 * */
/*图像处理之调整亮度与对比度
http://blog.csdn.net/jia20003/article/details/7385160
 

很多时候，一张图像被过度曝光显得很白，或者光线不足显得很暗，有时候背景跟图像人物

也观察不清楚，这个时候可以通过调节图像的两个基本属性-亮度与对比度来获得整体效果

的提升，从而得到质量更高的图片。

 

基本原理：

图像亮度本质上图像中每个像素的亮度，每个像素的亮度本质上RGB值的大小，RGB值为0

是像素点为黑色，RGB都为255时像素点最亮，为白色。对比度则是不同像素点之间的差值，

差值越大，对比度越明显。从直方图分析的观点来看，对比度越好的图片，直方图曲线会越

明显，分布也越显得均匀。

 

算法流程：

调整图像亮度与对比度算法主要由以下几个步骤组成：

1.      计算图像的RGB像素均值– M

2.      对图像的每个像素点Remove平均值-M

3.      对去掉平均值以后的像素点 P乘以对比度系数

4.      对步骤上处理以后的像素P加上 M乘以亮度系统

5.      对像素点RGB值完成重新赋值

 

算法系数

对比度 contrast的最佳取值范围在[0 ~ 4],

亮度 brightness的最佳取值范围在[0~ 2]之间
*/
/** 
 * this filter illustrate the brightness and contrast of the image 
 * and demo how to change the both attribute of the image. 
 *  
 * @author gloomy fish 
 * 
 */  
package com.noahedu.Image;

import com.noahedu.utils.ImageUtil;
import com.noahedu.utils.ImageUtils;

import android.graphics.Bitmap;

public class ConBriFilter
{
	private float contrast = 1.5f; // default value;  
    private float brightness = 1.0f; // default value;  
      
    public ConBriFilter() {  
        // do stuff here if you need......  
    }  
      
    public Bitmap filter(Bitmap src, Bitmap dest) {  
        int width = src.getWidth();  
        int height = src.getHeight();  
  
        if ( dest == null )  
            dest = Bitmap.createBitmap(src);  
  
        int[] inPixels = new int[width*height];  
        int[] outPixels = new int[width*height];  
        inPixels = ImageUtils.getColors(src, width, height);
          
        // calculate RED, GREEN, BLUE means of pixel  
        int index = 0;  
        int[] rgbmeans = new int[3];  
        double redSum = 0, greenSum = 0, blueSum = 0;  
        double total = height * width;  
        for(int row=0; row<height; row++) {  
            int ta = 0, tr = 0, tg = 0, tb = 0;  
            for(int col=0; col<width; col++) {  
                index = row * width + col;  
                ta = (inPixels[index] >> 24) & 0xff;  
                tr = (inPixels[index] >> 16) & 0xff;  
                tg = (inPixels[index] >> 8) & 0xff;  
                tb = inPixels[index] & 0xff;  
                redSum += tr;  
                greenSum += tg;  
                blueSum +=tb;  
            }  
        }  
          
        rgbmeans[0] = (int)(redSum / total);  
        rgbmeans[1] = (int)(greenSum / total);  
        rgbmeans[2] = (int)(blueSum / total);  
          
        // adjust contrast and brightness algorithm, here  
        for(int row=0; row<height; row++) {  
            int ta = 0, tr = 0, tg = 0, tb = 0;  
            for(int col=0; col<width; col++) {  
                index = row * width + col;  
                ta = (inPixels[index] >> 24) & 0xff;  
                tr = (inPixels[index] >> 16) & 0xff;  
                tg = (inPixels[index] >> 8) & 0xff;  
                tb = inPixels[index] & 0xff;  
                  
                // remove means  
                tr -=rgbmeans[0];  
                tg -=rgbmeans[1];  
                tb -=rgbmeans[2];  
                  
                // adjust contrast now !!!  
                tr = (int)(tr * getContrast());  
                tg = (int)(tg * getContrast());  
                tb = (int)(tb * getContrast());  
                  
                // adjust brightness  
                tr += (int)(rgbmeans[0] * getBrightness());  
                tg += (int)(rgbmeans[1] * getBrightness());  
                tb += (int)(rgbmeans[2] * getBrightness());  
                outPixels[index] = (ta << 24) | (clamp(tr) << 16) | (clamp(tg) << 8) | clamp(tb);  
            }  
        }  
        dest = ImageUtils.createBitmap(outPixels, width, height);
        return dest;  
    }  
      
    public int clamp(int value) {  
        return value > 255 ? 255 :(value < 0 ? 0 : value);  
    }  
  
    public float getContrast() {  
        return contrast;  
    }  
  
    public void setContrast(float contrast) {  
        this.contrast = contrast;  
    }  
  
    public float getBrightness() {  
        return brightness;  
    }  
  
    public void setBrightness(float brightness) {  
        this.brightness = brightness;  
    }  
}
