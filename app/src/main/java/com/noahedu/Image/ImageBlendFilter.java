/*
 * 图像处理之基于像素的图像混合 
 * */
package com.noahedu.Image;

import com.noahedu.utils.ImageUtil;
import com.noahedu.utils.ImageUtils;

import android.graphics.Bitmap;

public class ImageBlendFilter
{
	/** Define the blend mode */  
    public final static int MULTIPLY_PIXEL = 1;  
    public final static int SCREEN_PIXEL = 2;  
    public final static int OVERLAY_PIXEL = 3;  
    public final static int SOFTLIGHT_PIXEL = 4;  
    public final static int HARDLIGHT_PIXEL = 5;  
      
    private int mode;  
    private Bitmap secondImage;  
    public ImageBlendFilter() {  
        mode = 1;  
    }  
  
    public void setBlendMode(int mode) {  
        this.mode = mode;  
    }  
      
    public void setSecondImage(Bitmap image) {  
        this.secondImage = image;  
    }        
  
    public Bitmap filter(Bitmap src, Bitmap dest) {  
        checkImages(src);  
        int width = src.getWidth();  
        int height = src.getHeight();  
  
        if ( dest == null )  
           // dest = createCompatibleDestImage( src, null );  
        	dest = Bitmap.createBitmap(src);    
  
        int[] input1 = new int[width*height];  
        int[] input2 = new int[secondImage.getWidth() * secondImage.getHeight()];  
        int[] outPixels = new int[width*height];  
        //getRGB( src, 0, 0, width, height, input1);  
        //getRGB( secondImage, 0, 0, secondImage.getWidth(), secondImage.getHeight(), input2);  
        input1 = ImageUtils.getColors(src, width, height);
        input2 = ImageUtils.getColors(secondImage, secondImage.getWidth(), secondImage.getHeight());
        int index = 0;  
        int ta1 = 0, tr1 = 0, tg1 = 0, tb1 = 0;  
        for(int row=0; row<height; row++) {  
            for(int col=0; col<width; col++) {  
                index = row * width + col;  
                ta1 = (input1[index] >> 24) & 0xff;  
                tr1 = (input1[index] >> 16) & 0xff;  
                tg1 = (input1[index] >> 8) & 0xff;  
                tb1 = input1[index] & 0xff;  
                int[] rgb = getBlendData(tr1, tg1, tb1, input2, row, col);  
                outPixels[index] = (ta1 << 24) | (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];  
                  
            }  
        }  
        //setRGB( dest, 0, 0, width, height, outPixels );  
        dest = ImageUtils.createBitmap(outPixels, width, height);
        return dest;  
    }  
  
    private int[] getBlendData(int tr1, int tg1, int tb1, int[] input,int row, int col) {  
        int width = secondImage.getWidth();  
        int height = secondImage.getHeight();  
        if(col >= width || row >= height) {  
            return new int[]{tr1, tg1, tb1};  
        }  
        int index = row * width + col;  
        // int ta = (input[index] >> 24) & 0xff;  
        int tr = (input[index] >> 16) & 0xff;  
        int tg = (input[index] >> 8) & 0xff;  
        int tb = input[index] & 0xff;  
        int[] rgb = new int[3];  
        if(mode == 1) {  
            rgb[0] = modeOne(tr1, tr);  
            rgb[1] = modeOne(tg1, tg);  
            rgb[2] = modeOne(tb1, tb);  
        }  
        else if(mode == 2) {  
            rgb[0] = modeTwo(tr1, tr);  
            rgb[1] = modeTwo(tg1, tg);  
            rgb[2] = modeTwo(tb1, tb);            
        }  
        else if(mode == 3) {  
            rgb[0] = modeThree(tr1, tr);  
            rgb[1] = modeThree(tg1, tg);  
            rgb[2] = modeThree(tb1, tb);              
        }  
        else if(mode == 4) {  
            rgb[0] = modeFour(tr1, tr);  
            rgb[1] = modeFour(tg1, tg);  
            rgb[2] = modeFour(tb1, tb);           
        }  
        else if(mode == 5) {  
            rgb[0] = modeFive(tr1, tr);  
            rgb[1] = modeFive(tg1, tg);  
            rgb[2] = modeFive(tb1, tb);           
        }  
        return rgb;  
    }  
      
    private int modeOne(int v1, int v2) {  
        return (v1 * v2) / 255;  
    }  
      
    private int modeTwo(int v1, int v2) {  
        return v1 + v2 - v1 * v2 / 255;  
    }  
      
    private int modeThree(int v1, int v2) {  
        return (v2 < 128) ? (2 * v1 * v2 / 255):(255 - 2 * (255 - v1) * (255 - v2) / 255);  
    }  
      
    private int modeFour(double v1, double v2) {  
      if ( v1 > 127.5 ){  
          return (int)(v2 + (255.0 - v2) * ((v1 - 127.5) / 127.5) * (0.5 - Math.abs(v2-127.5)/255.0));  
       }else{  
          return (int)(v2 - v2 * ((127.5 -  v1) / 127.5) * (0.5 - Math.abs(v2-127.5)/255.0));  
       }  
    }  
      
    private int modeFive(double v1, double v2) {  
      if ( v1 > 127.5 ){  
          return (int)(v2 + (255.0 - v2) * ((v1 - 127.5) / 127.5));  
       }else{  
          return (int)(v2 * v1 / 127.5);  
       }  
    }  
  
    private void checkImages(Bitmap src) {  
        int width = src.getWidth();  
        int height = src.getHeight();  
        if(secondImage == null || secondImage.getWidth() > width || secondImage.getHeight() > height) {  
            throw new IllegalArgumentException("the width, height of the input image must be great than blend image");  
        }  
    }  

}
