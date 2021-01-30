/*
 * 图像处理之图像梯度效果
 * 二：程序思路及实现

梯度滤镜提供了两个参数：

– 方向，用来要决定图像完成X方向梯度计算， Y方向梯度计算，或者是振幅计算

– 算子类型，用来决定是使用sobel算子或者是prewitt算子。

计算振幅的公式可以参见以前《图像处理之一阶微分应用》的文章
 * */
package com.noahedu.Image;

import com.noahedu.utils.ImageUtil;
import com.noahedu.utils.ImageUtils;

import android.graphics.Bitmap;
/** 
 *  
 * @author gloomy-fish 
 * @date 2012-06-11 
 *  
 * prewitt operator  
 * X-direction 
 * -1, 0, 1 
 * -1, 0, 1 
 * -1, 0, 1 
 *  
 * Y-direction 
 * -1, -1, -1 
 *  0,  0,  0 
 *  1,  1,  1 
 *   
 * sobel operator 
 * X-direction 
 * -1, 0, 1 
 * -2, 0, 2 
 * -1, 0, 1 
 *  
 * Y-direction 
 * -1, -2, -1 
 *  0,  0,  0 
 *  1,  2,  1 
 * 
 */  
public class GradientFilter
{
	// prewitt operator  
    public final static int[][] PREWITT_X = new int[][]{{-1, 0, 1}, {-1, 0, 1}, {-1, 0, 1}};  
    public final static int[][] PREWITT_Y = new int[][]{{-1, -1, -1}, {0,  0,  0}, {1,  1,  1}};  
      
    // sobel operator  
    public final static int[][] SOBEL_X = new int[][]{{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};  
    public final static int[][] SOBEL_Y = new int[][]{{-1, -2, -1}, {0,  0,  0}, {1,  2,  1}};  
      
    // direction parameter  
    public final static int X_DIRECTION = 0;  
    public final static int Y_DIRECTION = 2;  
    public final static int XY_DIRECTION = 4;  
    private int direction;  
    private boolean isSobel;  
      
    public GradientFilter() {  
        direction = XY_DIRECTION;  
        isSobel = true;  
    }  
      
    public void setSoble(boolean sobel) {  
        this.isSobel = sobel;  
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
        int index = 0, index2 = 0;  
        double xred = 0, xgreen = 0, xblue = 0;  
        double yred = 0, ygreen = 0, yblue = 0;  
        int newRow, newCol;  
        for(int row=0; row<height; row++) {  
            int ta = 255, tr = 0, tg = 0, tb = 0;  
            for(int col=0; col<width; col++) {  
                index = row * width + col;  
                for(int subrow = -1; subrow <= 1; subrow++) {  
                    for(int subcol = -1; subcol <= 1; subcol++) {  
                        newRow = row + subrow;  
                        newCol = col + subcol;  
                        if(newRow < 0 || newRow >= height) {  
                            newRow = row;  
                        }  
                        if(newCol < 0 || newCol >= width) {  
                            newCol = col;  
                        }  
                        index2 = newRow * width + newCol;  
                        tr = (inPixels[index2] >> 16) & 0xff;  
                        tg = (inPixels[index2] >> 8) & 0xff;  
                        tb = inPixels[index2] & 0xff;  
                          
                        if(isSobel) {  
                            xred += (SOBEL_X[subrow + 1][subcol + 1] * tr);  
                            xgreen +=(SOBEL_X[subrow + 1][subcol + 1] * tg);  
                            xblue +=(SOBEL_X[subrow + 1][subcol + 1] * tb);  
                              
                            yred += (SOBEL_Y[subrow + 1][subcol + 1] * tr);  
                            ygreen +=(SOBEL_Y[subrow + 1][subcol + 1] * tg);  
                            yblue +=(SOBEL_Y[subrow + 1][subcol + 1] * tb);  
                        } else {  
                            xred += (PREWITT_X[subrow + 1][subcol + 1] * tr);  
                            xgreen +=(PREWITT_X[subrow + 1][subcol + 1] * tg);  
                            xblue +=(PREWITT_X[subrow + 1][subcol + 1] * tb);  
                              
                            yred += (PREWITT_Y[subrow + 1][subcol + 1] * tr);  
                            ygreen +=(PREWITT_Y[subrow + 1][subcol + 1] * tg);  
                            yblue +=(PREWITT_Y[subrow + 1][subcol + 1] * tb);  
                        }  
                    }  
                }  
                  
                double mred = Math.sqrt(xred * xred + yred * yred);  
                double mgreen = Math.sqrt(xgreen * xgreen + ygreen * ygreen);  
                double mblue = Math.sqrt(xblue * xblue + yblue * yblue);  
                if(XY_DIRECTION == direction)   
                {  
                    outPixels[index] = (ta << 24) | (clamp((int)mred) << 16) | (clamp((int)mgreen) << 8) | clamp((int)mblue);  
                }   
                else if(X_DIRECTION == direction)  
                {  
                    outPixels[index] = (ta << 24) | (clamp((int)yred) << 16) | (clamp((int)ygreen) << 8) | clamp((int)yblue);  
                }   
                else if(Y_DIRECTION == direction)   
                {  
                    outPixels[index] = (ta << 24) | (clamp((int)xred) << 16) | (clamp((int)xgreen) << 8) | clamp((int)xblue);  
                }   
                else   
                {  
                    // as default, always XY gradient  
                    outPixels[index] = (ta << 24) | (clamp((int)mred) << 16) | (clamp((int)mgreen) << 8) | clamp((int)mblue);  
                }  
                  
                // cleanup for next loop  
                newRow = newCol = 0;  
                xred = xgreen = xblue = 0;  
                yred = ygreen = yblue = 0;  
                  
            }  
        }         
        //setRGB(dest, 0, 0, width, height, outPixels );  
        dest = ImageUtils.createBitmap(outPixels, width, height);
        return dest;  
    }  
      
    public static int clamp(int value) {  
        return value < 0 ? 0 : (value > 255 ? 255 : value);  
    }  
  
}
