/*
图像处理之给定任意四点不规则放缩

 * 基本原理：

计算四个点的增长斜率，使用双线性插值实现像素填充。

废话也懒得说啦，自己看代码吧，我从一个地方抄袭+修改了一下

源来的代码，原因是原来的代码太乱了，也太让人费解了。
*/
package com.noahedu.Image;

import com.noahedu.utils.ImageUtil;
import com.noahedu.utils.ImageUtils;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class PerspectiveFilter
{
	private float x0, y0, x1, y1, x2, y2, x3, y3;  
    private float dx1, dy1, dx2, dy2, dx3, dy3;  
    private float A, B, C, D, E, F, G, H, I;  
    private float a11, a12, a13, a21, a22, a23, a31, a32, a33;  
    private boolean scaled;  
    private int width;  
    private int height;  
      
    public Bitmap filter(Bitmap src, Bitmap dest) {  
        width = src.getWidth();  
        height = src.getHeight();  
        if ( dest == null )  
            //dest = createCompatibleDestImage( src, null );    
        	dest = Bitmap.createBitmap(src);  
        A = a22*a33 - a32*a23;  
        B = a31*a23 - a21*a33;  
        C = a21*a32 - a31*a22;  
        D = a32*a13 - a12*a33;  
        E = a11*a33 - a31*a13;  
        F = a31*a12 - a11*a32;  
        G = a12*a23 - a22*a13;  
        H = a21*a13 - a11*a23;  
        I = a11*a22 - a21*a12;  
        if ( !scaled ) {  
            float invWidth = 1.0f/width;  
            float invHeight = 1.0f/height;  
  
            A *= invWidth;  
            D *= invWidth;  
            G *= invWidth;  
            B *= invHeight;  
            E *= invHeight;  
            H *= invHeight;  
        }  

       // int[] inPixels = getRGB( src, 0, 0, width, height, null );  
        int[] inPixels = new int[width*height];  
        inPixels = ImageUtils.getColors(src, width, height);
  
        int srcWidth = width;  
        int srcHeight = height;  
        int srcWidth1 = width-1;  
        int srcHeight1 = height-1;  
        int outX=0, outY=0;  
        Rectangle transformedSpace = new Rectangle(0, 0, width, height);  
        transformSpace(transformedSpace);  
        outX = transformedSpace.x;  
        outY = transformedSpace.y;  
        int outWidth = transformedSpace.width;  
        int outHeight = transformedSpace.height;  
        // int index = 0;  
        int[] outPixels = new int[transformedSpace.width];  
        float[] out = new float[2];  
  
        for (int y = 0; y < outHeight; y++) {  
            for (int x = 0; x < outWidth; x++) {  
                transformInverse(outX+x, outY+y, out);  
                int srcX = (int)Math.floor( out[0] );  
                int srcY = (int)Math.floor( out[1] );  
                float xWeight = out[0]-srcX;  
                float yWeight = out[1]-srcY;  
                int nw, ne, sw, se;  
  
                if ( srcX >= 0 && srcX < srcWidth1 && srcY >= 0 && srcY < srcHeight1) {  
                    // Easy case, all corners are in the image  
                    int i = srcWidth*srcY + srcX;  
                    nw = inPixels[i];  
                    ne = inPixels[i+1];  
                    sw = inPixels[i+srcWidth];  
                    se = inPixels[i+srcWidth+1];  
                } else {  
                    // Some of the corners are off the image  
                    nw = getPixel( inPixels, srcX, srcY, srcWidth, srcHeight );  
                    ne = getPixel( inPixels, srcX+1, srcY, srcWidth, srcHeight );  
                    sw = getPixel( inPixels, srcX, srcY+1, srcWidth, srcHeight );  
                    se = getPixel( inPixels, srcX+1, srcY+1, srcWidth, srcHeight );  
                }  
                outPixels[x] = ImageMath.bilinearInterpolate(xWeight, yWeight, nw, ne, sw, se);  
            }  
            //setRGB( dest, 0, y, transformedSpace.width, 1, outPixels );  
            dest = ImageUtils.createBitmap(outPixels, width, height);
        }
          
        return dest;  
    }  
      
    protected void transformSpace( Rectangle rect ) {  
        if ( scaled ) {  
            rect.x = (int)Math.min( Math.min( x0, x1 ), Math.min( x2, x3 ) );  
            rect.y = (int)Math.min( Math.min( y0, y1 ), Math.min( y2, y3 ) );  
            rect.width = (int)Math.max( Math.max( x0, x1 ), Math.max( x2, x3 ) ) - rect.x;  
            rect.height = (int)Math.max( Math.max( y0, y1 ), Math.max( y2, y3 ) ) - rect.y;  
            return;  
        }  
    }  
    final private int getPixel( int[] pixels, int x, int y, int width, int height ) {  
        if (x < 0 || x >= width || y < 0 || y >= height) {  
            return pixels[(ImageMath.clamp(y, 0, height-1) * width) + ImageMath.clamp(x, 0, width-1)] & 0x00ffffff;  
        }  
        return pixels[ y*width+x ];  
    }  
      
    public PerspectiveFilter() {  
        this( 0, 0, 1, 0, 1, 1, 0, 1);  
    }  
      
    public PerspectiveFilter(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {  
        unitSquareToQuad(x0, y0, x1, y1, x2, y2, x3, y3);  
    }  
  
    protected void transformInverse( int x, int y, float[] out ) {  
        out[0] = width * (A*x+B*y+C)/(G*x+H*y+I);  
        out[1] = height * (D*x+E*y+F)/(G*x+H*y+I);  
    }  
      
    public void unitSquareToQuad( float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3 ) {  
        this.x0 = x0;  
        this.y0 = y0;  
        this.x1 = x1;  
        this.y1 = y1;  
        this.x2 = x2;  
        this.y2 = y2;  
        this.x3 = x3;  
        this.y3 = y3;  
          
        dx1 = x1-x2;  
        dy1 = y1-y2;  
        dx2 = x3-x2;  
        dy2 = y3-y2;  
        dx3 = x0-x1+x2-x3;  
        dy3 = y0-y1+y2-y3;  
          
        if (dx3 == 0 && dy3 == 0) {  
            a11 = x1-x0;  
            a21 = x2-x1;  
            a31 = x0;  
            a12 = y1-y0;  
            a22 = y2-y1;  
            a32 = y0;  
            a13 = a23 = 0;  
        } else {  
            a13 = (dx3*dy2-dx2*dy3)/(dx1*dy2-dy1*dx2);  
            a23 = (dx1*dy3-dy1*dx3)/(dx1*dy2-dy1*dx2);  
            a11 = x1-x0+a13*x1;  
            a21 = x3-x0+a23*x3;  
            a31 = x0;  
            a12 = y1-y0+a13*y1;  
            a22 = y3-y0+a23*y3;  
            a32 = y0;  
        }  
        a33 = 1;  
        scaled = false;  
    }  
      
    public void setCorners(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {  
        unitSquareToQuad( x0, y0, x1, y1, x2, y2, x3, y3 );  
        scaled = true;  
    }  

}
