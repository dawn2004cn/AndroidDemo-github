package com.noahedu.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5相关工具类
 * Created by tsy on 16/8/15.
 */
public class MD5Utils {

    private static final String TAG = "MD5Utils";

    /**
     * md5加密
     * @param plainText 待加密字符串
     * @return 加密后32位字符串
     */
    public static String getMd5(String plainText) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            LogUtils.e(TAG, "getMd5 error", e);
            return null;
        }

    }
    

	public static String getMD5(String val){
		byte[] hash;
		if(val == null)//val 不能为空
		{
			return "";
		}
		if(val.length() == 0) //val长度不能为0
		{
			return "";
		}
	    try {
	        hash = MessageDigest.getInstance("MD5").digest(val.getBytes("UTF-8"));
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Huh, MD5 should be supported?", e);
	    } catch (UnsupportedEncodingException e) {
	        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
	    }

	    StringBuilder hex = new StringBuilder(hash.length * 2);
	    for (byte b : hash) {
	        if ((b & 0xFF) < 0x10) hex.append("0");
	        hex.append(Integer.toHexString(b & 0xFF));
	    }
	    return hex.toString();
	}
}
