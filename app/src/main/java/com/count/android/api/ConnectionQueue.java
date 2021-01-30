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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ConnectionQueue queues session and event data and periodically sends that data to
 * a Count.ly server on a background thread.
 *
 * None of the methods in this class are synchronized because access to this class is
 * controlled by the Countly singleton, which is synchronized.
 *
 * NOTE: This class is only public to facilitate unit testing, because
 *       of this bug in dexmaker: https://code.google.com/p/dexmaker/issues/detail?id=34
 */
public class ConnectionQueue {
    private CountlyStore store_;
    private ExecutorService executor_queue_;
    private ExecutorService executor_;
    private String appKey_;    
    private WeakReference<Context> context_  = null;  
//    private Context context_;
    private String serverURL_;
    private Future<?> connectionProcessorFuture_;    
    private Future<?> queueProcessorFuture_;    

    // Getters are for unit testing
    String getAppKey() {
        return appKey_;
    }

    void setAppKey(final String appKey) {
        appKey_ = appKey;
    }

    Context getContext() {
    	if(context_ != null)
    	{
            return context_.get();
    	}
    	return null;
    }

    void setContext(final Context context) {
    	context_ = new WeakReference<Context>(context);
 //       context_ = context;
    }

    String getServerURL() {
        return serverURL_;
    }

    void setServerURL(final String serverURL) {
        serverURL_ = serverURL;
    }

    CountlyStore getCountlyStore() {
        return store_;
    }

    void setCountlyStore(final CountlyStore countlyStore) {
        store_ = countlyStore;
    }

    /**
     * Checks internal state and throws IllegalStateException if state is invalid to begin use.
     * @throws IllegalStateException if context, app key, store, or server URL have not been set
     */
    void checkInternalState() {
        if (context_ == null) {
            throw new IllegalStateException("context has not been set");
        }
        if (appKey_ == null || appKey_.length() == 0) {
            throw new IllegalStateException("app key has not been set");
        }
        if (store_ == null) {
            throw new IllegalStateException("countly store has not been set");
        }
        if (serverURL_ == null || !Countly.isValidURL(serverURL_)) {
            throw new IllegalStateException("server URL is not valid");
        }
    }

    /**
     * Records a session start event for the app and sends it to the server.
     * @throws IllegalStateException if context, app key, store, or server URL have not been set
     */
    void beginSession(Context ctx) {
        checkInternalState();
        final String data = "app_key=" + appKey_
                          + "&timestamp=" + Countly.currentTimestamp()
                          + "&begin_session=1"
                          + "&product_type="+DeviceInfo.getDeviceModel()
                          + "&appinfo=" + DeviceInfo.getAppInfo(ctx)
                          + "&deviceinfo=" + DeviceInfo.getMetrics(getContext());

        
        //store_.addConnection(data);

        tick(data);
        tick();
    }

    /**
     * Records a session duration event for the app and sends it to the server. This method does nothing
     * if passed a negative or zero duration.
     * @param duration duration in seconds to extend the current app session, should be more than zero
     * @throws IllegalStateException if context, app key, store, or server URL have not been set
     */
    void updateSession(final int duration) {
        checkInternalState();
        if (duration > 0) {
            final String data = "app_key=" + appKey_
                              + "&timestamp=" + Countly.currentTimestamp()
                    		  + "&product_type="+DeviceInfo.getDeviceModel()
                              + "&session_duration=" + duration
            				  + "&deviceinfo=" + DeviceInfo.getMetrics(getContext());

            //store_.addConnection(data);

            tick(data);
            tick();
        }
    }

    /**
     * Records a session end event for the app and sends it to the server. Duration is only included in
     * the session end event if it is more than zero.
     * @param duration duration in seconds to extend the current app session
     * @throws IllegalStateException if context, app key, store, or server URL have not been set
     */
    void endSession(final Context ctx,final long duration) {
        checkInternalState();
        long e_time = Countly.currentTimestamp();
        long s_time = e_time-duration;
        String data = "app_key=" + appKey_
                    + "&timestamp=" + e_time
                    + "&product_type="+DeviceInfo.getDeviceModel()
                    + "&appinfo=" + DeviceInfo.getAppInfo(ctx)
                    + "&begin_session=" + s_time 
                    + "&end_session=" + e_time
        			+ "&deviceinfo=" + DeviceInfo.getMetrics(getContext());
        if (duration > 0) {
            data += "&session_duration=" + duration;
        }

//        store_.addConnection(data);

        tick(data);
        tick();
    }
	
	   /**
     * Reports a crash with device data to the server.
     * @throws IllegalStateException if context, app key, store, or server URL have not been set
     */
    void sendCrashReport(String error, boolean nonfatal) {
        checkInternalState();
        final String data = "app_key=" + appKey_
                + "&timestamp=" + Countly.currentTimestamp()
                + "&product_type=" + DeviceInfo.getDeviceModel()
                + "&appinfo=" + DeviceInfo.getAppInfo(getContext())
    			+ "&deviceinfo=" + DeviceInfo.getMetrics(getContext())
                + "&events=" + CrashDetails.getCrashData(getContext(), error, nonfatal);

        //store_.addConnection(data);

        tick(data);
        tick();
    }
    
    /**
     * Reports a device info to the server.
     * @throws IllegalStateException if context, app key, store, or server URL have not been set
     */
    void sendDeviceReport(final Context ctx) {
        checkInternalState();
        
        final String data = DeviceInfo.getDeviceString(ctx);
        //store_.addConnection(data);

        tick(data);
        tick();
    }

    /**
     * Records the specified events and sends them to the server.
     * @param events URL-encoded JSON string of event data
     * @throws IllegalStateException if context, app key, store, or server URL have not been set
     */
    void recordEvents(final String events) {
        checkInternalState();
        boolean delay = false;
        try {
        	final String s = java.net.URLDecoder.decode(events);
        	final JSONArray eventArray = new JSONArray(s);
        	for (int i = 0; i < eventArray.length();i++) {
        		final Event event = Event.fromJSON(new JSONObject(eventArray.get(i).toString()));
    			if(event != null)
    			{
    				delay = event.delay;
    			}
            }
        	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        final String data = "app_key=" + appKey_
                          + "&timestamp=" + Countly.currentTimestamp()
                          + "&product_type=" + DeviceInfo.getDeviceModel()
                          + "&delay=" + delay
                          + "&events=" + events
						  + "&deviceinfo=" + DeviceInfo.getMetrics(getContext());

//        store_.addConnection(data);

        tick(data);
        tick();
    }
    

    /**
     * Records the specified events and sends them to the server.
     * @param events URL-encoded JSON string of event data
     * @throws IllegalStateException if context, app key, store, or server URL have not been set
     */
    void recordEvents(final Context ctx,final String events) {
        checkInternalState();
        boolean delay = false;
        try {
        	final String s = java.net.URLDecoder.decode(events);
        	final JSONArray eventArray = new JSONArray(s);
        	for (int i = 0; i < eventArray.length();i++) {
        		final Event event = Event.fromJSON(new JSONObject(eventArray.get(i).toString()));
    			if(event != null)
    			{
    				delay = event.delay;
    			}
            }
        	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        final String data = "app_key=" + appKey_
                          + "&timestamp=" + Countly.currentTimestamp()
                          + "&product_type=" + DeviceInfo.getDeviceModel()
                          + "&delay=" + delay
                          + "&appinfo=" + DeviceInfo.getAppInfo(ctx)
						  + "&deviceinfo=" + DeviceInfo.getMetrics(getContext())
                          + "&events=" + events;

//        store_.addConnection(data);

        if(Countly.sharedInstance().isLoggingEnabled())
        {
        	Log.v(Countly.TAG, "Countly system recordEvents add Connection data");
        }
        tick(data);
        tick();
    }

    /**
     * Ensures that an executor has been created for ConnectionProcessor instances to be submitted to.
     */
    void ensureExecutor() {
        if (executor_ == null) {
            executor_ = Executors.newSingleThreadExecutor();
        }
    }

    /**
     * Ensures that an executor has been created for ConnectionProcessor instances to be submitted to.
     */
    void ensureQueueExecutor() {
        if (executor_queue_ == null) {
        	executor_queue_ = Executors.newSingleThreadExecutor();
        }
    }
    
    /**
     * Starts ConnectionProcessor instances running in the background to
     * process the local connection queue data.
     * Does nothing if there is connection queue data or if a ConnectionProcessor
     * is already running.
     */
    void tick(String data) {
    	//尝试上传
        if ((queueProcessorFuture_ == null || queueProcessorFuture_.isDone())) {
        	ensureQueueExecutor();
            if(isConn(getContext()))
            {
            	//尝试立即上传
            	queueProcessorFuture_ = executor_queue_.submit(new QueueProcessor(serverURL_, store_,data));
            }  
            else
            {
            	//写入本地文件
                store_.addConnection(data);
            }
        }
        else
        {
        	//写入本地文件
            store_.addConnection(data);
            
            if(Countly.sharedInstance().isLoggingEnabled())
            {
            	Log.v(Countly.TAG, "queueProcessorFuture_ is null and addConnection");
            }
        }
    }
    /**
     * Starts ConnectionProcessor instances running in the background to
     * process the local connection queue data.
     * Does nothing if there is connection queue data or if a ConnectionProcessor
     * is already running.
     */
    void tick() {
    	//本地有数据
        if (!store_.isEmptyConnections() && (connectionProcessorFuture_ == null || connectionProcessorFuture_.isDone())) {
            ensureExecutor();
            if(isConn(getContext()))
            {
            	//尝试立即上传
                connectionProcessorFuture_ = executor_.submit(new ConnectionProcessor(serverURL_, store_));
            }            
        }
    }
   /*
     * 判断网络连接是否已开
     * 2012-08-20
     *true 已打开  false 未打开
     * */
    public static boolean isConn(Context context){
        boolean bisConnFlag=false;
        if(context != null)
        {
            ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo network = conManager.getActiveNetworkInfo();
            if(network!=null){
                bisConnFlag=network.isAvailable();
            }        	
        }
        return bisConnFlag;
    }

    // for unit testing
    ExecutorService getExecutor() { return executor_; }
    void setExecutor(final ExecutorService executor) { executor_ = executor; }
    Future<?> getConnectionProcessorFuture() { return connectionProcessorFuture_; }
    void setConnectionProcessorFuture(final Future<?> connectionProcessorFuture) { connectionProcessorFuture_ = connectionProcessorFuture; }
}
