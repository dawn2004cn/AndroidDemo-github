package com.noahedu.utils;

import java.io.File;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.transform.Templates;

import com.noahedu.Image.BilineInterpolationScale;


import android.graphics.Bitmap;
import android.os.Environment;


public class Fingerprint {
    /**
     * 图片缩小后的宽
     */
    public static final int FWIDTH = 8;
    /**
     * 图片缩小后的高
     */
    public static final int FHEIGHT = 8;

    public static String strSDCardPathString = Environment.getExternalStorageDirectory() + "/";

    /**
     * 获得图片的指纹数
     *
     * @param srcPath 图片所在的路径
     * @return 图片的指纹数
     */
    public static String getFingerprint(String srcPath) {
        Bitmap img = ImageDigital.readImg(srcPath);
        int w = img.getWidth();
        int h = img.getHeight();
        int pix[] = new int[w * h];
        //pix = img.getRGB(0, 0, w, h, pix, 0, w);
        img.getPixels(pix, 0, w, 0, 0, w, h);
        long l = getFingerprint(pix, w, h);
        StringBuilder sb = new StringBuilder(Long.toHexString(l));
        if (sb.length() < 16) {
            int n = 16 - sb.length();
            for (int i = 0; i < n; i++) {
                sb.insert(0, "0");
            }
        }
        //System.out.println();
        return sb.toString();
    }

    /**
     * 求图片的指纹
     *
     * @param pix 图像的像素矩阵
     * @param w   图像的宽
     * @param h   图像的高
     * @return
     */
    public static long getFingerprint(int[] pix, int w, int h) {
        pix = AmplificatingShrinking.shrink(pix, w, h, FWIDTH, FHEIGHT);
        int[] newpix = ImageDigital.grayImage(pix, FWIDTH, FHEIGHT);
        int avrPix = averageGray(newpix, FWIDTH, FHEIGHT);
        //int hist[] = new int[FWIDTH*FHEIGHT];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < FWIDTH * FHEIGHT; i++) {
            if (newpix[i] >= avrPix) {
                sb.append("1");
                //hist[i] = 1;
            } else {
                sb.append("0");
                //hist[i] = 0;
            }
            //sb.append(hist[i]);
        }
        //System.out.println(sb.toString());
        long result = 0;
        if (sb.charAt(0) == '0') {
            result = Long.parseLong(sb.toString(), 2);
        } else {
            //如果第一个字符是1，则表示负数，不能直接转换成long，
            result = 0x8000000000000000l ^ Long.parseLong(sb.substring(1), 2);
        }

        return result;
    }

    /**
     * 求灰度图像的均值
     *
     * @param pix 图像的像素矩阵
     * @param w   图像的宽
     * @param h   图像的高
     * @return 灰度均值
     */
    private static int averageGray(int[] pix, int w, int h) {
        int sum = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                sum = sum + pix[i * w + j];
            }

        }
        return (int) (sum / (w * h));
    }

    /**
     * 计算汉明距离
     *
     * @param s1 指纹数1
     * @param s2 指纹数2
     * @return 汉明距离
     */
    public static int hammingDistance(String s1, String s2) {
        int count = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    public static String getFileName(String pathandname) {

        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }

    }

    /**
     * 基于pHash算法的指纹数
     *
     * @param srcPath 图片所在的路径
     * @return 图片的指纹数
     */
    public static String getFingerprintPhash(String srcPath) {
        Bitmap img = ImageDigital.readImg(srcPath);
        int w = img.getWidth();
        int h = img.getHeight();
        int iw = 20;
        int ih = 20;
        int pix[] = new int[w * h];
        Bitmap bmpBitmap;
        img.getPixels(pix, 0, w, 0, 0, w, h);

        if (w < 20 || h < 20) {
            if (w < 16 || h < 16) {
                iw = 8;
                ih = 8;
            } else {
                iw = 16;
                ih = 16;
            }
        }
        //缩放图片
        Bitmap bmp = ImageTools.reduceSize(img, iw, ih);
        pix = ImageUtils.getColors(bmp, iw, ih);
//		bmpBitmap = ImageUtil.createBitmap(pix, iw, ih);
//		ImageDigital.writeImg(bmpBitmap,"png",strSDCardPathString+"shrink/"+getFileName(srcPath)+".png");
        //gray 图片
        pix = ImageDigital.grayImage(pix, iw, ih);
//		bmpBitmap = ImageUtil.createBitmap(pix, 32, 32);
//		ImageDigital.writeImg(bmpBitmap,"png",strSDCardPathString+"gray/"+getFileName(srcPath)+".png");
        //图像转换
        Transformation tf = new Transformation();
        int[] dctPix = tf.DCT(pix, iw);
//		bmpBitmap = ImageUtil.createBitmap(dctPix, 32, 32);
//		ImageDigital.writeImg(bmpBitmap,"png",strSDCardPathString+"trans/"+getFileName(srcPath)+".png");

        int avrPix = averageGray(dctPix, FWIDTH, FHEIGHT);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < FHEIGHT; i++) {
            for (int j = 0; j < FWIDTH; j++) {
                if (dctPix[i * FWIDTH + j] >= avrPix) {
                    sb.append("1");
                } else {
                    sb.append("0");
                }
            }
        }
        //System.out.println(sb.toString());
        long result = 0;
        if (sb.charAt(0) == '0') {
            result = Long.parseLong(sb.toString(), 2);
        } else {
            //如果第一个字符是1，则表示负数，不能直接转换成long，
            result = 0x8000000000000000l ^ Long.parseLong(sb.substring(1), 2);
        }

        sb = new StringBuilder(Long.toHexString(result));
        if (sb.length() < 16) {
            int n = 16 - sb.length();
            for (int i = 0; i < n; i++) {
                sb.insert(0, "0");
            }
        }
        return sb.toString();
    }

    public void run() {
        File origin_file = new File(strSDCardPathString + "images/bmp_image1045.bmp");//搜索的图片
        File[] files = new File(strSDCardPathString + "images").listFiles();//图片库
        HashMap<String, String> hashmap = new HashMap<String, String>();

        Bitmap origin = ImageTools.readImage(origin_file);
        String orgin_fingerprint = getFingerprintPhash(strSDCardPathString + "images/bmp_image1045.bmp");

        for (File file : files) {
            String compared_img_name = file.getPath();
            Bitmap compared_img = ImageTools.readImage(file);
            String compared_fingerprint = getFingerprintPhash(compared_img_name);
            hashmap.put(compared_img_name, compared_fingerprint);
        }
        Iterator iter = hashmap.keySet().iterator();
        while (iter.hasNext()) {
            String compared_img_name = (String) iter.next();
            String compared_fingerprint = hashmap.get(compared_img_name);
            int different_num = hammingDistance(orgin_fingerprint, compared_fingerprint);
            getResult(compared_img_name, different_num);
        }

    }

    /**
     * 输出比较结果
     *
     * @param name
     * @param num
     */
    public static String getResult(String name, int num) {
        // TODO Auto-generated method stub
        if (null == name) {
            throw new NullPointerException();
        }
        if (num >= 10) {
            System.out.println(name + "与原图完全不同");
            return name + "与原图完全不同";
        } else if (num >= 7 && num < 10) {
            System.out.println(name + "与原图很少相似");
            return name + "与原图很少相似";
        } else if (num >= 3 && num < 7) {
            System.out.println(name + "与原图有些相似");
            return name + "与原图有些相似";
        } else if (num >= 1 && num < 3) {
            System.out.println(name + "与原图极其相似");
            return name + "与原图极其相似";
        } else if (num == 0) {
            System.out.println(name + "与原图是同一张图");
            return name + "与原图是同一张图";
        } else {
            System.out.println("error");
            return "error";
        }
    }
    /**
     * @param args
     */
	/*public static void main(String[] args) {
		String s1 = null, s2 = null;
		s1 =  getFingerprintPhash("src\\image\\person.jpg");
		//s1 =  getFingerprint("F:\\image processing\\测试图片素材\\images(0).jpg");
		System.out.println("source: " + s1);
		
		for(int i=1; i<=11; i++) {
			s2 =  getFingerprintPhash("src\\image\\person"+ i + ".jpg");
			//s2 =  getFingerprint("F:\\image processing\\测试图片素材\\images ("+ i + ").jpg");
			//System.out.println("example:" +s2);
			System.out.print(i + "-" + hammingDistance(s1, s2) + "\t");
		}		
	}*/

}

/**
 * source:00c9cfdfdfdfff00
 * 15	11	0	2	0	0
 * <p>
 * 6	4	0	1	1	1
 * <p>
 * 6	4	0	1	1	1
 * <p>
 * hash: 1-1	2-3	3-3	4-5	5-2	6-6	7-2	8-7	9-7	10-4	11-5	12-5	13-9	14-1
 * phash:1-9	2-8	3-14	4-8	5-12	6-11	7-14	8-13	9-14	10-13	11-14	12-15	13-13	14-9
 */
