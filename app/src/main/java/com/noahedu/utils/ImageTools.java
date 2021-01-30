package com.noahedu.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class ImageTools {
	/**
	 * 读取图片文件
	 * @param file
	 * @return
	 */
	public static Bitmap readImage(File file){
		Bitmap image = null;
		//image = ImageIO.read(file);
		InputStream in;
		try
		{
			in = new FileInputStream(file);
			image = BitmapFactory.decodeStream(in);
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
	/**
	 * 获得信息指纹
	 * @param pixels 灰度值矩阵
	 * @param avg 平均值
	 * @return 返回01串的十进制表示
	 */
	public static int getFingerPrint(double[][] pixels, double avg) {
		
		int width = pixels[0].length;
		int height = pixels.length;
		byte[] bytes = new byte[height*width];
		int count = 0;
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				if(pixels[i][j] >= avg){
					bytes[i*height + j] = 1;
				}else {
					bytes[i*height + j] = 0;
				}
			}
		}
	    int fingerprint = 0;
	    for(int i = 0; i < bytes.length; i++){
	    	fingerprint += (bytes[bytes.length-i-1] << i);
	    }
		return fingerprint;
	}	
	/**
	 * 得到平均值
	 * @param pixels 灰度值矩阵
	 * @return
	 */
	public static double getAverage(double[][] pixels) {
		int width = pixels[0].length;
		int height = pixels.length;
		int count = 0;
		int red = 0;
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				//int pix = (int)pixels[i][j];
				//red = (pix >> 16) & 0xFF;
				count += pixels[i][j];
			}
		}
		return count / (width*height);
	}

	/**
	 * 得到灰度值
	 * @param image
	 * @return
	 */	
	public static double[][] getGrayValue(Bitmap image) {
		int width = image.getWidth();  
	    int height = image.getHeight();  
		double[][] pixels = new double[height][width];
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
	        	pixels[i][j] = computeGrayValue(image.getPixel(i, j)); 
	        }  
	    }  
		return pixels;
	}
	/**
	 * 计算灰度值
	 * @param pixels
	 * @return
	 */
	public static double computeGrayValue(int pixel) {
		int red = (pixel >> 16) & 0xFF;
		int green = (pixel >> 8) & 0xFF;
		int blue = (pixel) & 255;
		return  0.3 * red + 0.58 * green + 0.12 * blue;
	}
	
	/**
	 * 缩小图片
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap reduceSize(Bitmap bitmap, int w, int h) {

        // load the origial Bitmap
        Bitmap BitmapOrg = bitmap;

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        // calculate the scale
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                        height, matrix, true);

        // make a Drawable from Bitmap to allow to set the Bitmap
        // to the ImageView, ImageButton or what ever
        return resizedBitmap;
	}
	private Bitmap loadBitmapFromView(View v) {
		int w=v.getWidth();
		int h=v.getHeight();
		Bitmap bmp=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
		Canvas c=new Canvas(bmp);
		c.drawColor(Color.WHITE);
		/** 如果不设置canvas画布为白色，则生成透明 */
		v.layout(0,0,w,h);
		v.draw(c);
		return bmp;
	}

	/**
	 * 图片合成
	 *
	 * @param srcBitmap
	 * @param dstBitmap
	 * @return
	 */
	static public Bitmap BlendBitmap(Bitmap srcBitmap, Bitmap dstBitmap) {
		if (srcBitmap == null) {
			return null;
		}
		//create the new blank bitmap of same to src
		Bitmap newb = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.ARGB_8888);

		final Paint paint = new Paint();
		final Canvas cv = new Canvas(newb);
		//draw src into bitmap
		cv.drawBitmap(srcBitmap, 0, 0, paint);
		//set paint mode to MULTIPLY
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));

		//draw dst intobitmap
		cv.drawBitmap(dstBitmap, 0, 0, paint);

		return newb;
	}

	static public Bitmap paddingBitmap(Bitmap bitmap){
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int realw = w;
		int realh = h;
		int ww,left,right,top,bottom;
		//横屏
		if (w > h){
			realw = (int)(w*1.1);
			realh = realw;
			ww = (int)(w*0.1);
			left = (int)(w*0.05);
			top = (w+ww-h)/2;
		}
		else {
			realw = w;
			realh = realw;
			left = (h-w)/2;
			top = 0;
		}
		// 背图
		Bitmap newBitmap = Bitmap.createBitmap(realw, realh, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		// 生成白色的
		paint.setColor(0xFF808080);
		canvas.drawBitmap(bitmap, left, top, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
		// 画正方形的
		canvas.drawRect(0, 0, realw, realh, paint);

		return newBitmap;
	}
}
