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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONException;
import org.json.JSONObject;

import com.count.android.cache.utils.StringConverter;
import com.count.android.cache.utils.TwoLevelLruCache;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;


/**
 * This class provides a persistence layer for the local event & connection queues.
 *
 * The "read" methods in this class are not synchronized, because the underlying data store
 * provides thread-safe reads.  The "write" methods in this class are synchronized, because
 * 1) they often read a list of items, modify the list, and then commit it back to the underlying
 * data store, and 2) while the Countly singleton is synchronized to ensure only a single writer
 * at a time from the public API side, the internal implementation has a background thread that
 * submits data to a Countly server, and it writes to this store as well.
 *
 * NOTE: This class is only public to facilitate unit testing, because
 *       of this bug in dexmaker: https://code.google.com/p/dexmaker/issues/detail?id=34
 */
public class CountlyStore {
    private static final String PREFERENCES = "COUNTLY_STORE";
    private static final String DELIMITER = "===";
    private static final String CONNECTIONS_PREFERENCE = "CONNECTIONS";
    private static final String EVENTS_PREFERENCE = "EVENTS";
    /**
     * Determines how many custom events can be queued locally before
     * an attempt is made to submit them to a Count.ly server.
     */
    private static final int CONNECT_QUEUE_SIZE_THRESHOLD = 1000;
    private static final int EVENTS_QUEUE_SIZE_THRESHOLD = 100;
	private static final int DISK_LRU_CACHE_SIZE   = 5 * 1024 * 1024;
	private static final int MEM_LRU_CACHE_SIZE   =   512 * 1024 ; //512K
	
	private static final int RECORD_CONNECTION_SIZE = 8 * 1024;
	
    private static final String LOCATION_PREFERENCE = "LOCATION";
    

    private static final String FILENAME = "CounltyStore";
	

    private final SharedPreferences preferences_;
    
//    private DiskLruCache  diskcache_;
    
//    private LruCache<String, String> memcache_; 
    
    
    private TwoLevelLruCache<String> cache_;
    private StringConverter converter;
    
    private boolean diskcreateFlag = false;
    
    private final List<String> connectionQueue = new ArrayList<String>(CONNECT_QUEUE_SIZE_THRESHOLD);
 
    private ExecutorService executor_;
    
    private Future<?> connectionProcessorFuture_;  

    // add connection thread
  	public final Runnable addConnectionRunnable = new Runnable() {
  		public void run() {    			
  			addConnectionInThread();  			
  		}
  	};	
	  /**
     * Ensures that an executor has been created for ConnectionProcessor instances to be submitted to.
     */
    void ensureExecutor() {
        if (executor_ == null) {
            executor_ = Executors.newSingleThreadExecutor();
        }
    }

    /**
     * Starts ConnectionProcessor instances running in the background to
     * process the local connection queue data.
     * Does nothing if there is connection queue data or if a ConnectionProcessor
     * is already running.
     */
    void tick() {
        if (!connectionQueue.isEmpty() && (connectionProcessorFuture_ == null || connectionProcessorFuture_.isDone())) {
            ensureExecutor();
            connectionProcessorFuture_ = executor_.submit(addConnectionRunnable);
        }
    }
    /**
     * Constructs a CountlyStore object.
     * @param context used to retrieve storage meta data, must not be null.
     * @throws IllegalArgumentException if context is null
     */
    CountlyStore(final Context context) {
        if (context == null) {
            throw new IllegalArgumentException("must provide valid context");
        }
        preferences_ = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
             
        //in thread create diskcache
    	new Thread(new Runnable() {

			public void run() {				
	            createDiskCache(context);    
			}
		}).start();		
    }    
    /**
     * monkey too much time then load disk cache need 10s */
    void createDiskCache(final Context context)
    {
    	try {
			// get diskcache file dir
			File cacheDir = getDiskCacheDir(context, "countly");
			if (cacheDir != null && !cacheDir.exists()) {
				cacheDir.mkdirs();				
			}
			
			if(cacheDir != null)
			{
				// disk cache create
				if(Countly.sharedInstance().isLoggingEnabled())
		        {
		        	Log.v(Countly.TAG, "diskcache_ is create start");
		        }
//				diskcache_ = DiskLruCache
//						.open(cacheDir, Countly.COUNTLY_SDK_VERSION_CODE, 1, DISK_LRU_CACHE_SIZE);	
				converter = new StringConverter();
		        cache_ = new TwoLevelLruCache<String>(cacheDir, Countly.COUNTLY_SDK_VERSION_CODE,
		        		MEM_LRU_CACHE_SIZE, DISK_LRU_CACHE_SIZE, converter);
				diskcreateFlag = true;
			}	
			else
			{
				diskcreateFlag = false;
				//diskcache_ = null;
			}
			
			if(Countly.sharedInstance().isLoggingEnabled())
	        {
	        	Log.v(Countly.TAG, "diskcache_ is create end");
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
	 * get diskcache file dir
	 */
	public File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		File file = null;
		
		if(Countly.sharedInstance().isStorageEnabled())
		{
			file = context.getCacheDir();
		}
		else
		{		
			file = context.getCacheDir();
		}
		if(file == null)
		{
			file = context.getCacheDir();
		}
		if(file != null)
		{
			cachePath = file.getPath();
			
			return new File(cachePath + File.separator + uniqueName);
		}
		return null;
	}
	
    /**
     * Returns an unsorted array of the current stored connections.
     */
    public String[] connections() {
        final String joinedConnStr = preferences_.getString(CONNECTIONS_PREFERENCE, "");
        return joinedConnStr.length() == 0 ? new String[0] : joinedConnStr.split(DELIMITER);
    }

    /**
     * Returns an unsorted array of the current stored event JSON strings.
     */
    public String[] events() {
        final String joinedEventsStr = preferences_.getString(EVENTS_PREFERENCE, "");
        return joinedEventsStr.length() == 0 ? new String[0] : joinedEventsStr.split(DELIMITER);
    }

    /**
     * Returns a list of the current stored events, sorted by timestamp from oldest to newest.
     */
    public List<Event> eventsList() {
        String[] array = events();
         	
    	//if connect bigger Threshold clear the old ;
    	if(array.length >  EVENTS_QUEUE_SIZE_THRESHOLD)
    	{
        	preferences_.edit().remove(CONNECTIONS_PREFERENCE).commit();

            if(Countly.sharedInstance().isLoggingEnabled())
            {
            	Log.v(Countly.TAG, "events exceed Threshold delete old connections_prefence");
            }
            array = events();
    	}
    	if(Countly.sharedInstance().isLoggingEnabled())
        {
        	Log.v(Countly.TAG, "events total sum is " + array.length);
        }
        final List<Event> events = new ArrayList<Event>(array.length);
        for (String s : array) {
            try {
                final Event event = Event.fromJSON(new JSONObject(s));
                if (event != null) {
                    events.add(event);
                }
            } catch (JSONException ignored) {
                // should not happen since JSONObject is being constructed from previously stringified JSONObject
                // events -> json objects -> json strings -> storage -> json strings -> here
            }
        }
        // order the events from least to most recent
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(final Event e1, final Event e2) {
                return (int)(e1.timestamp - e2.timestamp);
            }
        });
        return events;
    }

    /**
     * Returns true if no connections are current stored, false otherwise.
     */
    public boolean isEmptyConnections() {
        return preferences_.getString(CONNECTIONS_PREFERENCE, "").length() == 0;
    }

    /**
     * Adds a connection to the local store.
     * @param str the connection to be added, ignored if null or empty
     */
    public synchronized void addConnection(final String str) {
        if (str != null && str.length() > 0) {
        	//put it in thread
        	connectionQueue.add(str);  
        	tick();
        }        
    }    
    
    void addConnectionInThread()
    {		
    	//wait diskcache load complete
    	if(!diskcreateFlag)
    	{
    		return;
    	}
    	final List<String> connections = new ArrayList<String>(Arrays.asList(connections()));
    	if(Countly.sharedInstance().isLoggingEnabled())
        {
        	Log.v(Countly.TAG, "connect total sum is " + connections.size());
        }
    	//if connect bigger Threshold delete the first one;
        if(connections.size() >=  CONNECT_QUEUE_SIZE_THRESHOLD)
        {
        	String strTemp = connections.get(0);
        	connections.remove(0);
        	removeCache(strTemp);
        }
    	
        if(!connectionQueue.isEmpty())
        {
            String str = connectionQueue.remove(0);
            String hashkey = hashKeyForDisk(str);
            connections.add(hashkey);
            preferences_.edit().putString(CONNECTIONS_PREFERENCE, join(connections, DELIMITER)).commit();

        	if(Countly.sharedInstance().isLoggingEnabled())
        	{
            	Log.v(Countly.TAG, "key: " + str + "\r hashkey:"+hashkey);        		
        	}
            //add cache
            addCache(hashkey,str);
        }
    }
    
    synchronized void addCache(final String hashkey,final String str)
    {
    	cache_.put(hashkey, str);
//    	try{
//			//
//        	if(diskcache_ != null)
//        	{
//    			DiskLruCache.Editor editor= diskcache_.edit(hashkey);
//    			if (editor != null) {
//    				OutputStream outputStream = editor.newOutputStream(0);
//    				if (writeToStream(str, outputStream)) {
//    					editor.commit();
//    				} else {
//    					editor.abort();
//    				}
//    			}            		
//        	}
//        	else
//        	{
//        		if(Countly.sharedInstance().isLoggingEnabled())
//        		{
//        			Log.v(Countly.TAG, "add cache fail" + str);
//        		}
//        	}
//        	
//        } catch (IOException e) {
//			e.printStackTrace();
//		} 
    }

    /**
     * get a connection from the local store.
     * @param str the connection to be removed, ignored if null or empty,
     *            or if a matching connection cannot be found
     */
    public synchronized String getConnection(final String hashkey)
    {
		String key = null;
		key = cache_.get(hashkey);
//    	if (hashkey != null && hashkey.length() > 0) {          
//
//            BufferedInputStream in = null;
//			Snapshot snapShot = null;
//            try{
//            	if(diskcache_ != null)
//            	{    				
//    				snapShot = diskcache_.get(hashkey);
//        			if (snapShot != null) {
//        				InputStream is = snapShot.getInputStream(0);
//    	                in = new BufferedInputStream(is);
//    	                final ByteArrayOutputStream responseData = new ByteArrayOutputStream(RECORD_CONNECTION_SIZE); // big enough to handle success response without reallocating
//    	                int c;
//    	                while ((c = in.read()) != -1) {
//    	                    responseData.write(c);
//    	                }
//    	               key =  responseData.toString("UTF-8");    
//    	               
//    	               snapShot.close();
//        			}
//        		} 
//            	else
//            	{
//            		if(Countly.sharedInstance().isLoggingEnabled())
//    		        {
//    		        	Log.v(Countly.TAG, "diskcache_ is null");
//    		        }
//            	}
//            	return key;
//            	
//            } catch (IOException e) {
//				e.printStackTrace();
//			} 
//        }
        return key;
    }
    /**
     * Removes a connection from the local store.
     * @param str the connection to be removed, ignored if null or empty,
     *            or if a matching connection cannot be found
     */
    public synchronized void removeConnection(final String str) {
        if (str != null && str.length() > 0) {
            final List<String> connections = new ArrayList<String>(Arrays.asList(connections()));
            if (connections.remove(str)) {
                preferences_.edit().putString(CONNECTIONS_PREFERENCE, join(connections, DELIMITER)).commit();
            }       
            removeCache(str);
        }
    }
    /**
     * Removes a connection from the local cache.
     * @param str the connection to be removed, ignored if null or empty,
     *            or if a matching connection cannot be found
     */
    synchronized void removeCache(final String str)
    {
    	cache_.remove(str);
//        try{
//        	if(diskcache_ != null)
//        	{    				
//        		diskcache_.remove(str);
//    		} 
//        	
//        } catch (IOException e) {
//			e.printStackTrace();
//		}      
    	
    }
    public boolean writeToStream(String str,OutputStream outputStream) {

        BufferedOutputStream bufferedOutput = null;

        try {
            //Construct the BufferedOutputStream object
            bufferedOutput = new BufferedOutputStream(outputStream, RECORD_CONNECTION_SIZE);
            //Start writing to the output stream
            bufferedOutput.write(str.getBytes());
			//return true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the BufferedOutputStream
            try {
                if (bufferedOutput != null) {
                    bufferedOutput.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
		return true;
    }
    /**
     * Adds a custom event to the local store.
     * @param event event to be added to the local store, must not be null
     */
    void addEvent(final Event event) {
        final List<Event> events = eventsList();
        events.add(event);

        if(Countly.sharedInstance().isLoggingEnabled())
        {
        	Log.v(Countly.TAG, "addCountly event");
        }
        preferences_.edit().putString(EVENTS_PREFERENCE, joinEvents(events, DELIMITER)).commit();
    }

    /**
     * Adds a custom event to the local store.
     * @param key name of the custom event, required, must not be the empty string
     * @param segmentation segmentation values for the custom event, may be null
     * @param timestamp timestamp (seconds since 1970) in GMT when the event occurred
     * @param count count associated with the custom event, should be more than zero
     * @param sum sum associated with the custom event, if not used, pass zero.
     *            NaN and infinity values will be quietly ignored.
     */
    public synchronized void addEvent(final String key, final Map<String, String> segmentation, final long timestamp, final int count, final double sum,final boolean delay) {
        final Event event = new Event();
        event.key = key;
        event.segmentation = segmentation;
        event.timestamp = timestamp;
        event.count = count;
        event.sum = sum;
        event.delay = delay;

        addEvent(event);
    }

    /**
     * Sets location of user and sends it with next request
     */
    public void setLocation(final double lat, final double lon) {
        preferences_.edit().putString(LOCATION_PREFERENCE, lat + "," + lon).commit();
    }

    /**
     * Get location or empty string in case if no location is specified
     */
    public String getAndRemoveLocation() {
        String location = preferences_.getString(LOCATION_PREFERENCE, "");
        if (!location.equals("")) {
            preferences_.edit().remove(LOCATION_PREFERENCE).commit();
        }
        return location;
    }
    /**
     * Removes the specified events from the local store. Does nothing if the event collection
     * is null or empty.
     * @param eventsToRemove collection containing the events to remove from the local store
     */
    public synchronized void removeEvents(final Collection<Event> eventsToRemove) {
        if (eventsToRemove != null && eventsToRemove.size() > 0) {
            final List<Event> events = eventsList();
            if (events.removeAll(eventsToRemove)) {
                preferences_.edit().putString(EVENTS_PREFERENCE, joinEvents(events, DELIMITER)).commit();
            }
        }
    }

    /**
     * Converts a collection of Event objects to URL-encoded JSON to a string, with each
     * event JSON string delimited by the specified delimiter.
     * @param collection events to join into a delimited string
     * @param delimiter delimiter to use, should not be something that can be found in URL-encoded JSON string
     */
    static String joinEvents(final Collection<Event> collection, final String delimiter) {
        final List<String> strings = new ArrayList<String>();
        for (Event e : collection) {
            strings.add(e.toJSON().toString());
        }
        return join(strings, delimiter);
    }

    /**
     * Joins all the strings in the specified collection into a single string with the specified delimiter.
     */
    static String join(final Collection<String> collection, final String delimiter) {
        final StringBuilder builder = new StringBuilder();

        int i = 0;
        for (String s : collection) {
            builder.append(s);
            if (++i < collection.size()) {
                builder.append(delimiter);
            }
        }

        return builder.toString();
    }

    /**
	 * get md5 key for string
	 */
	public String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}
	
	/**
	 * fluchCache
	 */
	public void fluchCache() {
		/*if (diskcache_ != null) {
			try {
				diskcache_.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
	}

	private String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
    // for unit testing
    synchronized void clear() {
        final SharedPreferences.Editor prefsEditor = preferences_.edit();
        prefsEditor.remove(EVENTS_PREFERENCE);
        prefsEditor.remove(CONNECTIONS_PREFERENCE);
        prefsEditor.commit();
    }
}
