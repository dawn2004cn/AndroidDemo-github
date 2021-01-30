/*
 * // Geometric Moments Computing  
// low-order moments - calculate the center point  
// second-order moments - get angle size  
// projection - 
 * 图像处理之计算二值连通区域的质心 
 * */
package com.noahedu.Image;

import com.noahedu.utils.ImageUtil;
import com.noahedu.utils.ImageUtils;

import android.graphics.Bitmap;

public class GeometricMomentsFilter
{
	public Bitmap filter(Bitmap src, Bitmap dest) {  
        int width = src.getWidth();  
        int height = src.getHeight();  
  
        if ( dest == null )  
            //dest = createCompatibleDestImage( src, null );  
        	dest = Bitmap.createBitmap(src);  
  
        // first step - make it as binary image output pixel  
        int[] inPixels = new int[width*height];  
        int[] outPixels = new int[width*height];  
        //getRGB( src, 0, 0, width, height, inPixels );  
        inPixels = ImageUtils.getColors(src, width, height);
        int index = 0;  
        for(int row=0; row<height; row++) {  
            int tr = 0;  
            for(int col=0; col<width; col++) {  
                index = row * width + col;  
                tr = (inPixels[index] >> 16) & 0xff;  
                if(tr > 127)  
                {  
                     outPixels[index] = 1;  
                }  
                else  
                {  
                    outPixels[index] = 0;  
                }  
            }  
        }  
          
        // second step, connected component labeling algorithm  
        FastConnectedComponentLabelAlg ccLabelAlg = new FastConnectedComponentLabelAlg();  
        //ccLabelAlg.setBgColor(0);  
        int[] labels = ccLabelAlg.doLabel(outPixels, width, height);  
        int max = 0;  
        for(int i=0; i<labels.length; i++)  
        {  
            if(max < labels[i])  
            {  
                System.out.println("Label Index = " + labels[i]);  
                max = labels[i];  
            }  
        }  
          
        // third step, calculate center point of each region area(connected component)  
        int[] input = new int[labels.length];  
        GeometricMomentsAlg momentsAlg = new GeometricMomentsAlg();  
        momentsAlg.setBACKGROUND(0);  
        double[][] labelCenterPos = new double[max][2];  
        for(int i=1; i<=max; i++)  
        {  
            for(int p=0; p<input.length; p++)  
            {  
                if(labels[p] == i)  
                {  
                    input[p] = labels[p];                     
                }  
                else  
                {  
                    input[p] = 0;  
                }  
            }  
            labelCenterPos[i-1] = momentsAlg.getGeometricCenterCoordinate(input, width, height);  
        }  
          
        // render the each connected component center position  
        for(int row=0; row<height; row++) {  
            for(int col=0; col<width; col++) {  
                index = row * width + col;  
                if(labels[index] == 0)  
                {  
                    outPixels[index] = (255 << 24) | (0 << 16) | (0 << 8) | 0; // make it as black for background  
                }  
                else  
                {  
                    outPixels[index] = (255 << 24) | (0 << 16) | (0 << 8) | 100; // make it as blue for each region area  
                }  
            }  
        }  
          
        // make it as white color for each center position  
        for(int i=0; i<max; i++)  
        {  
            int crow = (int)labelCenterPos[i][0];  
            int ccol = (int)labelCenterPos[i][1];  
            index = crow * width + ccol;  
            outPixels[index] = (255 << 24) | (255 << 16) | (255 << 8) | 255;   
        }  
          
        //setRGB( dest, 0, 0, width, height, outPixels );  
        dest = ImageUtils.createBitmap(outPixels, width, height);
        return dest;  
    }  
}
