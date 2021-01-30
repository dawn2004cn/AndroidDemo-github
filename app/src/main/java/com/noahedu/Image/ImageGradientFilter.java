/*
 * 图像处理之一阶微分应用
 * */
package com.noahedu.Image;

import com.noahedu.utils.ImageUtil;
import com.noahedu.utils.ImageUtils;

import android.graphics.Bitmap;

public class ImageGradientFilter
{
	public final static int X_DIRECTION = 0;  
    public final static int Y_DIRECTION = 2;  
    public final static int XY_DIRECTION = 4;  
      
    private boolean sharp;  
    private int direction;  
      
    public ImageGradientFilter() {  
        direction = XY_DIRECTION; // default;  
        sharp = false;  
    }  
      
    public boolean isSharp() {  
        return sharp;  
    }  
  
    public void setSharp(boolean sharp) {  
        this.sharp = sharp;  
    }  
  
    public int getDirection() {  
        return direction;  
    }  
  
    public void setDirection(int direction) {  
        this.direction = direction;  
    }  
  
    public Bitmap filter(Bitmap src, Bitmap dest) {  
        int width = src.getWidth();  
        int height = src.getHeight();  
  
        if (dest == null )  
            //dest = createCompatibleDestImage( src, null );  
        	dest = Bitmap.createBitmap(src);  
  
        int[] inPixels = new int[width*height];  
        int[] outPixels = new int[width*height];  
        //getRGB( src, 0, 0, width, height, inPixels );  
        inPixels = ImageUtils.getColors(src, width, height);
        int index = 0;  
        double mred, mgreen, mblue;  
        int newX, newY;  
        int index1, index2, index3;  
        for(int row=0; row<height; row++) {  
            int ta = 0, tr = 0, tg = 0, tb = 0;  
            for(int col=0; col<width; col++) {  
                index = row * width + col;  
                  
                // base on roberts operator  
                newX = col + 1;  
                newY = row + 1;  
                if(newX > 0 && newX < width) {  
                    newX = col + 1;  
                } else {  
                    newX = 0;  
                }  
                  
                if(newY > 0 && newY < height) {  
                    newY = row + 1;  
                } else {  
                    newY = 0;  
                }  
                index1 = newY * width + newX;  
                index2 = row * width + newX;  
                index3 = newY * width + col;  
                ta = (inPixels[index] >> 24) & 0xff;  
                tr = (inPixels[index] >> 16) & 0xff;  
                tg = (inPixels[index] >> 8) & 0xff;  
                tb = inPixels[index] & 0xff;  
                  
                int ta1 = (inPixels[index1] >> 24) & 0xff;  
                int tr1 = (inPixels[index1] >> 16) & 0xff;  
                int tg1 = (inPixels[index1] >> 8) & 0xff;  
                int tb1 = inPixels[index1] & 0xff;  
                  
                int xgred = tr -tr1;  
                int xggreen = tg - tg1;  
                int xgblue = tb - tb1;  
                  
                int ta2 = (inPixels[index2] >> 24) & 0xff;  
                int tr2 = (inPixels[index2] >> 16) & 0xff;  
                int tg2 = (inPixels[index2] >> 8) & 0xff;  
                int tb2 = inPixels[index2] & 0xff;  
                  
                int ta3 = (inPixels[index3] >> 24) & 0xff;  
                int tr3 = (inPixels[index3] >> 16) & 0xff;  
                int tg3 = (inPixels[index3] >> 8) & 0xff;  
                int tb3 = inPixels[index3] & 0xff;  
                  
                int ygred = tr2 - tr3;  
                int yggreen = tg2 - tg3;  
                int ygblue = tb2 - tb3;  
                  
                mred = Math.sqrt(xgred * xgred + ygred * ygred);  
                mgreen = Math.sqrt(xggreen * xggreen + yggreen * yggreen);  
                mblue = Math.sqrt(xgblue * xgblue + ygblue * ygblue);  
                if(sharp) {  
                    tr = (int)(tr + mred);  
                    tg = (int)(tg + mgreen);  
                    tb = (int)(tb + mblue);  
                    outPixels[index] = (ta << 24) | (clamp(tr) << 16) | (clamp(tg) << 8) | clamp(tb);  
                } else {  
                    outPixels[index] = (ta << 24) | (clamp((int)mred) << 16) | (clamp((int)mgreen) << 8) | clamp((int)mblue);  
                    // outPixels[index] = (ta << 24) | (clamp((int)ygred) << 16) | (clamp((int)yggreen) << 8) | clamp((int)ygblue);  
                    // outPixels[index] = (ta << 24) | (clamp((int)xgred) << 16) | (clamp((int)xggreen) << 8) | clamp((int)xgblue);  
                }  
                  
                  
            }  
        }  
        //setRGB(dest, 0, 0, width, height, outPixels );  
        dest = ImageUtils.createBitmap(outPixels, width, height);
        return dest;  
    }  
  
    public static int clamp(int c) {  
        if (c < 0)  
            return 0;  
        if (c > 255)  
            return 255;  
        return c;  
    }  
}
