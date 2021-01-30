/*
 * 人脸识别第一步：图像预处理，预处理的目的是为了减少图像中干扰像素，使得皮肤检测步骤可以得

到更好的效果，最常见的手段是调节对比度与亮度，也可以高斯模糊
 调节对比度的算法
 * */
package com.noahedu.Image;

import com.noahedu.utils.ImageUtil;
import com.noahedu.utils.ImageUtils;

import android.graphics.Bitmap;

public class ContrastFilter extends AbstractBufferedImageOp
{
	private double nContrast = 30;  
    
    public ContrastFilter() {  
        System.out.println("Contrast Filter");  
    }  
  
    public Bitmap filter(Bitmap src, Bitmap dest) {  
        int width = src.getWidth();  
        int height = src.getHeight();  
        double contrast = (100.0 + nContrast) / 100.0;  
        contrast *= contrast;  
        if ( dest == null )  
            dest = Bitmap.createBitmap(src);  
  
        int[] inPixels = new int[width*height];  
        int[] outPixels = new int[width*height];  
        inPixels = ImageUtils.getColors(src, width, height);
        int index = 0;  
        int ta = 0, tr = 0, tg = 0, tb = 0;  
        for(int row=0; row<height; row++) {  
            for(int col=0; col<width; col++) {  
                index = row * width + col;  
                ta = (inPixels[index] >> 24) & 0xff;  
                tr = (inPixels[index] >> 16) & 0xff;  
                tg = (inPixels[index] >> 8) & 0xff;  
                tb = inPixels[index] & 0xff;  
                  
                // adjust contrast - red, green, blue  
                tr = adjustContrast(tr, contrast);  
                tg = adjustContrast(tg, contrast);  
                tb = adjustContrast(tb, contrast);  
                  
                // output RGB pixel  
                outPixels[index] = (ta << 24) | (tr << 16) | (tg << 8) | tb;  
            }  
        }  
        dest = ImageUtils.createBitmap(outPixels, width, height);
        return dest;  
    }  
      
    public int adjustContrast(int color, double contrast) {  
        double result = 0;  
        result = color / 255.0;  
        result -= 0.5;  
        result *= contrast;  
        result += 0.5;  
        result *=255.0;  
        return clamp((int)result);  
    }  
      
    public static int clamp(int c) {  
        if (c < 0)  
            return 0;  
        if (c > 255)  
            return 255;  
        return c;  
    }  
}
