package com.noahedu.utils;

import android.graphics.Bitmap;

public class GreyIdentification {
    private static final int GRAYBIT = 4;

    /**
     * 求三维的灰度直方图
     *
     * @param srcPath
     * @return
     */
    public static double[][] getHistgram(String srcPath) {
        Bitmap img = ImageDigital.readImg(srcPath);
        return getHistogram(img);
    }

    /**
     * hist[0][]red的直方图，hist[1][]green的直方图，hist[2][]blue的直方图
     *
     * @param img 要获取直方图的图像
     * @return 返回r, g, b的三维直方图
     */
    public static double[][] getHistogram(Bitmap img) {
        int w = img.getWidth();
        int h = img.getHeight();
        double[][] hist = new double[3][256];
        int r, g, b;
        int pix[] = new int[w * h];
        //pix = img.getRGB(0, 0, w, h, pix, 0, w);
        img.getPixels(pix, 0, w, 0, 0, w, h);
        for (int i = 0; i < w * h; i++) {
            r = pix[i] >> 16 & 0xff;
            g = pix[i] >> 8 & 0xff;
            b = pix[i] & 0xff;
			/*hr[r] ++;
			hg[g] ++;
			hb[b] ++;*/
            hist[0][r]++;
            hist[1][g]++;
            hist[2][b]++;
        }
        for (int j = 0; j < 256; j++) {
            for (int i = 0; i < 3; i++) {
                hist[i][j] = hist[i][j] / (w * h);
                //System.out.println(hist[i][j] + "  ");
            }
        }
        return hist;
    }

    public double indentification(String srcPath, String destPath) {
        Bitmap srcImg = ImageDigital.readImg(srcPath);
        Bitmap destImg = ImageDigital.readImg(destPath);
        return indentification(srcImg, destImg);
    }

    public double indentification(Bitmap srcImg, Bitmap destImg) {
        double[][] histR = getHistogram(srcImg);
        double[][] histD = getHistogram(destImg);
        return indentification(histR, histD);
    }

    public static double indentification(double[][] histR, double[][] histD) {
        double p = (double) 0.0;
        for (int i = 0; i < histR.length; i++) {
            for (int j = 0; j < histR[0].length; j++) {
                p += Math.sqrt(histR[i][j] * histD[i][j]);
            }
        }
        return p / 3;
    }

    /**
     * 用三维灰度直方图求图像的相似度
     *
     * @param n
     * @param str1
     * @param str2
     */
    public static void histogramIditification(int n, String str1, String str2) {
        double p = 0;
        double[][] histR = GreyIdentification.getHistgram(str1);
        double[][] histD = null;
        for (int i = 0; i < n; i++) {
            histD = GreyIdentification.getHistgram(str2 + (i + 1) + ".jpg");
            p = GreyIdentification.indentification(histR, histD);
            System.out.print((i + 1) + "--" + p + "    ");
        }
    }

    /**
     * 求一维的灰度直方图
     *
     * @param srcPath
     * @return
     */
    public static double[] getHistgram2(String srcPath) {
        Bitmap img = ImageDigital.readImg(srcPath);
        return getHistogram2(img);
    }

    /**
     * 求一维的灰度直方图
     *
     * @param img
     * @return
     */
    public static double[] getHistogram2(Bitmap img) {
        int w = img.getWidth();
        int h = img.getHeight();
        int series = (int) Math.pow(2, GRAYBIT);    //GRAYBIT=4;用12位的int表示灰度值，前4位表示red,中间4们表示green,后面4位表示blue
        int greyScope = 256 / series;
        double[] hist = new double[series * series * series];
        int r, g, b, index;
        int pix[] = new int[w * h];
        //pix = img.getRGB(0, 0, w, h, pix, 0, w);
        img.getPixels(pix, 0, w, 0, 0, w, h);
        for (int i = 0; i < w * h; i++) {
            r = pix[i] >> 16 & 0xff;
            r = r / greyScope;
            g = pix[i] >> 8 & 0xff;
            g = g / greyScope;
            b = pix[i] & 0xff;
            b = b / greyScope;
            index = r << (2 * GRAYBIT) | g << GRAYBIT | b;
            hist[index]++;
        }
        for (int i = 0; i < hist.length; i++) {
            hist[i] = hist[i] / (w * h);
            //System.out.println(hist[i] + "  ");
        }
        return hist;
    }

    public double indentification2(String srcPath, String destPath) {
        Bitmap srcImg = ImageDigital.readImg(srcPath);
        Bitmap destImg = ImageDigital.readImg(destPath);
        return indentification2(srcImg, destImg);
    }

    public double indentification2(Bitmap srcImg, Bitmap destImg) {
        double[] histR = getHistogram2(srcImg);
        double[] histD = getHistogram2(destImg);
        return indentification2(histR, histD);
    }

    public static double indentification2(double[] histR, double[] histD) {
        double p = (double) 0.0;
        for (int i = 0; i < histR.length; i++) {
            p += Math.sqrt(histR[i] * histD[i]);
        }
        return p;
    }

    /**
     * 用一维直方图求图像的相似度
     *
     * @param n
     * @param str1
     * @param str2
     */
    public static void histogramIditification2(int n, String str1, String str2) {
        double p = 0;
        double[] histR = GreyIdentification.getHistgram2(str1);
        double[] histD = null;
        for (int i = 0; i < n; i++) {
            histD = GreyIdentification.getHistgram2(str2 + (i + 1) + ".jpg");
            p = GreyIdentification.indentification2(histR, histD);
            System.out.print((i + 1) + "--" + p + "    ");
        }
    }

}
