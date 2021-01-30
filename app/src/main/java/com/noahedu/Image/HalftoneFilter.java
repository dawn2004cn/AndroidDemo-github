package com.noahedu.Image;

import com.noahedu.utils.ImageUtil;

import android.graphics.Bitmap;

public class HalftoneFilter extends AbstractBufferedImageOp
{
	private float softness = 0.1f;  
    private boolean invert;  
    private Bitmap mask;  
  
    public HalftoneFilter() {  
        System.out.println("Stylize/Halftone...");  
    }  
  
    /** 
     * Set the softness of the effect in the range 0..1. 
     * @param softness the softness 
     * @min-value 0 
     * @max-value 1 
     */  
    public void setSoftness( float softness ) {  
        this.softness = softness;  
    }  
      
    /** 
     * Get the softness of the effect. 
     * @return the softness 
     * @see #setSoftness 
     */  
    public float getSoftness() {  
        return softness;  
    }  
      
    /** 
     * Set the halftone background image. 
     * @param BufferedImage maskImage 
     * @see #getMask 
     */  
    public void setMask( Bitmap maskImage ) {  
        this.mask = maskImage;  
    }  
      
    public void setInvert( boolean invert ) {  
        this.invert = invert;  
    }  
  
    public Bitmap filter( Bitmap src, Bitmap dst ) {  
        int width = src.getWidth();  
        int height = src.getHeight();  
  
        if ( dst == null )  
            //dst = createCompatibleDestImage( src, null );  
        	dst = Bitmap.createBitmap(src);
        if ( mask == null )  
            return dst;  
  
        int maskWidth = mask.getWidth();  
        int maskHeight = mask.getHeight();  
  
        // scale to [0~255]  
        float s = 255*softness;   
  
        int[] inPixels = new int[width];  
        int[] maskPixels = new int[maskWidth];  
  
        for ( int y = 0; y < height; y++ ) {  
            getRGB( src, 0, y, width, 1, inPixels ); // get row pixels  
            getRGB( mask, 0, y % maskHeight, maskWidth, 1, maskPixels ); // get row pixels  
            
  
            for ( int x = 0; x < width; x++ ) {  
                int maskRGB = maskPixels[x % maskWidth];  
                int inRGB = inPixels[x];  
                if ( invert )  
                    maskRGB ^= 0xffffff;  
  
                // start to halftone here!!  
                int ir = (inRGB >> 16) & 0xff;  
                int ig = (inRGB >> 8) & 0xff;  
                int ib = inRGB & 0xff;  
                int mr = (maskRGB >> 16) & 0xff;  
                int mg = (maskRGB >> 8) & 0xff;  
                int mb = maskRGB & 0xff;  
                int r = (int)(255 * (1-cubeInterpolation( ir-s, ir+s, mr )));  
                int g = (int)(255 * (1-cubeInterpolation( ig-s, ig+s, mg )));  
                int b = (int)(255 * (1-cubeInterpolation( ib-s, ib+s, mb )));  
                inPixels[x] = (inRGB & 0xff000000) | (r << 16) | (g << 8) | b;  
                  
            }  
  
            setRGB( dst, 0, y, width, 1, inPixels );  
        }  
  
        return dst;  
    }  
      
    public static float cubeInterpolation(float a, float b, float x) {  
        if (x < a)  
            return 0;  
        if (x >= b)  
            return 1;  
        x = (x - a) / (b - a);  
        return x*x * (3 - 2*x);  
    }  

}
