package com.noahedu.utils;

import com.noahedu.Image.BiCubicInterpolationScale;
import com.noahedu.Image.BilineInterpolationScale;
import com.noahedu.Image.NearNeighbourZoom;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * 图像的放大与缩小
 *
 * @author xiaoxu
 */
public class AmplificatingShrinking {

    public static int min(int x, int y) {
        return (x <= y) ? x : y;
    }

    public static int alpha(int color) {
        return (color >> 24) & 0xFF;
    }

    public static int red(int color) {
        return Color.red(color);
    }

    public static int green(int color) {
        return Color.green(color);
    }

    public static int blue(int color) {
        return Color.blue(color);
    }

    public static int ARGB(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    /**
     * 双线性插值法图像的放大
     *
     * @param srcPath
     * @param distPath
     * @param formatName
     * @param k1
     * @param k2
     */
    public static void bilinearityInterpolation(String srcPath, String distPath,
                                                String formatName, float k1, float k2) {
        Bitmap img = ImageDigital.readImg(srcPath);
        Bitmap imgOut = bilinearityInterpolation(img, k1, k2);
        ImageDigital.writeImg(imgOut, formatName, distPath);
    }

    /**
     * 双线性插值法图像的放大
     *
     * @param srcPath
     * @param distPath
     * @param formatName
     * @param k1
     * @param k2
     */
    public static Bitmap bilinearityInterpolation(Bitmap img, int w, int h, int destW, int destH) {

        BilineInterpolationScale biLine = new BilineInterpolationScale();

        int pix[] = new int[w * h];
        img.getPixels(pix, 0, w, 0, 0, w, h);
        int[] newpix = biLine.imgScale(pix, w, h, destW, destH);

        Bitmap imgOut = ImageUtils.createBitmap(newpix, destW, destH);
        return imgOut;
    }

    /**
     * 双线性插值法图像的放大
     *
     * @param img 要缩小的图像对象
     * @param k1  要缩小的列比列
     * @param k2  要缩小的行比列
     * @return 返回处理后的图像对象
     */
    public static Bitmap bilinearityInterpolation(Bitmap img, float k1, float k2) {
        if (k1 < 1 || k2 < 1) {// 如果k1 <1 || k2<1则是图片缩小，不是放大
            System.err
                    .println("this is shrink image funcation, please set k1<=1 and k2<=1！");
            return null;
        }
        float ii = 1 / k1; // 采样的行间距
        float jj = (1 / k2); // 采样的列间距
        int dd = (int) (ii * jj);
        // int m=0 , n=0;
        //	int imgType = img.getType();
        int w = img.getWidth(); // 原图片的宽
        int h = img.getHeight(); // 原图片的宽
        int m = Math.round(k1 * w); // 放大后图片的宽
        int n = Math.round(k2 * h); // 放大后图片的宽
        int[] pix = new int[w * h];
        //pix = img.getRGB(0, 0, w, h, pix, 0, w);
        img.getPixels(pix, 0, w, 0, 0, w, h);
        /*
         * System.out.println(w + " * " + h); System.out.println(m + " * " + n);
         */
        int[] newpix = new int[m * n];

        for (int j = 0; j < h - 1; j++) {
            for (int i = 0; i < w - 1; i++) {
                int x0 = Math.round(i * k1);
                int y0 = Math.round(j * k2);
                int x1, y1;
                if (i == w - 2) {
                    x1 = m - 1;
                } else {
                    x1 = Math.round((i + 1) * k1);
                }
                if (j == h - 2) {
                    y1 = n - 1;
                } else {
                    y1 = Math.round((j + 1) * k2);
                }
                int d1 = x1 - x0;
                int d2 = y1 - y0;
                if (0 == newpix[y0 * m + x0]) {
                    newpix[y0 * m + x0] = pix[j * w + i];
                }
                if (0 == newpix[y0 * m + x1]) {
                    if (i == w - 2) {
                        newpix[y0 * m + x1] = pix[j * w + w - 1];
                    } else {
                        newpix[y0 * m + x1] = pix[j * w + i + 1];
                    }
                }
                if (0 == newpix[y1 * m + x0]) {
                    if (j == h - 2) {
                        newpix[y1 * m + x0] = pix[(h - 1) * w + i];
                    } else {
                        newpix[y1 * m + x0] = pix[(j + 1) * w + i];
                    }
                }
                if (0 == newpix[y1 * m + x1]) {
                    if (i == w - 2 && j == h - 2) {
                        newpix[y1 * m + x1] = pix[(h - 1) * w + w - 1];
                    } else {
                        newpix[y1 * m + x1] = pix[(j + 1) * w + i + 1];
                    }
                }
                int r, g, b;
                float c;
                //ColorModel cm = ColorModel.getRGBdefault();
                for (int l = 0; l < d2; l++) {
                    for (int k = 0; k < d1; k++) {
                        if (0 == l) {
                            // f(x,0) = f(0,0) + c1*(f(1,0)-f(0,0))
                            if (j < h - 1 && newpix[y0 * m + x0 + k] == 0) {
                                c = (float) k / d1;
                                r = red(newpix[y0 * m + x0])
                                        + (int) (c * (red(newpix[y0 * m
                                        + x1]) - red(newpix[y0
                                        * m + x0])));// newpix[(y0+l)*m
                                // + k]
                                g = green(newpix[y0 * m + x0])
                                        + (int) (c * (green(newpix[y0 * m
                                        + x1]) - green(newpix[y0
                                        * m + x0])));
                                b = blue(newpix[y0 * m + x0])
                                        + (int) (c * (blue(newpix[y0 * m
                                        + x1]) - blue(newpix[y0
                                        * m + x0])));
                                newpix[y0 * m + x0 + k] = Color.rgb(r, g, b);
                            }
                            if (j + 1 < h && newpix[y1 * m + x0 + k] == 0) {
                                c = (float) k / d1;
                                r = red(newpix[y1 * m + x0])
                                        + (int) (c * (red(newpix[y1 * m
                                        + x1]) - red(newpix[y1
                                        * m + x0])));
                                g = green(newpix[y1 * m + x0])
                                        + (int) (c * (green(newpix[y1 * m
                                        + x1]) - green(newpix[y1
                                        * m + x0])));
                                b = blue(newpix[y1 * m + x0])
                                        + (int) (c * (blue(newpix[y1 * m
                                        + x1]) - blue(newpix[y1
                                        * m + x0])));
                                newpix[y1 * m + x0 + k] = Color.rgb(r, g, b);
                            }
                            // System.out.println(c);
                        } else {
                            // f(x,y) = f(x,0) + c2*f(f(x,1)-f(x,0))
                            c = (float) l / d2;
                            r = red(newpix[y0 * m + x0 + k])
                                    + (int) (c * (red(newpix[y1 * m + x0
                                    + k]) - red(newpix[y0 * m
                                    + x0 + k])));
                            g = green(newpix[y0 * m + x0 + k])
                                    + (int) (c * (green(newpix[y1 * m
                                    + x0 + k]) - green(newpix[y0
                                    * m + x0 + k])));
                            b = blue(newpix[y0 * m + x0 + k])
                                    + (int) (c * (blue(newpix[y1 * m + x0
                                    + k]) - blue(newpix[y0 * m
                                    + x0 + k])));
                            newpix[(y0 + l) * m + x0 + k] = Color.rgb(r, g, b);
                            // System.out.println((int)(c*(cm.getRed(newpix[y1*m
                            // + x0+k]) - cm.getRed(newpix[y0*m + x0+k]))));
                        }
                    }
                    if (i == w - 2 || l == d2 - 1) { // 最后一列的计算
                        // f(1,y) = f(1,0) + c2*f(f(1,1)-f(1,0))
                        c = (float) l / d2;
                        r = red(newpix[y0 * m + x1])
                                + (int) (c * (red(newpix[y1 * m + x1]) - red(newpix[y0 * m + x1])));
                        g = green(newpix[y0 * m + x1])
                                + (int) (c * (green(newpix[y1 * m + x1]) - green(newpix[y0 * m + x1])));
                        b = blue(newpix[y0 * m + x1])
                                + (int) (c * (blue(newpix[y1 * m + x1]) - blue(newpix[y0 * m + x1])));
                        newpix[(y0 + l) * m + x1] = Color.rgb(r, g, b);
                    }
                }
            }
        }
        /*
         * for(int j=0; j<50; j++){ for(int i=0; i<50; i++) {
         * System.out.print(new Color(newpix[j*m + i]).getRed() + "\t"); }
         * System.out.println(); }
         */
        //	BufferedImage imgOut = new BufferedImage(m, n, imgType);

        //	imgOut.setRGB(0, 0, m, n, newpix, 0, m);


        Bitmap imgOut = Bitmap.createBitmap(m, n, Bitmap.Config.RGB_565);
        imgOut.setPixels(newpix, 0, m, 0, 0, m, n);
        return imgOut;
    }

    /**
     * 双立方插值
     *
     * @param srcPath    原图像的路径
     * @param destPath   目标图像的路径
     * @param formatName 图像文件格式
     * @param k1         图像的宽放大比例
     * @param k2         图像的高放大比例
     */
    public static void biCubicInterpolationScale(String srcPath,
                                                 String destPath, String formatName, float k1, float k2) {
        Bitmap img = ImageDigital.readImg(srcPath);
        BiCubicInterpolationScale biCub = new BiCubicInterpolationScale();
        int w = img.getWidth();
        int h = img.getHeight();
        int destW = Math.round(w * k1);
        int destH = Math.round(h * k2);
        int pix[] = new int[w * h];
//		int type = img.getType();
        img.getPixels(pix, 0, w, 0, 0, w, h);
        int[] newpix = biCub.imgScale(pix, w, h, destW, destH);

        Bitmap imgOut = ImageUtils.createBitmap(newpix, destW, destH);
        ImageDigital.writeImg(imgOut, formatName, destPath);
    }

    /**
     * 双立方插值
     *
     * @param img 原图像
     * @param k1  图像的宽放大比例
     * @param k2  图像的高放大比例
     */
    public static Bitmap biCubicInterpolationScale(Bitmap img, float k1, float k2) {
        BiCubicInterpolationScale biCub = new BiCubicInterpolationScale();
        int w = img.getWidth();
        int h = img.getHeight();
        int destW = Math.round(w * k1);
        int destH = Math.round(h * k2);
        int pix[] = new int[w * h];
//		int type = img.getType();
        img.getPixels(pix, 0, w, 0, 0, w, h);
        int[] newpix = biCub.imgScale(pix, w, h, destW, destH);

        Bitmap imgOut = ImageUtils.createBitmap(newpix, destW, destH);
        return imgOut;
    }

    /**
     * 双立方插值
     *
     * @param img 原图像
     * @param k1  图像的宽放大比例
     * @param k2  图像的高放大比例
     */
    public static Bitmap biCubicInterpolationScale(Bitmap img, int w, int h, int destW, int destH) {
        BiCubicInterpolationScale biCub = new BiCubicInterpolationScale();
        int pix[] = new int[w * h];
//		int type = img.getType();
        img.getPixels(pix, 0, w, 0, 0, w, h);
        int[] newpix = biCub.imgScale(pix, w, h, destW, destH);

        Bitmap imgOut = ImageUtils.createBitmap(newpix, destW, destH);
        return imgOut;
    }

    /**
     * 临近点插值
     *
     * @param img 原图像
     * @param k1  图像的宽放大比例
     * @param k2  图像的高放大比例
     */
    public static Bitmap NearNaighborInterpolationScale(Bitmap img, float k1, float k2) {
        NearNeighbourZoom biCub = new NearNeighbourZoom();
        int w = img.getWidth();
        int h = img.getHeight();
        int destW = Math.round(w * k1);
        int destH = Math.round(h * k2);
        int pix[] = new int[w * h];
//		int type = img.getType();
        img.getPixels(pix, 0, w, 0, 0, w, h);
        int[] newpix = biCub.imgScale(pix, w, h, destW, destH);

        Bitmap imgOut = ImageUtils.createBitmap(newpix, destW, destH);
        return imgOut;
    }

    /**
     * 临近点插值
     *
     * @param img 原图像
     * @param w   图像的宽放大比例
     * @param k2  图像的高放大比例
     */
    public static Bitmap NearNaighborInterpolationScale(Bitmap img, int w, int h, int destW, int destH) {
        NearNeighbourZoom biCub = new NearNeighbourZoom();
        int pix[] = new int[w * h];
        img.getPixels(pix, 0, w, 0, 0, w, h);
        int[] newpix = biCub.imgScale(pix, w, h, destW, destH);

        Bitmap imgOut = ImageUtils.createBitmap(newpix, destW, destH);
        return imgOut;
    }

    /**
     * 等间隔采样的图像放大(缩小)
     *
     * @param img 要放大(缩小)的图像对象
     * @param k1  要放大(缩小)的列比列
     * @param k2  要放大(缩小)的行比列
     * @return 返回处理后的图像对象
     */
    public static Bitmap flex(Bitmap img, float k1, float k2) {
        float ii = 1 / k1; // 采样的行间距
        float jj = 1 / k2; // 采样的列间距
        // int m=0 , n=0;
        //int imgType = img.getType();
        int w = img.getWidth();
        int h = img.getHeight();
        int m = (int) (k1 * w);
        int n = (int) (k2 * h);
        int[] pix = new int[w * h];
        //pix = img.getRGB(0, 0, w, h, pix, 0, w);
        img.getPixels(pix, 0, w, 0, 0, w, h);
        System.out.println(w + " * " + h);
        System.out.println(m + " * " + n);
        int[] newpix = new int[m * n];

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                newpix[j * m + i] = pix[(int) (jj * j) * w + (int) (ii * i)];
            }
        }
        System.out.println((int) ((m - 1) * ii));
        System.out.println("m:" + m + " n:" + n);

        //	BufferedImage imgOut = new BufferedImage(m, n, imgType);

        //	imgOut.setRGB(0, 0, m, n, newpix, 0, m);

//		Bitmap imgOut = Bitmap.createBitmap(m, n, Bitmap.Config.RGB_565);
//		imgOut.setPixels(newpix, 0, n, 0, 0, m, n);
        Bitmap imgOut = ImageUtils.createBitmap(newpix, m, n);
        return imgOut;
    }

    /**
     * 等间隔采样的图像放大(缩小)
     *
     * @param img 要放大(缩小)的图像对象
     * @param m   放大(缩小)后图像的宽
     * @param n   放大(缩小)后图像的高
     * @return 返回处理后的图像对象
     */
    public static Bitmap flex(Bitmap img, int m, int n) {
        float k1 = (float) m / img.getWidth();
        float k2 = (float) n / img.getHeight();
        return flex(img, k1, k2);
    }

    /**
     * 等间隔采样的图像放大(缩小)
     *
     * @param srcPath
     * @param distPath
     * @param formatName
     * @param m
     * @param n
     */
    public static void filexIsometry(String srcPath, String distPath,
                                     String formatName, int m, int n) {
        Bitmap img = ImageDigital.readImg(srcPath);
        Bitmap imgOut = flex(img, m, n);
        ImageDigital.writeImg(imgOut, formatName, distPath);
    }

    /**
     * 局部均值的图像缩小
     *
     * @param img 要缩小的图像对象
     * @param k1  要缩小的列比列
     * @param k2  要缩小的行比列
     * @return 返回处理后的图像对象
     */
    public static Bitmap shrink(Bitmap img, float k1, float k2) {
        if (k1 > 1 || k2 > 1) {// 如果k1 >1 || k2>1则是图片放大，不是缩小
            System.err
                    .println("this is shrink image funcation, please set k1<=1 and k2<=1！");
            return null;
        }
        float ii = 1 / k1; // 采样的行间距
        float jj = 1 / k2; // 采样的列间距
        int dd = (int) (ii * jj);
        // int m=0 , n=0;
        //int imgType = img.getType();
        int w = img.getWidth();
        int h = img.getHeight();
        int m = Math.round(k1 * w);
        int n = Math.round(k2 * h);
        int[] pix = new int[w * h];
        //pix = img.getRGB(0, 0, w, h, pix, 0, w);
        img.getPixels(pix, 0, w, 0, 0, w, h);
        System.out.println(w + " * " + h);
        System.out.println(m + " * " + n);
        int[] newpix = shrink(pix, w, h, m, n);

        //Bitmap imgOut = new Bitmap(m, n, imgType);


        //imgOut.setRGB(0, 0, m, n, newpix, 0, m);
        //Bitmap imgOut = Bitmap.createBitmap(m, n, Bitmap.Config.RGB_565);
        //imgOut.setPixels(newpix, 0, m, 0, 0, m, n);
        Bitmap imgOut = ImageUtils.createBitmap(newpix, m, n);
        return imgOut;
    }

    /**
     * 局部均值的图像缩小
     *
     * @param pix 图像的像素矩阵
     * @param w   原图像的宽
     * @param h   原图像的高
     * @param m   缩小后图像的宽
     * @param n   缩小后图像的高
     * @return
     */
    public static int[] shrink(int[] pix, int w, int h, int m, int n) {
        float k1 = (float) m / w;
        float k2 = (float) n / h;
        int ii = (int) (1 / k1); // 采样的行间距
        int jj = (int) (1 / k2); // 采样的列间距
        int dd = ii * jj;
        // int m=0 , n=0;
        // int imgType = img.getType();
        int[] newpix = new int[m * n];

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                int r = 0, g = 0, b = 0;
                //ColorModel cm = ColorModel.getRGBdefault();
                for (int k = 0; k < jj; k++) {
                    for (int l = 0; l < ii; l++) {
                        r = r
                                + red(pix[(jj * j + k) * w
                                + (ii * i + l)]);
                        g = g
                                + green(pix[(jj * j + k) * w
                                + (ii * i + l)]);
                        b = b
                                + blue(pix[(jj * j + k) * w
                                + (ii * i + l)]);
                    }
                }
                r = r / dd;
                g = g / dd;
                b = b / dd;
                newpix[j * m + i] = 255 << 24 | r << 16 | g << 8 | b;
                // 255<<24 | r<<16 | g<<8 | b 这个公式解释一下，颜色的RGB在内存中是
                // 以二进制的形式保存的，从右到左1-8位表示blue，9-16表示green，17-24表示red
                // 所以"<<24" "<<16" "<<8"分别表示左移24,16,8位

                // newpix[j*m + i] = new Color(r,g,b).getRGB();
            }
        }
        return newpix;
    }

    /**
     * 局部均值的图像缩小
     *
     * @param img 要缩小的图像对象
     * @param m   缩小后图像的宽
     * @param n   缩小后图像的高
     * @return 返回处理后的图像对象
     */
    public static Bitmap shrink(Bitmap img, int m, int n) {
        float k1 = (float) m / img.getWidth();
        float k2 = (float) n / img.getHeight();
        return shrink(img, k1, k2);
    }

}
