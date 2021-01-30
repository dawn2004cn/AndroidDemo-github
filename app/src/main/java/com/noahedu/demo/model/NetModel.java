package com.noahedu.demo.model;

import android.provider.Settings;

import com.baidu.aip.util.Base64Util;
import com.noahedu.common.http.network.Config;
import com.noahedu.common.http.network.NetWorkConfig;
import com.noahedu.common.http.network.RequestResult;
import com.noahedu.common.util.*;
import com.noahedu.demo.R;
import com.noahedu.demo.contants.Constants;
import com.noahedu.demo.contants.RobosysContants;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;

import okhttp3.OkHttpClient;

/**
 * @Description: java类作用描述
 * @Author: huangjialin
 * @CreateDate: 2019/6/19 10:17
 */
public class NetModel implements Config {

    private static NetModel instance = null;
    private static String BASE_URL ="https://newresource.youxuepai.com/";

    public static NetModel getInstance() {
        if (instance == null) {
            synchronized (NetModel.class) {
                if (instance == null) {
                    instance = new NetModel();
                }
            }
        }
        return instance;
    }


    /**
     * 腾讯云中获取视频url
     * http://resource.ulearning.noahedu.com/ures/svc/famouseCource/getBatchUrlByCodes?modelCode=V2C&machine_no=1234567890&open=1&ratio=720&data=[%22This%20is%20the%20way%22]
     */
    public void getTencentVideo(String voiceId, RequestResult requestResult) {
        HashMap<String, String> param = new HashMap<String, String>();

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(voiceId);
        String urlIds = jsonArray.toString();
        param.put("data",urlIds);
        param.put("modelCode", DeviceUtils.getModel());
        param.put("machine_no", DeviceUtils.getProductID());
        param.put("ratio", String.valueOf(720));
        NetWorkConfig.getInstance().get("https://resource.youxuepai.com/ures/svc/famouseCource/getBatchUrlByCodes", param, Constants.GET_TENCENT_VIDEO, requestResult, this);
    }

    public void roboSysLogin(RequestResult requestResult)
    {
        HashMap<String, String> param = new HashMap<>();
        String password = RobosysContants.APP_ID +"-"+DeviceUtils.getProductID()+"-" + RobosysContants.ROBOSYS_FLAG;
        String md5 = MD5Utils.getMD5(password);

        param.put("account_type", RobosysContants.ACCOUNT_TYPE);
        param.put("appid",RobosysContants.APP_ID);
        param.put("deviceid",DeviceUtils.getProductID());
        param.put("password", md5);
        LogUtils.v(password);
        LogUtils.v(MD5Utils.getMD5(password));
        LogUtils.v(Helper.md5(password));
        NetWorkConfig.getInstance().get(RobosysContants.LOGIN_URL, param, Constants.ROBO_SYS_LOGIN, requestResult, this);
    }
    /*appid - 每个用户可以有多个产品类别，每个类别对应一个appid
   sn - 每个产品（appid），会有很多个终端设备，sn是每个设备的唯一标识
   id - 每个产品（appid）， 所授权的内容的唯一编号
   type - 内容的类型， type=page， 是课件页， type=course，是课件
   timestamp - 时间戳，当前的时间UTC格式2019-12-14T16:12:30，注意中间的T，表示UTC格式
   sign - 签名，每次请求，为保证不被篡改，都需要携带签名。
   token - 登录时候返回的TOKEN，每次请求必须携带*/
    public String roboSysGetCourse(String token,String type,String id,String mode)
    {
        String timestamp = TimeUtils.Local2UTC();
        String base_str = "appid="+RobosysContants.APP_ID
                +"&id="+id
                +"&menu=opencourse"
                +"&mode="+mode
                +"&sn="+DeviceUtils.getProductID()
                +"&timestamp="+URLEncoder.encode(timestamp)
                +"&token="+token
                +"&type="+type;
        String base64_str= Base64Util.encode(base_str.getBytes());
        String sign = MD5Utils.getMD5(base64_str+RobosysContants.SECRET_KEY);
        base_str =RobosysContants.COURSE_URL+"?"+base_str + "&sign="+sign;

        return base_str;
    }
    @Override
    public String addBaseUrl() {
        return BASE_URL;
    }

    /**
     * 添加头部
     */
    @Override
    public HashMap<String, String> addHeader() {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        return headerMap;
    }

    /**
     * ssl加密文件
     */
    @Override
    public SSLSocketFactory sslSocketFactory() {
        //return GetSSLFactory.getInstance().getSSLFactory();
        return null;
    }

    @Override
    public HostnameVerifier hostNameVerifier() {
        //return new MyHostnameVerifier();
        return null;
    }

    @Override
    public X509TrustManager x509TrustManager() {
        //return GetSSLFactory.getInstance().getX509TrustManager();
        return null;
    }
}
