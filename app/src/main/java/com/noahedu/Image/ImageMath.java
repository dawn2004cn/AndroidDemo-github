package com.noahedu.Image;

public class ImageMath
{
    public static final float TWO_PI = (float) (2*Math.PI);

	public static int clamp(int value) {  
        return value < 0 ? 0 : (value > 255 ? 255 : value);  
    }

	public static int bilinearInterpolate(float xWeight, float yWeight, int nw,
			int ne, int sw, int se)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public static int clamp(int y, int i, int j)
	{
		// TODO Auto-generated method stub
		return 0;
	}  
}
