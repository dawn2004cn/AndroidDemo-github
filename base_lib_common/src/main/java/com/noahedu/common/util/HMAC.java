package com.noahedu.common.util;

import android.os.Environment;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
/*
HMAC
HMAC(Hash Message Authentication Code，散列消息鉴别码，基于密钥的Hash算法的认证协议。
消息鉴别码实现鉴别的原理是，用公开函数和密钥产生一个固定长度的值作为认证标识，用这个标识鉴别消息的完整性。
使用一个密钥生成一个固定大小的小数据块，
即MAC，并将其加入到消息中，然后传输。接收方利用与发送方共享的密钥进行鉴别认证等。*/


/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：HMAC $
 * @file describe：HMAC 加密
 * @anthor :daisg
 * @create time 2020/3/21$ 16:30$
 */
public class HMAC {
    public static final String KEY_MAC = "HmacMD5";

    /**
     * 初始化HMAC密钥
     *
     * @return
     * @throws Exception
     */
    public static String initMacKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
        SecretKey secretKey = keyGenerator.generateKey();
        return BASE64.encryptBASE64(secretKey.getEncoded());
    }

    /**
     * HMAC加密  ：主要方法
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptHMAC(byte[] data, String key) throws Exception {

        SecretKey secretKey = new SecretKeySpec(BASE64.decryptBASE64(key), KEY_MAC);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return new String(mac.doFinal(data));

    }

    public static  String  getResult1(String inputStr)
    {
        String path= Environment.getExternalStorageDirectory()+"";
        String fileSource=path+"/file/HMAC_key.txt";
        LogUtils.v("=======加密前的数据:"+inputStr);
        String  result=null;
        try {
            byte[] inputData = inputStr.getBytes();
            String key = HMAC.initMacKey(); /*产生密钥*/
            LogUtils.v("Mac密钥:===" + key);
            /*将密钥写文件*/
            //Tools.WriteMyFile(fileSource,key);
            result= HMAC.encryptHMAC(inputData, key);
            LogUtils.v("HMAC加密后:===" + result);
        } catch (Exception e) {e.printStackTrace();}
        return result.toString();
    }

    public static  String  getResult2(String inputStr)
    {
        LogUtils.v("=======加密前的数据:"+inputStr);
        String path=Environment.getExternalStorageDirectory()+"";
        String fileSource=path+"/file/HMAC_key.txt";
        String key=null;;
        try {
            /*将密钥从文件中读取*/
            //key=Tools.ReadMyFile(fileSource);
            LogUtils.v("getResult2密钥:===" + key);
        } catch (Exception e1) {
            e1.printStackTrace();}
        String  result=null;
        try {
            byte[] inputData = inputStr.getBytes();
            /*对数据进行加密*/
            result= HMAC.encryptHMAC(inputData, key);
            LogUtils.v("HMAC加密后:===" + result);
        } catch (Exception e) {e.printStackTrace();}
        return result.toString();
    }

    public static void main(String args[])
    {
        try {
            String inputStr = "简单加密";
            /*使用同一密钥：对数据进行加密：查看两次加密的结果是否一样*/
            getResult1(inputStr);
            getResult2(inputStr);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
