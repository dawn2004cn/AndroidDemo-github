package com.noahedu.demo.model;

import android.os.Build;

import com.baidu.tts.tools.MD5Util;
import com.noahedu.common.http.network.Config;
import com.noahedu.common.http.network.NetWorkConfig;
import com.noahedu.common.http.network.RequestResult;
import com.noahedu.common.util.DeviceUtils;
import com.noahedu.demo.contants.Constants;

import org.json.JSONArray;

import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

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
        NetWorkConfig.getInstance().get("https://newresource.youxuepai.com/ures/svc/famouseCource/getBatchUrlByCodes", param, Constants.GET_TENCENT_VIDEO, requestResult, this);
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
