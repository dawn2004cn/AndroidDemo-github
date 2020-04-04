package com.noahedu.common.http.util;

import android.content.Context;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class OkHttpsUtil {

    private Context context;
    private OkHttpClient.Builder clientBuilder;
    private String[] hostNames;

    public static void toHttps(Context context, OkHttpClient.Builder clientBuilder, String... hostName) {
        toHttps(context, HttpsUtil.TYPE_HTTPS_BOTH_AUTH, clientBuilder, hostName);
    }

    public static void toHttps(Context context, int type, OkHttpClient.Builder clientBuilder, String... hostName) {
        new OkHttpsUtil(context, type, clientBuilder, hostName);
    }

    public OkHttpsUtil(Context context, int type, OkHttpClient.Builder clientBuilder, String... hostName) {
        this.context = context;
        if (hostName != null && hostName.length != 0) {
            hostNames = hostName;
        }
        this.clientBuilder = clientBuilder;
        initHttps(type);
    }

    private void initHttps(int type) {
        HttpsUtil httpsUtil = new HttpsUtil(context, type);
        X509TrustManager trustManager = httpsUtil.newTrustManager();
        SSLSocketFactory sslSocketFactory = httpsUtil.newSslSocketFactory(trustManager);
        if (sslSocketFactory != null) {
            clientBuilder.sslSocketFactory(sslSocketFactory, trustManager)
                    .hostnameVerifier(new HttpsUtil.MyHostnameVerifier(hostNames));
        }
    }

}
