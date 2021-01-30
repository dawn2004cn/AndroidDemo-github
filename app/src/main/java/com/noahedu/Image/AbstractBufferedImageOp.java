package com.noahedu.Image;

import android.graphics.Bitmap;

public class AbstractBufferedImageOp implements Cloneable
{	
	   public Bitmap createCompatibleDestImage(Bitmap src) {     
	        return  Bitmap.createBitmap(src);
	    }
	/**
	 * A convenience method for getting ARGB pixels from an image. This tries to avoid the performance
	 * penalty of BufferedImage.getRGB unmanaging the image.
	     * @param image   a BufferedImage object
	     * @param x       the left edge of the pixel block
	     * @param y       the right edge of the pixel block
	     * @param width   the width of the pixel arry
	     * @param height  the height of the pixel arry
	     * @param pixels  the array to hold the returned pixels. May be null.
	     * @return the pixels
	     * @see #setRGB
	     */
	public int[] getRGB( Bitmap image, int x, int y, int width, int height, int[] pixels ) {
			image.getPixels(pixels,0,width, x, y, width, height);
			return pixels;
    }
	/**
 * A convenience method for setting ARGB pixels in an image. This tries to avoid the performance
 * penalty of BufferedImage.setRGB unmanaging the image.
     * @param image   a BufferedImage object
     * @param x       the left edge of the pixel block
     * @param y       the right edge of the pixel block
     * @param width   the width of the pixel arry
     * @param height  the height of the pixel arry
     * @param pixels  the array of pixels to set
     * @see #getRGB
 */
public void setRGB( Bitmap image, int x, int y, int width, int height, int[] pixels ) {
			image.setPixels(pixels, 0, width,x, y, width, height );
    	}

	public Object clone() {
		try {
			return super.clone();
		}
		catch ( CloneNotSupportedException e ) {
			return null;
		}
	}
}
