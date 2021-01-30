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
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import android.util.Log;

import com.count.android.cache.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class is the public API for the Countly Android SDK.
 * Get more details <a href="https://github.com/Countly/countly-sdk-android">here</a>.
 */
public class Countly {

    /**
     * Current version of the Count.ly Android SDK as a displayable string.
     */
    public static final String COUNTLY_SDK_VERSION_STRING = "1.05";
    
    public static final int COUNTLY_SDK_VERSION_CODE = 105;
    /**
     * Default string used in the begin session metrics if the
     * app version cannot be found.
     */
    public static final String DEFAULT_APP_VERSION = "1.01";
    /**
     * Default string used in the begin session metrics if the
     * app version cannot be found.
     */
    public static final int DEFAULT_APP_VERSIONCode = 2;
    /**
     * Default string used in the begin session metrics if the
     * app version cannot be found.
     */
    public static final String DEFAULT_APP_PACKAGENAME = "com.noahedu";
    /**
     * Tag used in all logging in the Count.ly SDK.
     */
    public static final String TAG = "Countly";
    
    
    public static final String DEFAULT_ENABLE_COUNTLY = "noah.countly.enable";
    
	public static final String DEBUG_ENABLE_COUNTLY = "noah.countly.debug";
	
	public static final String LOGGER_ENABLE_COUNTLY = "noah.countly.logger";

    /**
     * Determines how many custom events can be queued locally before
     * an attempt is made to submit them to a Count.ly server.
     */
    private static final int EVENT_QUEUE_SIZE_THRESHOLD = 10;
    /**
     * How often onTimer() is called.
     */
    private static final long TIMER_DELAY_IN_SECONDS = 60;

    // see http://stackoverflow.com/questions/7048198/thread-safe-singletons-in-java
    private static class SingletonHolder {
        static final Countly instance = new Countly();
    }

    private ConnectionQueue connectionQueue_;
    @SuppressWarnings("FieldCanBeLocal")
    private ScheduledExecutorService timerService_;
    private EventQueue eventQueue_;
    private long prevSessionDurationStartTime_;
    private long startSessionDurationStartTime_;
    private int activityCount_;
    private boolean disableUpdateSessionRequests_;
    private boolean enableLogging_ = false;
    private boolean enableCrash_ = false;
    private boolean enableStorage_ = false;
    private boolean enabledebug_ = false;

    /**
     * Returns the Countly singleton.
     */
    public static Countly sharedInstance() {
        return SingletonHolder.instance;
    }
    
    /**
     * Returns the Countly debug info，if true upload record to test server.
     */
	public synchronized int getEnableDebug()
	{
		int flag = PropertyUtils.getInt(DEBUG_ENABLE_COUNTLY,0);
		return flag;
	}
    /**
     * Returns the Countly debug info，if true upload record to test server.
     */
	public synchronized boolean getNoahDebug()
	{
		//if(SystemProperties.getInt(DEBUG_ENABLE_COUNTLY,0) == 0)
		{
			return enabledebug_;
		}
		//return true;
	}
    /**
     * Constructs a Countly object.
     * Creates a new ConnectionQueue and initializes the session timer.
     */
    Countly() {
        connectionQueue_ = new ConnectionQueue();
        timerService_ = Executors.newSingleThreadScheduledExecutor();
        timerService_.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                onTimer();
            }
        }, TIMER_DELAY_IN_SECONDS, TIMER_DELAY_IN_SECONDS, TimeUnit.SECONDS);
    }
    
    /**
     * Initializes the Countly SDK. Call from your main Activity's onCreate() method.
     * Must be called before other SDK methods can be used.
     * Device ID is supplied by OpenUDID service, see <a href="https://github.com/Countly/countly-sdk-android">Countly</a> for instructions.
     * @param context application context
     * @param serverURL URL of the Countly server to submit data to; use "https://cloud.count.ly" for Countly Cloud
     * @param appKey app key for the application being tracked; find in the Countly Dashboard under Management > Applications
     * @throws IllegalArgumentException if context, serverURL, appKey, or deviceID are invalid
     * @throws IllegalStateException if the Countly SDK has already been initialized
     */
    public void init(final Context context, final String serverURL, final String appKey) {
        init(context, serverURL, appKey, null);
    }

    /**
     * Initializes the Countly SDK. Call from your main Activity's onCreate() method.
     * Must be called before other SDK methods can be used.
     * @param context application context
     * @param serverURL URL of the Countly server to submit data to; use "https://cloud.count.ly" for Countly Cloud
     * @param appKey app key for the application being tracked; find in the Countly Dashboard under Management > Applications
     * @param deviceID unique ID for the device the app is running on; note that null in deviceID means that Countly will use OpenUDID
     * @throws IllegalArgumentException if context, serverURL, appKey, or deviceID are invalid
     * @throws IllegalStateException if init has previously been called with different values during the same application instance
     */
    public synchronized void init(final Context context, final String serverURL, final String appKey, final String deviceID) {

        if(Countly.sharedInstance().isLoggingEnabled())
        {
        	Log.v(Countly.TAG, "Countly system is initing ps waiting");
        }
    	if (context == null) {
            throw new IllegalArgumentException("valid context is required");
        }
        if (!isValidURL(serverURL)) {
            throw new IllegalArgumentException("valid serverURL is required");
        }
        if (appKey == null || appKey.length() == 0) {
            throw new IllegalArgumentException("valid appKey is required");
        }
        if (deviceID != null && deviceID.length() == 0) {
            throw new IllegalArgumentException("valid deviceID is required");
        }
        if (deviceID == null && !OpenUDIDAdapter.isOpenUDIDAvailable()) {
            throw new IllegalArgumentException("valid deviceID is required because OpenUDID is not available");
        }
        if (eventQueue_ != null && (!connectionQueue_.getServerURL().equals(serverURL) ||
                                    !connectionQueue_.getAppKey().equals(appKey) ||
                                    !DeviceInfo.deviceIDEqualsNullSafe(deviceID))) {
            throw new IllegalStateException("Countly cannot be reinitialized with different values");
        }

        IpGetter_manager.sync(context.getApplicationContext());
        // if we get here and eventQueue_ != null, init is being called again with the same values,
        // so there is nothing to do, because we are already initialized with those values
        if (eventQueue_ == null) {
            if (deviceID == null && !OpenUDIDAdapter.isInitialized()) {
                OpenUDIDAdapter.sync(context);
            } else {
                DeviceInfo.setDeviceID(deviceID);
            }            

            final CountlyStore countlyStore = new CountlyStore(context);

            connectionQueue_.setServerURL(serverURL);
            connectionQueue_.setAppKey(appKey);
            connectionQueue_.setCountlyStore(countlyStore);

            eventQueue_ = new EventQueue(countlyStore);
        }

        // context is allowed to be changed on the second init call
        connectionQueue_.setContext(context);
    }

	/**
	 * Checks whether Countly.init has been already called.
	 * @return true if Countly is ready to use
	 */
	public synchronized boolean isInitialized() {
		return eventQueue_ != null;
	}
    /**
     * Immediately disables session & event tracking and clears any stored session & event data.
     * This API is useful if your app has a tracking opt-out switch, and you want to immediately
     * disable tracking when a user opts out. The onStart/onStop/recordEvent methods will throw
     * IllegalStateException after calling this until Countly is reinitialized by calling init
     * again.
     */
    public synchronized void halt() {
        eventQueue_ = null;
        final CountlyStore countlyStore = connectionQueue_.getCountlyStore();
        if (countlyStore != null) {
            countlyStore.clear();
        }
        connectionQueue_.setContext(null);
        connectionQueue_.setServerURL(null);
        connectionQueue_.setAppKey(null);
        connectionQueue_.setCountlyStore(null);
        prevSessionDurationStartTime_ = 0;
        activityCount_ = 0;
        DeviceInfo.setDeviceID(null);
    }

    /**
     * Tells the Countly SDK that an Activity has started. Since Android does not have an
     * easy way to determine when an application instance starts and stops, you must call this
     * method from every one of your Activity's onStart methods for accurate application
     * session tracking.
     * @throws IllegalStateException if Countly SDK has not been initialized
     */
    public synchronized void onStart(Context ctx) {    	
    	//java
        if(PropertyUtils.getInt(DEFAULT_ENABLE_COUNTLY,0) == 0)
        {
            if (eventQueue_ == null) {
            	  StatWrapper.init(ctx);
             //   throw new IllegalStateException("init must be called before onStart");
            }

            ++activityCount_;
            if (activityCount_ == 1) {
                onStartHelper(ctx);
            }

            if(Countly.sharedInstance().getEnableDebug() != 0)
            {
    			Intent show = new Intent(ctx, TopWindowService.class);
    			show.putExtra(TopWindowService.OPERATION,
    					TopWindowService.OPERATION_SHOW);
    			show.putExtra(TopWindowService.PACKAGENAME, DeviceInfo.getActivityName(ctx));
    			ctx.startService(show);
            }
			CrashDetails.inForeground();
        }
    }

    /**
     * Called when the first Activity is started. Sends a begin session event to the server
     * and initializes application session tracking.
     */
    void onStartHelper(Context ctx) {
        prevSessionDurationStartTime_ = System.nanoTime();
        startSessionDurationStartTime_ = Countly.currentTimestamp();
        //connectionQueue_.beginSession(ctx);
    }

    /**
     * Tells the Countly SDK that an Activity has stopped. Since Android does not have an
     * easy way to determine when an application instance starts and stops, you must call this
     * method from every one of your Activity's onStop methods for accurate application
     * session tracking.
     * @throws IllegalStateException if Countly SDK has not been initialized, or if
     *                               unbalanced calls to onStart/onStop are detected
     */
    public synchronized void onStop(Context ctx) {
        if(PropertyUtils.getInt(DEFAULT_ENABLE_COUNTLY,0) == 0)
        {
            if (eventQueue_ == null) {
          	  StatWrapper.init(ctx);
              // throw new IllegalStateException("init must be called before onStop");
            }
            if (activityCount_ == 0) {
                throw new IllegalStateException("must call onStart before onStop");
            }

            --activityCount_;
            if (activityCount_ == 0) {
                onStopHelper(ctx);
            }

            if(Countly.sharedInstance().getEnableDebug() != 0){
				Intent hide = new Intent(ctx, TopWindowService.class);
				hide.putExtra(TopWindowService.OPERATION,
						TopWindowService.OPERATION_HIDE);
				ctx.stopService(hide);
            }
			CrashDetails.inBackground();
		}
	}

    /**
     * Called when final Activity is stopped. Sends an end session event to the server,
     * also sends any unsent custom events.
     */
    void onStopHelper(Context ctx) {
        connectionQueue_.endSession(ctx,roundedSecondsSinceStartSessionDurationUpdate());
        prevSessionDurationStartTime_ = 0;

        if (eventQueue_.size() > 0) {
            connectionQueue_.recordEvents(ctx,eventQueue_.events());
        }
    }    
    public void recordError(final Context context,final Throwable e)
    {
    	if(enableCrash_)
    	{
			logException(e);
		}
	}
	public void recordError(final Context context,final Exception e)
	{
		if(enableCrash_)
		{
			logException(e);
		}
    }
    
    public synchronized void recordError(final Context context,final String error)
    {
    	if(enableCrash_)
    	{
			connectionQueue_.sendCrashReport(error, true);
		}
	}

    /**
     * Records a custom event with no segmentation values, a count of one and a sum of zero.
     * @param key name of the custom event, required, must not be the empty string
     * @throws IllegalStateException if Countly SDK has not been initialized
     * @throws IllegalArgumentException if key is null or empty
     */
    public void recordEvent(final Context context,final String key) {
        recordEvent(context,key, null, 1, 0,false);
    }

    /**
     * Records a custom event with no segmentation values, the specified count, and a sum of zero.
     * @param key name of the custom event, required, must not be the empty string
     * @param count count to associate with the event, should be more than zero
     * @throws IllegalStateException if Countly SDK has not been initialized
     * @throws IllegalArgumentException if key is null or empty
     */
    public void recordEvent(final Context context,final String key, final int count) {
        recordEvent(context,key, null, count, 0,false);
    }

    /**
     * Records a custom event with no segmentation values, and the specified count and sum.
     * @param key name of the custom event, required, must not be the empty string
     * @param count count to associate with the event, should be more than zero
     * @param sum sum to associate with the event
     * @throws IllegalStateException if Countly SDK has not been initialized
     * @throws IllegalArgumentException if key is null or empty
     */
    public void recordEvent(final Context context,final String key, final int count, final double sum) {
        recordEvent(context,key, null, count, sum,false);
    }

    /**
     * Records a custom event with the specified segmentation values and count, and a sum of zero.
     * @param key name of the custom event, required, must not be the empty string
     * @param segmentation segmentation dictionary to associate with the event, can be null
     * @param count count to associate with the event, should be more than zero
     * @throws IllegalStateException if Countly SDK has not been initialized
     * @throws IllegalArgumentException if key is null or empty
     */
    public void recordEvent(final Context context,final String key, final Map<String, String> segmentation, final int count) {
        recordEvent(context,key, segmentation, count, 0,false);
    }
    /**
     * Records a custom event with the specified values.
     * @param key name of the custom event, required, must not be the empty string
     * @param segmentation segmentation dictionary to associate with the event, can be null
     * @param count count to associate with the event, should be more than zero
     * @param sum sum to associate with the event
     * @throws IllegalStateException if Countly SDK has not been initialized
     * @throws IllegalArgumentException if key is null or empty, count is less than 1, or if
     *                                  segmentation contains null or empty keys or values
     */
    public synchronized void recordEvent(final Context context,final String key, final Map<String, String> segmentation, final int count, final double sum) {
    	 recordEvent(context,key, segmentation, count, sum,false);
    }
    /**
     * Records a custom event with the specified values.
     * @param key name of the custom event, required, must not be the empty string
     * @param segmentation segmentation dictionary to associate with the event, can be null
     * @param count count to associate with the event, should be more than zero
     * @param sum sum to associate with the event
     * @throws IllegalStateException if Countly SDK has not been initialized
     * @throws IllegalArgumentException if key is null or empty, count is less than 1, or if
     *                                  segmentation contains null or empty keys or values
     */
    public synchronized void recordEvent(final Context context,final String key, final Map<String, String> segmentation, final int count, final double sum,final boolean delay) {
        if(PropertyUtils.getInt(DEFAULT_ENABLE_COUNTLY,0) == 0)
        {
			if (!isInitialized()) {
           	   StatWrapper.init(context);
                 //throw new IllegalStateException("Countly.sharedInstance().init must be called before recordEvent");
             }
             if (key == null || key.length() == 0) {
                 throw new IllegalArgumentException("Valid Countly event key is required");
             }
             if (count < 1) {
                 throw new IllegalArgumentException("Countly event count should be greater than zero");
             }
             if (segmentation != null) {
                 for (String k : segmentation.keySet()) {
                     if (k == null || k.length() == 0) {
                         throw new IllegalArgumentException("Countly event segmentation key cannot be null or empty");
                     }
                     if (segmentation.get(k) == null || segmentation.get(k).length() == 0) {
                         //throw new IllegalArgumentException("Countly event segmentation value cannot be null or empty");
                    	 if(Countly.sharedInstance().isLoggingEnabled())
                         {
                         	Log.v(Countly.TAG, "Countly event segmentation value cannot be null or empty");
                         }
                     }
                 }
             }
             if(Countly.sharedInstance().isLoggingEnabled())
             {
             	Log.v(Countly.TAG, "Countly event :"+key +" is start");
             }
             eventQueue_.recordEvent(key, segmentation, count, sum,delay);
            // sendEventsIfNeeded();
             sendEventsfast(context);
        }
    }
    	
    /**
	 * Set user location.
	 *
	 * Countly detects user location based on IP address. But for geolocation-enabled apps,
	 * it's better to supply exact location of user.
	 * Allows sending messages to a custom segment of users located in a particular area.
	 *
	 * @param lat Latitude
	 * @param lon Longitude
	 */
	public synchronized void setLocation(double lat, double lon) {
		connectionQueue_.getCountlyStore().setLocation(lat, lon);

		if (disableUpdateSessionRequests_) {
			connectionQueue_.updateSession(roundedSecondsSinceLastSessionDurationUpdate());
		}
	}
	/**
	 * Sets custom segments to be reported with crash reports
	 * In custom segments you can provide any string key values to segments crashes by
	 * @param segments Map&lt;String, String&gt; key segments and their values
	 */
	public synchronized void setCustomCrashSegments(Map<String, String> segments) {
		if(segments != null)
			CrashDetails.setCustomSegments(segments);
	}

	/**
	 * Add crash breadcrumb like log record to the log that will be send together with crash report
	 * @param record String a bread crumb for the crash report
	 */
	public synchronized void addCrashLog(String record) {
		CrashDetails.addLog(record);
	}
	/**
	 * Log handled exception to report it to server as non fatal crash
	 * @param exception Exception to log
	 */
	public synchronized void logException(Throwable exception) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		connectionQueue_.sendCrashReport(sw.toString(), true);
	}
	/**
	 * Log handled exception to report it to server as non fatal crash
	 * @param exception Exception to log
	 */
	public synchronized void logException(Exception exception) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		connectionQueue_.sendCrashReport(sw.toString(), true);
		
        eventQueue_.recordError(sw.toString(), true);

        sendEventsIfNeeded();
        // sendEventsfast(context);
	}

	/**
	 * Enable crash reporting to send unhandled crash reports to server
	 */
	public synchronized void enableCrashReporting() {
		//get default handler
		final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();

		Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				connectionQueue_.sendCrashReport(sw.toString(), false);

				//if there was another handler before
				if(oldHandler != null){
					//notify it also
					oldHandler.uncaughtException(t,e);
				}
			}
		};

		Thread.setDefaultUncaughtExceptionHandler(handler);
	}
    /**
     * Disable periodic session time updates.
     * By default, Countly will send a request to the server each 30 seconds with a small update
     * containing session duration time. This method allows you to disable such behavior.
     * Note that event updates will still be sent every 10 events or 30 seconds after event recording.
     * @param disable whether or not to disable session time updates
     */
    public synchronized void setDisableUpdateSessionRequests(final boolean disable) {
        disableUpdateSessionRequests_ = disable;
    }

    /**
     * Sets whether debug logging is turned on or off. Logging is disabled by default.
     * @param enableLogging true to enable logging, false to disable logging
     */
    public synchronized void setLoggingEnabled(final boolean enableLogging) {
        enableLogging_ = enableLogging;
    }

    public synchronized boolean isLoggingEnabled() {
		if(getEnableDebug() != 0)
		{
			return true;
		}
		if(PropertyUtils.getInt(LOGGER_ENABLE_COUNTLY,0) != 0)
		{
			return true;
		}
        return enableLogging_;
    }
    /**
     * Sets whether catch UncaughtException is turned on or off. Crash is disabled by default.
     * @param enableCrash true to enable Crash, false to disable Crash
     */
    public synchronized void setCatchUncaughtExceptions(final boolean enableCrash)
	{
		enableCrash_ = enableCrash;
    	if(enableCrash_)
    	{
//			Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance());
			enableCrashReporting();
		}
//		enableCrash_ = enableCrash;
	}
    
    public synchronized void setStorageEnable(final boolean enableStorage)
    {
    	enableStorage_ = enableStorage;
    }
    
    public synchronized void setDebugEnable(final boolean enabledebug)
    {
    	enabledebug_ = enabledebug;
    }
    public synchronized boolean isCrashEnabled() {
        return enableCrash_;
    }

    public synchronized boolean isStorageEnabled() {
        return enableStorage_;
    }
    /**
     * Submits all of the locally queued events to the server if there are more than 10 of them.
     */
    void sendEventsIfNeeded() {
        if (eventQueue_.size() >= EVENT_QUEUE_SIZE_THRESHOLD) {
            connectionQueue_.recordEvents(eventQueue_.events());
        }
    }

    /**
     * Submits all of the locally queued events to the server if there are more than 10 of them.
     */
    void sendEventsfast(final Context ctx) {
            connectionQueue_.recordEvents(ctx,eventQueue_.events());
    }
    /**
     * Called every 60 seconds to send a session heartbeat to the server. Does nothing if there
     * is not an active application session.
     */
    synchronized void onTimer() {
        final boolean hasActiveSession = activityCount_ > 0;
        if (hasActiveSession) {
            if (!disableUpdateSessionRequests_) {
                //connectionQueue_.updateSession(roundedSecondsSinceLastSessionDurationUpdate());
            }
        }
        if (eventQueue_.size() > 0) {
            connectionQueue_.recordEvents(eventQueue_.events());
        }
    }

    /**
     * Calculates the unsent session duration in seconds, rounded to the nearest int.
     */
    int roundedSecondsSinceLastSessionDurationUpdate() {
        final long currentTimestampInNanoseconds = System.nanoTime();
        final long unsentSessionLengthInNanoseconds = currentTimestampInNanoseconds - prevSessionDurationStartTime_;
        prevSessionDurationStartTime_ = currentTimestampInNanoseconds;
        return (int) Math.round(unsentSessionLengthInNanoseconds / 1000000000.0d);
    }   
    /**
     * Calculates the unsent session duration in seconds, rounded to the nearest int.
     */
    long roundedSecondsSinceStartSessionDurationUpdate() {
        final long currentTimestampInNanoseconds = Countly.currentTimestamp();
		if(currentTimestampInNanoseconds < startSessionDurationStartTime_ 
        	|| startSessionDurationStartTime_ < 946656000000L 
        	|| currentTimestampInNanoseconds - startSessionDurationStartTime_  > 3600000 )
        {
        	startSessionDurationStartTime_ = currentTimestampInNanoseconds+60;        	
        }
        final long unsentSessionLengthInNanoseconds = currentTimestampInNanoseconds - startSessionDurationStartTime_;
        startSessionDurationStartTime_ = currentTimestampInNanoseconds;
        return (long) unsentSessionLengthInNanoseconds;
    }

    /**
     * Utility method to return a current timestamp that can be used in the Count.ly API.
     */
    static long currentTimestamp() {
        return ((long)(System.currentTimeMillis()));
    }

    /**
     * Utility method for testing validity of a URL.
     */
    static boolean isValidURL(final String urlStr) {
        boolean validURL = false;
        if (urlStr != null && urlStr.length() > 0) {
            try {
                new URL(urlStr);
                validURL = true;
            }
            catch (MalformedURLException e) {
                validURL = false;
            }
        }
        return validURL;
    }

    public String readApplicationInfo(final Context context)
	{
		  ApplicationInfo appInfo = null;
			try {
				appInfo = context.getPackageManager()  
				        .getApplicationInfo(context.getPackageName(),   
				PackageManager.GET_META_DATA);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			if(appInfo != null && appInfo.metaData != null)
			{
				String appID = appInfo.metaData.getString("NoahApp_STAT_ID"); 
				Boolean enableLog = appInfo.metaData.getBoolean("NoahApp_ENABLE_LOG"); 
				Boolean enableError = appInfo.metaData.getBoolean("NoahApp_ENABLE_ERROR"); 
				Boolean enableStorage = appInfo.metaData.getBoolean("NoahApp_ENABLE_STORAGE"); 
				Boolean enabledebug = appInfo.metaData.getBoolean("NoahApp_ENABLE_DEBUG"); 
		        
		        if(enableLog)
		        {
		        	setLoggingEnabled(true);
		        }
		        
		        //if(enableError)
		        {
		        	setCatchUncaughtExceptions(true);
		        }
		        if(enableStorage)
		        {
		        	setStorageEnable(true);
		        }

		        if(enabledebug)
		        {
		        	setDebugEnable(true);
		        }
		        
		        return appID;				
			}
			return null;
	}

    public synchronized void recordDownload(final Context context,
    		final String resName,final String resType,
    		final String resId,final String resSuffix,final String netType)
    {
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("resName", resName);
    	map.put("resType", resType);
    	map.put("resId", resId);
    	map.put("resSuffix", resSuffix);
    	map.put("netType", netType);
    	recordEvent(context,"NH_Download_Res",map,1,1,false);
    }
    public synchronized void recordVisit(final Context context,
    		final String resName,final String resType,
    		final String resId,final String resFrom)
    {
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("resName", resName);
    	map.put("resType", resType);
    	map.put("resId", resId);
    	map.put("resFrom", resFrom);
    	recordEvent(context,"NH_Visit_Res",map,1,1,false);
    }

    public synchronized void recordPowerOn(final Context context)
    {
    	Map<String,String> map = new HashMap<String,String>();    	
    	map.put("time", String.valueOf(System.currentTimeMillis()));
    	recordEvent(context,"poweron",map,1,1,false);
    } 

    public synchronized void recordPhoneEvent(final Context context)
    {
    	Map<String,String> map = new HashMap<String,String>();    	
    	map.put("time", String.valueOf(System.currentTimeMillis()));
    	recordEvent(context,"NH_Telephone_Event",map,1,1,false);
    } 
    
    public synchronized void recordDataPlanEvent(final Context context,String data)
    {
    	Map<String,String> map = new HashMap<String,String>();    	
    	map.put("data", data);
    	recordEvent(context,"NH_DataPlan_Event",map,1,1,false);
    } 
    
    public synchronized void recordAppDuration(final Context context,String packageName,String activityName,String appName,long start,long end,long duration)
    {
    	Map<String,String> map = new HashMap<String,String>();    	
    	map.put("packageName", packageName);
    	map.put("activityName", activityName);
    	map.put("appName", appName);
    	map.put("start", String.valueOf(start));
    	map.put("end", String.valueOf(end));    	
    	map.put("duration", String.valueOf(duration));
    	recordEvent(context,"NH_App_Duration",map,1,1,false);
    }    
    public synchronized void recordAppDuration(final Context context,String packageName,String activityName,String appName,long start,long end,long duration,long unlock)
    {
    	Map<String,String> map = new HashMap<String,String>();    	
    	map.put("packageName", packageName);
    	map.put("activityName", activityName);
    	map.put("appName", appName);
    	map.put("start", String.valueOf(start));
    	map.put("end", String.valueOf(end));    	
    	map.put("duration", String.valueOf(duration));
    	map.put("unlock", String.valueOf(unlock));
    	recordEvent(context,"NH_App_Duration",map,1,1,false);
    }
    public synchronized void recordSearchEvent(final Context context,String content,String type)
    {
    	Map<String,String> map = new HashMap<String,String>();    
    	
    	if(content== null || StringUtils.isBlank(content))
    	{
    		return;
    	}
    	map.put("content", content);
    	map.put("type", type);
    	recordEvent(context,"NH_Search_Event",map,1,1,false);
    }
    public synchronized void recordClickEvent(final Context context,String content)
    {
    	Map<String,String> map = new HashMap<String,String>();    	
    	map.put("content", content);
    	recordEvent(context,"NH_Click_Event",map,1,1,false);
    }
    
    public synchronized void recordUpgradeEvent(final Context context,String pre_version,String cur_version, String type)
    {
    	Map<String,String> map = new HashMap<String,String>();    	
    	map.put("pre_version", pre_version);
    	map.put("cur_version", cur_version);
    	if(type == null)
    	{
    		type = CountlyUtils.UPDATE_TYPE_OTA;
    	}
    	map.put("type", type);
    	recordEvent(context,"NH_Update_Event",map,1,1,false);
    }
    
    public synchronized void recordDoQuestionEvent(final Context context,String questionId, 
    		int correctFlag,long duration,String errorReason,String resFrom,int resGrade,int resSubject)
    {
    	Map<String,String> map = new HashMap<String,String>();    	
    	map.put("questionId", questionId);
    	map.put("correctFlag", String.valueOf(correctFlag));
    	map.put("duration", String.valueOf(duration));
    	if(errorReason == null||StringUtils.isEmpty(errorReason))
    	{
    		errorReason = "未知";
    	}
    	map.put("errorReason", errorReason);
    	if(resFrom == null && context != null)
    	{
    		resFrom = DeviceInfo.getAppPackageName(context);
    	}
    	map.put("resFrom", resFrom);
    	map.put("resGrade", String.valueOf(resGrade));
    	map.put("resSubject", String.valueOf(resSubject));
    	recordEvent(context,"NH_Do_Question",map,1,1,false);
    }

    public synchronized void recordPlayEvent(final Context context,
			 final String resName,final String resType,final String resId,final String resFrom,  
			 long start,long end,long duration,long total)
    {
    	Map<String,String> map = new HashMap<String,String>();    	
    	map.put("resName", resName);
    	map.put("resType", resType);
    	map.put("resId", resId);
    	map.put("resFrom", resFrom);
    	map.put("start", String.valueOf(start));
    	map.put("end", String.valueOf(end));
    	map.put("duration", String.valueOf(duration));
    	map.put("total", String.valueOf(total));
    	float finish = 1 ;
    	if(total != 0)
    	{
    		finish = duration/total;    		
    	}
    	if(finish >=1)
    	{
    		finish = 1;
    	}
    	map.put("finish", String.valueOf(finish));
    	recordEvent(context,"NH_Play_Media",map,1,1,false);
    }

    public synchronized void recordPositionEvent(final Context context,String province_name,String city_name,
		 	String distinct_name,String address,String longitude,String latitude)
    {
    	Map<String,String> map = new HashMap<String,String>();    	
    	map.put("province_name", province_name);
    	map.put("city_name", city_name);
    	map.put("distinct_name", distinct_name);
    	map.put("address", address);
    	map.put("longitude", longitude);
    	map.put("latitude", latitude);
    	recordEvent(context,"NH_Position_Event",map,1,1,false);
    }

    public synchronized void recordSearchBookEvent(final Context context,String province_name,String city_name,
		 	String distinct_name,String address,int result,int time)
    {
    	Map<String,String> map = new HashMap<String,String>();    	
    	map.put("time", String.valueOf(time));	
    	map.put("province_name", province_name);
    	map.put("city_name", city_name);
    	map.put("distinct_name", distinct_name);
    	map.put("address", address);
    	map.put("result", String.valueOf(result));
    	recordEvent(context,"NH_SearchBook_Event",map,1,1,false);
    }
    
    // for unit testing
    ConnectionQueue getConnectionQueue() { return connectionQueue_; }
    void setConnectionQueue(final ConnectionQueue connectionQueue) { connectionQueue_ = connectionQueue; }
    ExecutorService getTimerService() { return timerService_; }
    EventQueue getEventQueue() { return eventQueue_; }
    void setEventQueue(final EventQueue eventQueue) { eventQueue_ = eventQueue; }
    long getPrevSessionDurationStartTime() { return prevSessionDurationStartTime_; }
    void setPrevSessionDurationStartTime(final long prevSessionDurationStartTime) { prevSessionDurationStartTime_ = prevSessionDurationStartTime; }
    int getActivityCount() { return activityCount_; }
    boolean getDisableUpdateSessionRequests() { return disableUpdateSessionRequests_; }
}
