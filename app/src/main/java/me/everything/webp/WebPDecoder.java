package me.everything.webp;

import android.graphics.Bitmap;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import android.graphics.BitmapFactory;
import android.os.Build;
import java.nio.ByteBuffer;

public class WebPDecoder {
    private static WebPDecoder instance = null;

    private WebPDecoder() {
        System.loadLibrary("webp_evme");
    }

    public static WebPDecoder getInstance() {
        if (instance == null) {
            synchronized (WebPDecoder.class) {
                if (instance == null) {
                    instance = new WebPDecoder();
                }
            }
        }

        return instance;
    }

    public Bitmap decodeWebP(byte[] encoded) {
        return decodeWebP(encoded, 0, 0);
    }

    public Bitmap decodeWebP(byte[] encoded, int w, int h) {
        int[] width = new int[]{w};
        int[] height = new int[]{h};

        byte[] decoded = decodeRGBAnative(encoded, encoded.length, width, height);
        if (decoded.length == 0) return null;

        int[] pixels = new int[decoded.length / 4];
        ByteBuffer.wrap(decoded).asIntBuffer().get(pixels);

        return Bitmap.createBitmap(pixels, width[0], height[0], Bitmap.Config.ARGB_8888);
    }

    public Bitmap webpToBitmap(InputStream is) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.JELLY_BEAN_MR1) {
            bitmap = WebPDecoder.getInstance().decodeWebP(streamToBytes(is));
        } else {
            bitmap = BitmapFactory.decodeStream(is);
        }
        return bitmap;
    }

    private static byte[] streamToBytes(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len= is.read(buffer)) >=0) {
                os.write(buffer, 0, len);
            }
        } catch (java.io.IOException e) {
        }
        return os.toByteArray();
    }

    public static native byte[] decodeRGBAnative(byte[] encoded, long encodedLength, int[] width, int[] height);
}
