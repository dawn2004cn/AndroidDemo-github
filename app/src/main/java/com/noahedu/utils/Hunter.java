package com.noahedu.utils;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import android.graphics.Bitmap;
import android.os.Environment;


public class Hunter {	
	public static String strSDCardPathString = Environment.getExternalStorageDirectory()+"/";
	public void run() {
		File origin_file = new File(strSDCardPathString+"origin/origin.png");//搜索的图片
		File[] files = new File(strSDCardPathString+"images").listFiles();//图片库
		HashMap<String, Integer> hashmap = new HashMap<String, Integer>();		
		
		Bitmap origin = ImageTools.readImage(origin_file);
		int orgin_fingerprint = getFingerPrint(origin);
		
		for(File file : files){
			String compared_img_name = file.getName();
			Bitmap compared_img = ImageTools.readImage(file);
			int compared_fingerprint = getFingerPrint(compared_img);
			hashmap.put(compared_img_name, compared_fingerprint);
		}
		Iterator iter = hashmap.keySet().iterator();
		while(iter.hasNext()){
			String compared_img_name = (String)iter.next();
			int compared_fingerprint = hashmap.get(compared_img_name);
			int different_num = compareFingerPrint(orgin_fingerprint,compared_fingerprint);
			getResult(compared_img_name, different_num);
		}
		
	}

	/**
	 * @param image
	 * @return
	 */
	private int getFingerPrint(Bitmap image) {
		final int WIDTH = 8;
		final int HEIGHT = 8;
		image = ImageTools.reduceSize(image, WIDTH, HEIGHT);
		double[][] pixels = ImageTools.getGrayValue(image);
		double avg = ImageTools.getAverage(pixels);
		int fingerprint = ImageTools.getFingerPrint(pixels, avg);
		return fingerprint;
	}


	private int compareFingerPrint(int orgin_fingerprint, int compared_fingerprint) {
		int count = 0;
		for(int i = 0; i < 64; i++){
			byte orgin = (byte) (orgin_fingerprint & (1 << i));
			byte compared = (byte) (compared_fingerprint & (1 << i));
			if(orgin != compared){
				count++;
			}
		}
		return count;
	}
	/**
	 *  输出比较结果
	 * @param name
	 * @param num
	 */
	private void getResult(String name, int num) {
		// TODO Auto-generated method stub
		if(null == name){
			throw new NullPointerException();
		}
		if(num >= 10){
			System.out.println(name + "与原图完全不同");
		}else if(num >= 7 && num < 10){
			System.out.println(name + "与原图很少相似");
		}else if(num >= 3 && num < 7){
			System.out.println(name + "与原图有些相似");
		}else if(num >= 1 && num < 3){
			System.out.println(name + "与原图极其相似");
		}else if(num == 0){
			System.out.println(name + "与原图是同一张图");
		}else {
			System.out.println("error");			
		}	
	}
}
