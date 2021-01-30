/*
 * 图像处理之基于一维高斯快速模糊 
 * */
package com.noahedu.Image;

import com.noahedu.utils.ImageUtil;
import com.noahedu.utils.ImageUtils;

import android.graphics.Bitmap;

public class HVGaussianFilter
{
	private float[] gaussianKeneral = null;  
    private int radius = 2; // default value  
    private float sigma = 10;  
    public HVGaussianFilter() {  
          
    }  
      
    public void setSigma(float a) {  
        this.sigma = a;  
    }  
      
    public void setRadius(int size) {  
        this.radius = size;  
    }  
      
    public float[][] getHVGaussianKeneral() {  
        float[][] hvKeneralData = new float[5][5];  
        for(int i=0; i<5; i++)  
        {  
            for(int j=0; j<5; j++)   
            {  
                hvKeneralData[i][j] = gaussianKeneral[i] * gaussianKeneral[j];  
            }  
        }  
        return hvKeneralData;  
    }  
      
    public Bitmap filter(Bitmap src, Bitmap dest) {  
        generateGaussianKeneral(radius, sigma);  
        int width = src.getWidth();  
        int height = src.getHeight();  
  
        if ( dest == null )  
            //dest = createCompatibleDestImage( src, null );  
        	dest = Bitmap.createBitmap(src);   
  
        int[] inPixels = new int[width*height];  
        int[] outPixels = new int[width*height];  
        //getRGB( src, 0, 0, width, height, inPixels);  
        inPixels = ImageUtils.getColors(src, width, height);
        blur( inPixels, outPixels, width, height); // H Gaussian  
        blur( outPixels, inPixels, height, width); // V Gaussain  
       // setRGB(dest, 0, 0, width, height, inPixels );  
        dest = ImageUtils.createBitmap(inPixels, width, height);
        return dest;  
    }  
  
    /** 
     * <p> here is 1D Gaussian        , </p> 
     *  
     * @param inPixels 
     * @param outPixels 
     * @param width 
     * @param height 
     */  
    private void blur(int[] inPixels, int[] outPixels, int width, int height)  
    {  
        int subCol = 0;  
        int index = 0, index2 = 0;  
        float redSum=0, greenSum=0, blueSum=0;  
        for(int row=0; row<height; row++) {  
            int ta = 0, tr = 0, tg = 0, tb = 0;  
            index = row;  
            for(int col=0; col<width; col++) {  
                // index = row * width + col;  
                redSum=0;  
                greenSum=0;  
                blueSum=0;  
                for(int m=-2; m<=2; m++) {  
                    subCol = col + m;  
                    if(subCol < 0 || subCol >= width) {  
                        subCol = 0;  
                    }  
                    index2 = row * width + subCol;  
                    ta = (inPixels[index2] >> 24) & 0xff;  
                    tr = (inPixels[index2] >> 16) & 0xff;  
                    tg = (inPixels[index2] >> 8) & 0xff;  
                    tb = inPixels[index2] & 0xff;  
                    redSum += (tr * gaussianKeneral[m + 2]);  
                    greenSum += (tg * gaussianKeneral[m + 2]);  
                    blueSum += (tb * gaussianKeneral[m + 2]);  
                }  
                outPixels[index] = (ta << 24) | (clamp(redSum) << 16) | (clamp(greenSum) << 8) | clamp(blueSum);  
                index += height;// correct index at here!!!, out put pixels matrix,  
            }  
        }  
    }  
      
    public static int clamp(float a) {  
        return (int)(a < 0 ?  0 : ((a > 255) ? 255 : a));  
    }  
      
    private float[] generateGaussianKeneral(int n, float sigma) {  
        float sigma22 = 2*sigma*sigma;  
        float Pi2 = 2*(float)Math.PI;  
        float sqrtSigmaPi2 = (float)Math.sqrt(Pi2) * sigma ;  
        int size = 2*n + 1;  
        int index = 0;  
        gaussianKeneral = new float[size];  
        float sum = 0.0f;  
        for(int i=-n; i<=n; i++) {  
            float distance = i*i;  
            gaussianKeneral[index] = (float)Math.exp((-distance)/sigma22)/sqrtSigmaPi2;  
            //System.out.println("\t" + gaussianKeneral[index]);  
            sum += gaussianKeneral[index];  
            index++;  
        }  
          
        // nomalization to 1  
        for(int i=0; i<gaussianKeneral.length; i++) {  
            gaussianKeneral[i] = gaussianKeneral[i]/sum;  
            //System.out.println("final gaussian data " + i + " = " + gaussianKeneral[i]);  
        }   
        return gaussianKeneral;  
    }  
  

}
