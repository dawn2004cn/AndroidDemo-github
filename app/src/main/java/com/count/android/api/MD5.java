package com.count.android.api;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * 对外提供getMD5(String)方法
 * @author daisg
 *
 */
public class MD5 {
	
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

