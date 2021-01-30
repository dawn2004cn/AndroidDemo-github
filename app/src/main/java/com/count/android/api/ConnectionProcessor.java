/*
Copyright (c) 2012, 2013, 2014 Countly

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.count.android.api;

import android.os.Build;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * ConnectionProcessor is a Runnable that is executed on a background
 * thread to submit session & event data to a Count.ly server.
 *
 * NOTE: This class is only public to facilitate unit testing, because
 *       of this bug in dexmaker: https://code.google.com/p/dexmaker/issues/detail?id=34
 */
public class ConnectionProcessor implements Runnable {
    private static final int CONNECT_TIMEOUT_IN_MILLISECONDS = 30000;
    private static final int READ_TIMEOUT_IN_MILLISECONDS = 30000;
    private static final int DELAY_TIMEOUT_IN_MILLISECONDS = 1000;

    private final CountlyStore store_;
    private final String serverURL_;

    ConnectionProcessor(final String serverURL, final CountlyStore store) {
        serverURL_ = serverURL;
        store_ = store;

        // HTTP connection reuse which was buggy pre-froyo
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    }
   /** 
    * 总结： 
     * HttpURLConnection的connect()函数，实际上只是建立了一个与服务器的tcp连接，并没有实际发送http请求。 
     * 无论是post还是get，http请求实际上直到HttpURLConnection的getInputStream()这个函数里面才正式发送出去。 
     *  
     * 对HttpURLConnection对象的一切配置都必须要在connect()函数执行之前完成。 
     * 而对outputStream的写操作，又必须要在inputStream的读操作之前。 
     * 这些顺序实际上是由http请求的格式决定的。 
     *  
     * 在http头后面紧跟着的是http请求的正文，正文的内容是通过outputStream流写入的， 
     * 实际上outputStream不是一个网络流，充其量是个字符串流，往里面写入的东西不会立即发送到网络， 
     * 而是存在于内存缓冲区中，待outputStream流关闭时，根据输入的内容生成http正文。 
     * 至此，http请求的东西已经全部准备就绪。在getInputStream()函数调用的时候，就会把准备好的http请求 
     * 正式发送到服务器了，然后返回一个输入流，用于读取服务器对于此次http请求的返回信息。由于http 
     * 请求在getInputStream的时候已经发送出去了（包括http头和正文），因此在getInputStream()函数 
     * 之后对connection对象进行设置（对http头的信息进行修改）或者写入outputStream（对正文进行修改） 
     * 都是没有意义的了，执行这些操作会导致异常的发生。 
     *  
     */  
    HttpURLConnection urlConnectionForEventData(final String eventData) throws IOException {
        final String urlStr = serverURL_ + "/upload?" + eventData;
        if(Countly.sharedInstance().isLoggingEnabled())
        {
            Log.v(Countly.TAG, "Connection Url=:"+urlStr);        	
        }
        final URL url = new URL(urlStr);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(CONNECT_TIMEOUT_IN_MILLISECONDS);
        conn.setReadTimeout(READ_TIMEOUT_IN_MILLISECONDS);
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(false);
        conn.setRequestMethod("POST"); 
        conn.setRequestProperty("Content-Length","0");    
        
        return conn;
    }

    @Override
    public void run() {
        while (true) {
            final String[] storedEvents = store_.connections();
            if (storedEvents == null || storedEvents.length == 0) {
                // currently no data to send, we are done for now
                break;
            }

            // get first event from collection
            final String deviceId = DeviceInfo.getDeviceID();
            if (deviceId == null) {
                // When device ID is supplied by OpenUDID, in some cases it might take
                // time for OpenUDID service to initialize. So, just wait for it.
                break;
            }

            final String stroredString = store_.getConnection(storedEvents[0]);
       	 
       	 	//if not find in cache then delete this Connection
            if(stroredString == null)
            {
                if (Countly.sharedInstance().isLoggingEnabled()) {
                    Log.d(Countly.TAG, "error -> cache delete it" + storedEvents[0]+"responseData->>>>"+stroredString);
                }            	
                store_.removeConnection(storedEvents[0]);
                continue;
            }
           	 
            //find in cache  then init eventconnect
            String eventData = stroredString + "&device_id=" + deviceId;      
            HttpURLConnection conn = null;
            BufferedInputStream responseStream = null;
            try {    	
                // initialize and open connection
                conn = urlConnectionForEventData(eventData);
                conn.connect();
                // consume response stream
                responseStream = new BufferedInputStream(conn.getInputStream());
                final ByteArrayOutputStream responseData = new ByteArrayOutputStream(256); // big enough to handle success response without reallocating
                int c;
                while ((c = responseStream.read()) != -1) {
                    responseData.write(c);
                }

                // response code has to be 2xx to be considered a success
                boolean success = true;
                if (conn instanceof HttpURLConnection) {
                    final HttpURLConnection httpConn = (HttpURLConnection) conn;
                    final int responseCode = httpConn.getResponseCode();
                    success = responseCode >= 200 && responseCode < 300;
                    if (!success && Countly.sharedInstance().isLoggingEnabled()) {
                        Log.w(Countly.TAG, "HTTP error response code was " + responseCode + " from submitting event data: " + eventData);
                    }
                }

                  //{"msgCode":301,"message":"提交成功","field":null,"id":null}
            	//200:参数为空
            	//230:应用[app_key]不存在
            	//301:提交成功
                // HTTP response code was good, check response JSON contains {"result":"Success"}
                if (success) {
                    final JSONObject responseDict = new JSONObject(responseData.toString("UTF-8"));
                    //success = responseDict.optString("message").equalsIgnoreCase("提交成功");
                    success = (responseDict.optInt("msgCode") == 301);
                    if (!success ) {
                    	if(Countly.sharedInstance().isLoggingEnabled())
                    	{
                            Log.w(Countly.TAG, "Response from Countly server did not report success, it was: " + responseData.toString("UTF-8"));
                    	}
                        //store_.removeConnection(storedEvents[0]);
                    }
                }

                if (success) {
                    if (Countly.sharedInstance().isLoggingEnabled()) {
                        Log.d(Countly.TAG, "ok ->" + eventData+"responseData->>>>"+responseData);
                    }

                    // successfully submitted event data to Count.ly server, so remove
                    // this one from the stored events collection
                    store_.removeConnection(storedEvents[0]);
                }
                else {
                    // warning was logged above, stop processing, let next tick take care of retrying
                    if (Countly.sharedInstance().isLoggingEnabled()) {
                        Log.d(Countly.TAG, "fail ->" + eventData+"responseData->>>>"+responseData);
                    }
                    break;
                }
            }
            catch (Exception e) {
                if (Countly.sharedInstance().isLoggingEnabled()) {
                    Log.w(Countly.TAG, "Got exception while trying to submit event data: " + eventData, e);
                }
                // if exception occurred, stop processing, let next tick take care of retrying
                break;
            }
            finally {
                // free connection resources
                if (responseStream != null) {
                    try { responseStream.close(); } catch (IOException ignored) {}
                }
                if (conn != null && conn instanceof HttpURLConnection) {
                    ((HttpURLConnection)conn).disconnect();
                }
                //这里停1s，再上传
                try {
					Thread.sleep(DELAY_TIMEOUT_IN_MILLISECONDS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

            if (Countly.sharedInstance().isLoggingEnabled())
            {
                Log.i(Countly.TAG, "break " + Thread.currentThread().getName()+"\n");
            }
        }
    }

    // for unit testing
    String getServerURL() { return serverURL_; }
    CountlyStore getCountlyStore() { return store_; }
}
