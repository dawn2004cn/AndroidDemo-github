package com.count.android.api;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
 
public class IpGetter_manager {
	public final static String PREF_IP_KEY = "ip_address";
	public final static String PREF_CITY_KEY = "city_address";
	public final static String PREF_SSID_KEY = "net_ssid";
	public final static String PREF_NMAC_KEY = "net_mac";
	public final static String PREF_LMAC_KEY = "local_mac";
	public final static String PREFS_NAME = "ip_prefs";
	public final static String TAG = "IpGetter";
	private final static boolean LOG = true; //Display or not debug message
    private static final int CONNECT_TIMEOUT_IN_MILLISECONDS = 30000;
    private static final int READ_TIMEOUT_IN_MILLISECONDS = 30000;
    
    
    private static final String IP138_URL = "http://1212.ip138.com/ic.asp";
    private static final String IP_NOAH_URL = "http://portal.hd.noahedu.com/hadRecord/data/ip";


	private final SharedPreferences mPreferences; //Preferences to store the ip
//	private final Context mContext;   
	private final WeakReference<Context> mContext;  
	private static String ip = null;
	private static String city = null;
	private static String localMac = null;
	private static String netMac = null;
	private static String netSSID = null;
	private static boolean mInitialized = false; 
	
	public static IpGetter_manager instance = null;
	
    public static IpGetter_manager GetInstance(Context context) {
    	if(instance == null)
    	{
    		synchronized (IpGetter_manager.class)
    		{
    			if(instance == null)
    			{
    	    		instance =  new IpGetter_manager(context);
    			}
    		}
    	}
    
        return instance;
    }

	private IpGetter_manager(Context context)
	{
		mPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		//mContext = context;
		mContext = new WeakReference<Context>(context); 
	}
	
	/**
	 * The Method the call at the init of your app
	 * @param context	you current context
	 */
	public static void sync(Context context) {
		//Initialise the Manager
		IpGetter_manager manager = GetInstance(context);
		
		//Try to get the openudid from local preferences
		ip = manager.mPreferences.getString(PREF_IP_KEY, null);
		city = manager.mPreferences.getString(PREF_CITY_KEY, null);
		netSSID = manager.mPreferences.getString(PREF_SSID_KEY, null);
		netMac = manager.mPreferences.getString(PREF_NMAC_KEY, null);
		localMac = manager.mPreferences.getString(PREF_LMAC_KEY, null);
		if (ip == null) //Not found
		{
			//Get the list of all OpenUDID services available (including itself)
			getIpInfo(manager.mContext.get());
		
		} else {//Got it, you can now call getOpenUDID()
			if (LOG) Log.d(TAG, "OpenIp: " + ip);
			getIpInfo(context);
		}
	}	

	private void storeIPAddress() {
    	final Editor e = mPreferences.edit();
		e.putString(PREF_IP_KEY, ip);
		e.putString(PREF_CITY_KEY, city);
		e.putString(PREF_SSID_KEY, netSSID);
		e.putString(PREF_NMAC_KEY, netMac);
		e.putString(PREF_LMAC_KEY, localMac);
		e.commit();
	}
	/**
	 * The Method to call to get OpenUDID
	 * @return the ip
	 */
	public static String getOpenIpAddress() {
		if (!mInitialized) Log.v("OpenIp", "Initialisation isn't done");
		return ip;
	}

	/**
	 * The Method to call to get OpenUDID
	 * @return the city
	 */
	public static String getOpenCity() {
		if (!mInitialized) Log.v("OpenIp", "Initialisation isn't done");
		return city;
	}

	/**
	 * The Method to call to get OpenUDID
	 * @return the city
	 */
	public static String getNetSSID() {
		if (!mInitialized) Log.v("OpenIp", "Initialisation isn't done");
		return netSSID;
	}
	/**
	 * The Method to call to get OpenUDID
	 * @return the city
	 */
	public static String getNetMac() {
		if (!mInitialized) Log.v("OpenIp", "Initialisation isn't done");
		return netMac;
	}
	/**
	 * The Method to call to get OpenUDID
	 * @return the city
	 */
	public static String getLocalMac() {
		if (!mInitialized) Log.v("OpenIp", "Initialisation isn't done");
		return localMac;
	}
	/**
	 * The Method to call to get OpenUDID
	 * @return the OpenUDID
	 */
	public static boolean isInitialized() {
		return mInitialized;
	}
	public static HttpURLConnection urlConnectionForEventData(final String eventData) throws IOException {
	        final String urlStr = DeviceInfo.decodeSeverUrl() + "/device?" + eventData;
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
	        return conn;
	    }
	public static void uploadDevice(Context ctx)
	{
		String eventData = DeviceInfo.getDeviceString(ctx);
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

            //{"msgCode":301,"message":"Success","field":null,"id":null}
            // HTTP response code was good, check response JSON contains {"result":"Success"}
            if (success) {
                final JSONObject responseDict = new JSONObject(responseData.toString("UTF-8"));
                //success = responseDict.optString("message").equalsIgnoreCase("Success");
                success = (responseDict.optInt("msgCode") == 301) || (responseDict.optInt("msgCode") == 401);
                if (!success && Countly.sharedInstance().isLoggingEnabled()) {
                    Log.w(Countly.TAG, "Response from Countly server did not report success, it was: " + responseData.toString("UTF-8"));
                }
            }

            if (success) {
                if (Countly.sharedInstance().isLoggingEnabled()) {
                    Log.d(Countly.TAG, "ok ->" + eventData+"responseData->>>>"+responseData);
                }
            }
            else {
                // warning was logged above, stop processing, let next tick take care of retrying
           
            }
        }
        catch (Exception e) {
            if (Countly.sharedInstance().isLoggingEnabled()) {
                Log.w(Countly.TAG, "Got exception while trying to submit event data: " + eventData, e);
            }
            // if exception occurred, stop processing, let next tick take care of retrying
           
        }
        finally {
            // free connection resources
            if (responseStream != null) {
                try { responseStream.close(); } catch (IOException ignored) {}
            }
            if (conn != null && conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).disconnect();
            }
        }
    
	}
	/**
     * 取出ip138html源码中ip地址部分，即为我们要的结果(不规则字符串)
     * 
     * @return
     * @throws IOException
     */
	public static void getIpInfo(final Context ctx)
	{
    	new Thread()
    	{
    		public void run()
    		{
    			//上传device info 信息 只上传一次
    			uploadDevice(ctx);    
    			WifiManager mWifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);

    			if (mWifi.isWifiEnabled()) {
    				WifiInfo wifiInfo = mWifi.getConnectionInfo();
    				netSSID = wifiInfo.getSSID(); //获取被连接网络的名称
    				netMac =  wifiInfo.getBSSID(); //获取被连接网络的mac地址
    				localMac = wifiInfo.getMacAddress();// 获得本机的MAC地址  
    			}
    			boolean sync = true;
    			//获取ip地址信息
    			String info ="";
        		/*try {
        			info = httpStringGet(IP138_URL, "gb2312");
        		} catch (Exception e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}           
        		if(info.length() > 10)
        		{
            		ArrayList<String> addressarray=getIpAddresses(info);
            		if(addressarray.size() > 0)
            		{
            			if((ip == null)|| (ip != null && !ip.equalsIgnoreCase(addressarray.get(0))))
            			{
            				ip = addressarray.get(0);
            				sync = false;
            				mInitialized = false;
            			}
            			else
            			{
            				mInitialized = true;
            			}
            		}
                    if (Countly.sharedInstance().isLoggingEnabled())
                    {
                    	Log.i(Countly.TAG,"ip规范化结果："+ip);        
                    }  	
                     addressarray = getAddresses(info); 
                    if(addressarray.size() > 0)
            		{
                    	String temp = addressarray.get(0);
            			int idx = temp.indexOf(" ");
            			city = temp.substring(0,idx);
            		}
                    if (Countly.sharedInstance().isLoggingEnabled())
                    {                            	
                    	Log.i(Countly.TAG,"city规范化结果："+city);   
                    }                      
        		}
        		else 
        		{
                    if (Countly.sharedInstance().isLoggingEnabled())
                    {                            	
                    	Log.i(Countly.TAG,"未获取ip地址和位置信息："+"\n");  
                    } 
        			ip = null;
        		}
        		*/
        		if(ip == null)
                {
            		try {
            			info = httpStringGet(IP_NOAH_URL,"utf-8");
            		} catch (Exception e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}   
            		 boolean success = true;
                    JSONObject responseDict = null;
					try {
						responseDict = new JSONObject(info);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(responseDict !=null)
					{
                        success = (responseDict.optInt("msgCode") == 301);
                        try {
							JSONObject obj = responseDict.getJSONObject("data");

	                        city = obj.optString("address");
	                        ip = obj.optString("IP");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        if (!success && Countly.sharedInstance().isLoggingEnabled()) {
                            Log.w(Countly.TAG, "Response from Countly server did not report success, it was: " + info);
                            
                        }
					}
                }

    			if(!isInitialized())
    			{
    				GetInstance(ctx).storeIPAddress();
    			}
    			mInitialized = true;            
    		}
    	}.start();		    	
	}  
    /**
     * 从不规则字符串中(getInfo()的结果),抽取出地址信息数组
     * 
     * @param info
     */
    public static ArrayList<String> getAddresses(String info) {
        ArrayList<String> addressarray=new ArrayList<String>();
        String regex = "来自：(.*?)</center>";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(info);
        while(matcher.find())
        {
            addressarray.add(matcher.group(1));
        }
        return addressarray;
    }
    /**
     * 从不规则字符串中(html的结果),抽取出ip地址和city地址信息数组
     * 
     * @param info
     */
    public static ArrayList<String> getIpAddresses(String info)
    {
    	 ArrayList<String> addressarray=new ArrayList<String>();
         String regex = "(1?\\d{1,2}|2[1234]\\d|25[12345])\\.(1?\\d{1,2}|2[1234]\\d|25[12345])\\.(1?\\d{1,2}|2[1234]\\d|25[12345])\\.(1?\\d{1,2}|2[1234]\\d|25[12345])";
         Pattern p = Pattern.compile(regex);
         Matcher matcher = p.matcher(info);
         while(matcher.find())
         {
             addressarray.add(matcher.group(0));
         }
         return addressarray;    	
    }
    /**
     * 抓取某个网页的源代码
     * 
     * @param url
     *            要抓取网页的地址
     * @param enc
     *            网页所使用的编码 如"utf-8","gbk"
     * @return
     * @throws Exception
     */    
    public static String httpStringGet(String url, String enc) throws Exception {
		// This method for HttpConnection
		String page = "";
		BufferedReader bufferedReader = null;
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setConnectTimeout(3000);
			connection.setReadTimeout(5000);
			connection.setRequestProperty("User-Agent", "android");
			connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
//			int responseCode = connection.getResponseCode();
			connection.connect();
			bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),
					enc));

			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";

			String NL = System.getProperty("line.separator");
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line + NL);
			}
			bufferedReader.close();
			page = stringBuffer.toString();
			return page;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					Log.d("httpStringGet", e.toString());
				}
			}
		}
	}
    
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     * @throws Exception 
     */
    public static String httpGet(String url) {
        String result = "";
        boolean success = true;
        BufferedReader in = null;
        HttpURLConnection connection = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            connection = (HttpURLConnection)realUrl.openConnection();
            // 设置通用的请求属性
            connection.setConnectTimeout(CONNECT_TIMEOUT_IN_MILLISECONDS);
            connection.setReadTimeout(READ_TIMEOUT_IN_MILLISECONDS);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(false);       
            connection.setRequestProperty("contentType", "UTF-8");  
            connection.setRequestMethod("GET");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }           
            
            //boolean success = true;
            if (connection instanceof HttpURLConnection) {
                final HttpURLConnection httpConn = (HttpURLConnection) connection;
                final int responseCode = httpConn.getResponseCode();
                success = responseCode >= 200 && responseCode < 300;
                if (!success) {
                    System.out.println("发送GET请求出现异常！"+url);
                }
                else
                {
                    System.out.println("发送GET请求OK！"+result+url);
                }
            }
            
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e+"\n url=:"+url);
            success = false;
            e.printStackTrace();
            //throw new Exception(e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (connection != null && connection instanceof HttpURLConnection) {
                    ((HttpURLConnection)connection).disconnect();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;        

    }
}