/*
 * 图像处理之计算连通区域的角度方向
 * 基于空间Moment算法在图像处理与分析中寻找连通区域计算连通区域的中心与角度方

向Moment的一阶可以用来计算区域的中心质点，二阶可以用来证明图像的几个不变性

如旋转不变行，放缩不变性等。基于Moment的二阶计算结果，根据如下公式：


可以得到区域的方向角度。

二：算法流程

1.      读入图像数据

2.      根据连通组件标记算法得到区域

3.      根据中心化Moment算法计算角度

4.      根据中心离心值画出渲染黄色线条
 * */
package com.noahedu.Image;

import com.noahedu.utils.ImageUtil;
import com.noahedu.utils.ImageUtils;

import android.graphics.Bitmap;

public class DirectionRegionMoments
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
	          
	        // third step, calculate the orientation of the region  
	        int[] input = new int[labels.length];  
	        GeometricMomentsAlg momentsAlg = new GeometricMomentsAlg();  
	        momentsAlg.setBACKGROUND(0);  
	        double[][] labelCenterPos = new double[max][2];  
	        double[][] centerAngles = new double[max][3];  
	        for(int i=1; i<=max; i++)  
	        {  
	            int numberOfLabel = 0;  
	            for(int p=0; p<input.length; p++)  
	            {  
	                if(labels[p] == i)  
	                {  
	                    input[p] = labels[p];    
	                    numberOfLabel++;  
	                }  
	                else  
	                {  
	                    input[p] = 0;  
	                }  
	            }  
	            labelCenterPos[i-1] = momentsAlg.getGeometricCenterCoordinate(input, width, height);  
	            double m11 = momentsAlg.centralMoments(input, width, height, 1, 1);  
	            double m02 = momentsAlg.centralMoments(input, width, height, 0, 2);  
	            double m20 = momentsAlg.centralMoments(input, width, height, 2, 0);  
	            double m112 = m11 * m11;  
	            double dd = Math.pow((m20-m02), 2);  
	            double sum1 = Math.sqrt(dd + 4*m112);  
	            double sum2 = m02 + m20;  
	            double a1 = sum2 + sum1;  
	            double a2 = sum2 - sum1;  
	            // double ecc = a1 / a2;  
	              
	            double ra = Math.sqrt((2*a1)/Math.abs(numberOfLabel));  
	            double rb = Math.sqrt((2*a2)/Math.abs(numberOfLabel));  
	            double angle = Math.atan((2*m11)/(m20 - m02))/2.0;  
	            centerAngles[i-1][0] = angle;  
	            centerAngles[i-1][1] = ra;  
	            centerAngles[i-1][2] = rb;  
	        }  
	          
	          
	        // render the angle/orientation info for each region  
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
	          
	        int labelCount = centerAngles.length;  
	        for(int i=0; i<labelCount; i++)  
	        {  
	            System.out.println("Region " + i + "'s angle = " + centerAngles[i][0]);  
	            System.out.println("Region " + i + " ra = " + centerAngles[i][1]);  
	            System.out.println("Region " + i + " rb = " + centerAngles[i][2]);  
	            double sin = Math.sin(centerAngles[i][0]);  
	            double cos = Math.cos(centerAngles[i][0]);  
	            System.out.println("sin = " + sin);  
	            System.out.println("cos = " + cos);  
	            System.out.println();  
	            int crow = (int)labelCenterPos[i][0];  
	            int ccol = (int)labelCenterPos[i][1];  
	            int radius = (int)centerAngles[i][1]; // ra  
	            for(int j=0; j<radius; j++)  
	            {  
	                int drow = (int)(crow - j * sin); // it is trick, display correct angle as you see!!!  
	                int dcol = (int)(ccol + j * cos);  
	                if(drow >= height) continue;  
	                if(dcol >= width) continue;  
	                index = drow * width + dcol;  
	                outPixels[index] = (255 << 24) | (255 << 16) | (255 << 8) | 0;   
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
