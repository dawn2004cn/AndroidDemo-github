package com.noahedu.Image;

import java.util.ArrayList;
import java.util.List;

import com.noahedu.utils.ImageUtil;
import com.noahedu.utils.ImageUtils;

import android.graphics.Bitmap;

public class HessianFeatureDetector
{
	 private GaussianDerivativeFilter gdFilter;  
	    private double minRejectThreshold = 4.1; // (r+1)^2/r  
	    private List<HessianMatrix> pixelMatrixList;  
	      
	    public HessianFeatureDetector()  
	    {  
	        gdFilter = new GaussianDerivativeFilter();  
	        pixelMatrixList = new ArrayList<HessianMatrix>();  
	    }  
	      
	    public Bitmap filter(Bitmap src, Bitmap dest) {  
	        int width = src.getWidth();  
	        int height = src.getHeight();  
	        initSettings(height, width);  
	        if ( dest == null )  
	            //dest = createCompatibleDestImage( src, null );   
	        	dest = Bitmap.createBitmap(src);  
	  
	        int[] inPixels = new int[width*height];  
	        gdFilter.setDirectionType(GaussianDerivativeFilter.XX_DIRECTION);  
	        Bitmap bixx = gdFilter.filter(src, null);  
	        //getRGB( bixx, 0, 0, width, height, inPixels );  
	        inPixels = ImageUtils.getColors(bixx, width, height);
	        extractPixelData(inPixels, GaussianDerivativeFilter.XX_DIRECTION, height, width);  
	          
	        // YY Direction  
	        gdFilter.setDirectionType(GaussianDerivativeFilter.YY_DIRECTION);  
	        Bitmap biyy = gdFilter.filter(src, null);  
	        //getRGB( biyy, 0, 0, width, height, inPixels );  
	        inPixels = ImageUtils.getColors(biyy, width, height);
	        extractPixelData(inPixels, GaussianDerivativeFilter.YY_DIRECTION, height, width);  
	          
	        // XY Direction  
	        gdFilter.setDirectionType(GaussianDerivativeFilter.XY_DIRECTION);  
	        Bitmap bixy = gdFilter.filter(src, null);  
	        //getRGB( bixy, 0, 0, width, height, inPixels );  
	        inPixels = ImageUtils.getColors(bixy, width, height);
	        extractPixelData(inPixels, GaussianDerivativeFilter.XY_DIRECTION, height, width);  
	          
	        int[] outPixels = new int[width*height];  
	        int index = 0;  
	        for(int row=0; row<height; row++) {  
	            int ta = 0, tr = 0, tg = 0, tb = 0;  
	            for(int col=0; col<width; col++) {  
	                index = row * width + col;  
	                ta = 255;  
	                HessianMatrix hm = pixelMatrixList.get(index);  
	                double[] t = hm.getThreshold();  
	                if(t[0] > minRejectThreshold)  
	                {  
	                    tr = 127;  
	                }  
	                else  
	                {  
	                    tr = 0;  
	                }  
	                if(t[1] > minRejectThreshold)  
	                {  
	                    tg = 127;  
	                }  
	                else  
	                {  
	                    tg = 0;  
	                }  
	                if(t[2] > minRejectThreshold)  
	                {  
	                    tb = 127;  
	                }  
	                else  
	                {  
	                    tb = 0;  
	                }  
	                outPixels[index] = (ta << 24) | (tr << 16) | (tg << 8) | tb;  
	            }  
	        }  
	  
	        //setRGB( dest, 0, 0, width, height, outPixels );  
	        dest = ImageUtils.createBitmap(outPixels, width, height);
	        return dest;  
	    }  
	      
	    private void initSettings(int height, int width)  
	    {  
	        int index = 0;  
	        for(int row=0; row<height; row++) {  
	            for(int col=0; col<width; col++) {  
	                index = row * width + col;  
	                HessianMatrix matrix = new HessianMatrix();  
	                pixelMatrixList.add(index, matrix);  
	            }  
	        }  
	    }  
	      
	    private void extractPixelData(int[] pixels, int type, int height, int width)  
	    {  
	        int index = 0;  
	        for(int row=0; row<height; row++) {  
	            int ta = 0, tr = 0, tg = 0, tb = 0;  
	            for(int col=0; col<width; col++) {  
	                index = row * width + col;  
	                ta = (pixels[index] >> 24) & 0xff;  
	                tr = (pixels[index] >> 16) & 0xff;  
	                tg = (pixels[index] >> 8) & 0xff;  
	                tb = pixels[index] & 0xff;  
	                HessianMatrix matrix = pixelMatrixList.get(index);  
	                if(type == GaussianDerivativeFilter.XX_DIRECTION)  
	                {  
	                    matrix.setXx(new double[]{tr, tg, tb});  
	                }  
	                if(type == GaussianDerivativeFilter.YY_DIRECTION)  
	                {  
	                    matrix.setYy(new double[]{tr, tg, tb});  
	                }  
	                if(type == GaussianDerivativeFilter.XY_DIRECTION)  
	                {  
	                    matrix.setXy(new double[]{tr, tg, tb});  
	                }  
	            }  
	        }  
	    }  
}
