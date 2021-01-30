/*
 * 人脸识别第二步：皮肤检测，采用的是基于RGB色彩空间的统计结果来判断一个像素是否为skin像

素，如果是皮肤像素，则设置像素为黑色，否则为白色。给出基于RGB色彩空间的五种皮

肤检测统计方法
*/
/** 
 * this skin detection is absolutely good skin classification, 
 * i love this one very much 
 *  
 * this one should be always primary skin detection  
 * from all five filters 
 *  
 * @author zhigang 
 * 
 */  
package com.noahedu.Image;

import com.noahedu.utils.ImageUtil;
import com.noahedu.utils.ImageUtils;

import android.graphics.Bitmap;

public class SkinFilter4
{
	public Bitmap filter(Bitmap src, Bitmap dest) {  
        int width = src.getWidth();  
        int height = src.getHeight();  
  
        if ( dest == null )  
            dest = Bitmap.createBitmap(src);  
  
        int[] inPixels = new int[width*height];  
        int[] outPixels = new int[width*height];  
        inPixels = ImageUtils.getColors(src, width, height);
        int index = 0;  
        for(int row=0; row<height; row++) {  
            int ta = 0, tr = 0, tg = 0, tb = 0;  
            for(int col=0; col<width; col++) {  
                index = row * width + col;  
                ta = (inPixels[index] >> 24) & 0xff;  
                tr = (inPixels[index] >> 16) & 0xff;  
                tg = (inPixels[index] >> 8) & 0xff;  
                tb = inPixels[index] & 0xff;  
                  
                // detect skin method...  
                double sum = tr + tg + tb;  
                if (((double)tb/(double)tg<1.249) &&  
                    ((double)sum/(double)(3*tr)>0.696) &&  
                    (0.3333-(double)tb/(double)sum>0.014) &&  
                    ((double)tg/(double)(3*sum)<0.108))  
                {  
                    tr = tg = tb = 0;  
                } else {  
                    tr = tg = tb = 255;  
                }  
                outPixels[index] = (ta << 24) | (tr << 16) | (tg << 8) | tb;  
            }  
        }  
        dest = ImageUtils.createBitmap(outPixels, width, height);
        return dest;  
    }  
}
