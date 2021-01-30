/*图像处理之添加高斯与泊松噪声 
 * */
package com.noahedu.Image;

import java.util.Random;

import com.noahedu.utils.ImageUtil;
import com.noahedu.utils.ImageUtils;

import android.graphics.Bitmap;

public class NoiseAdditionFilter
{
	public final static double MEAN_FACTOR = 2.0;  
    public final static int POISSON_NOISE_TYPE = 2;  
    public final static int GAUSSION_NOISE_TYPE = 1;
	private static final int SNR = 0;  
    private double _mNoiseFactor = 25;  
    private int _mNoiseType = POISSON_NOISE_TYPE;
	private float means = 0;  
      
    public NoiseAdditionFilter() {  
        System.out.println("Adding Poisson/Gaussion Noise");  
    }  
      
    public void setNoise(double power) {  
        this._mNoiseFactor = power;  
    }  
      
    public void setNoiseType(int type) {  
        this._mNoiseType = type;  
    }  
      
    public Bitmap filter(Bitmap src, Bitmap dest) {  
        int width = src.getWidth();  
        int height = src.getHeight();  
        Random random = new Random();  
        if ( dest == null )  
            //dest = createCompatibleDestImage( src, null );
        	dest = Bitmap.createBitmap(src);    
  
        int[] inPixels = new int[width*height];  
        int[] outPixels = new int[width*height];  
        //getRGB( src, 0, 0, width, height, inPixels );  
        inPixels = ImageUtils.getColors(src, width, height);
        int index = 0;  
        for(int row=0; row<height; row++) {  
            int ta = 0, tr = 0, tg = 0, tb = 0;  
            for(int col=0; col<width; col++) {  
                index = row * width + col;  
                ta = (inPixels[index] >> 24) & 0xff;  
                tr = (inPixels[index] >> 16) & 0xff;  
                tg = (inPixels[index] >> 8) & 0xff;  
                tb = inPixels[index] & 0xff;  
                if(_mNoiseType == POISSON_NOISE_TYPE) {  
                    tr = clamp(addPNoise(tr, random));  
                    tg = clamp(addPNoise(tg, random));  
                    tb = clamp(addPNoise(tb, random));  
                } else if(_mNoiseType == GAUSSION_NOISE_TYPE) {  
                    tr = clamp(addGNoise(tr, random));  
                    tg = clamp(addGNoise(tg, random));  
                    tb = clamp(addGNoise(tb, random));  
                }  
                outPixels[index] = (ta << 24) | (tr << 16) | (tg << 8) | tb;  
            }  
        }  
  
        //setRGB( dest, 0, 0, width, height, outPixels );  
        dest = ImageUtils.createBitmap(outPixels, width, height);
        return dest;  
    }  
      
    private int addGNoise(int tr, Random random) {  
        int v, ran;  
        boolean inRange = false;  
        do {  
            ran = (int)Math.round(random.nextGaussian()*_mNoiseFactor);  
            v = tr + ran;  
            // check whether it is valid single channel value  
            inRange = (v>=0 && v<=255);   
            if (inRange) tr = v;  
        } while (!inRange);  
        return tr;   
    }  
  
    public static int clamp(int p) {  
        return p > 255 ? 255 : (p < 0 ? 0 : p);  
    }  
      
    private int addPNoise(int pixel, Random random) {  
        // init:  
        double L = Math.exp(-_mNoiseFactor * MEAN_FACTOR);  
        int k = 0;  
        double p = 1;  
        do {  
            k++;  
            // Generate uniform random number u in [0,1] and let p ← p × u.  
            p *= random.nextDouble();  
        } while (p >= L);  
        double retValue = Math.max((pixel + (k - 1) / MEAN_FACTOR - _mNoiseFactor), 0);  
        return (int)retValue;  
    }  
    //椒盐噪声
    private Bitmap addSaltAndPepperNoise(Bitmap src, Bitmap dst) {  
        int width = src.getWidth();  
           int height = src.getHeight();  
      
           if ( dst == null )  
               //dst = createCompatibleDestImage( src, null );  
        	   dst = Bitmap.createBitmap(src);  
      
           int[] inPixels = new int[width*height];  
           //getRGB( src, 0, 0, width, height, inPixels );  
           inPixels = ImageUtils.getColors(src, width, height);
             
           int index = 0;  
           int size = (int)(inPixels.length * (1-SNR));  
      
           for(int i=0; i<size; i++) {  
            int row = (int)(Math.random() * (double)height);  
            int col = (int)(Math.random() * (double)width);  
            index = row * width + col;  
            inPixels[index] = (255 << 24) | (255 << 16) | (255 << 8) | 255;  
           }  
      
           //setRGB( dst, 0, 0, width, height, inPixels );  
           dst = ImageUtils.createBitmap(inPixels, width, height);
           return dst;  
    }  
    //这个函数需要修正 bug
    public int getGaussianValue()
    {
		return 0;    	
    }
    //高斯噪声
    private Bitmap gaussianNoise(Bitmap src, Bitmap dst) {  
        int width = src.getWidth();  
        int height = src.getHeight();  
  
        if ( dst == null )  
            //dst = createCompatibleDestImage( src, null );  
        	dst = Bitmap.createBitmap(src);  
  
        int[] inPixels = new int[width*height];  
        int[][][] tempPixels = new int[height][width][4];   
        int[] outPixels = new int[width*height];  
        //getRGB( src, 0, 0, width, height, inPixels );  
        inPixels = ImageUtils.getColors(src, width, height);
        int index = 0;  
        float inMax = 0;  
        float outMax = 0;  
        for(int row=0; row<height; row++) {  
            int ta = 0, tr = 0, tg = 0, tb = 0;  
            for(int col=0; col<width; col++) {  
                index = row * width + col;  
                ta = (inPixels[index] >> 24) & 0xff;  
                tr = (inPixels[index] >> 16) & 0xff;  
                tg = (inPixels[index] >> 8) & 0xff;  
                tb = inPixels[index] & 0xff;  
                if(inMax < tr) {  
                    inMax = tr;  
                }  
                if(inMax < tg) {  
                    inMax = tg;  
                }  
                if(inMax < tb) {  
                    inMax = tb;  
                }  
                tr = (int)((float)tr + getGaussianValue() + this.means );  
                tg = (int)((float)tg + getGaussianValue() + this.means);  
                tb = (int)((float)tb + getGaussianValue() + this.means);  
                if(outMax < tr) {  
                    outMax = tr;  
                }  
                if(outMax < tg) {  
                    outMax = tg;  
                }  
                if(outMax < tb) {  
                    outMax = tb;  
                }  
                tempPixels[row][col][0] = ta;  
                tempPixels[row][col][1] = tr;  
                tempPixels[row][col][2] = tg;  
                tempPixels[row][col][3] = tb;  
            }  
        }  
  
        // Normalization  
        index = 0;  
        float rate = inMax/outMax;  
        for(int row=0; row<height; row++) {  
            int ta = 0, tr = 0, tg = 0, tb = 0;  
            for(int col=0; col<width; col++) {  
                index = row * width + col;  
                ta = tempPixels[row][col][0];  
                tr = tempPixels[row][col][1];  
                tg = tempPixels[row][col][2];  
                tb = tempPixels[row][col][3];  
  
                tr = (int)((float)tr * rate);  
                tg = (int)((float)tg * rate);  
                tb = (int)((float)tb * rate);  
                outPixels[index] = (ta << 24) | (tr << 16) | (tg << 8) | tb;  
            }  
        }  
       // setRGB( dst, 0, 0, width, height, outPixels );  
        dst = ImageUtils.createBitmap(outPixels, width, height);
        return dst;  
    }  

}
