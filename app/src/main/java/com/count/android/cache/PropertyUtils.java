package com.count.android.cache;

import java.lang.reflect.Method;

/**
 * © 2020 www.youxuepai.com
 *
 * @version 1.0
 * @file name：PropertyUtils$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2021/1/21$ 18:40$
 */
public class PropertyUtils {
    public static long getLong(final String key, final long def) {
        long  value = def;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method method = c.getMethod("getLong",String.class,long.class);
            value = ((Long)method.invoke(c,key,def)).longValue();
            return value;
        } catch (Exception e) {
            return def;
        }finally {
            return value;
        }
    }

    public static long setLong(final String key, final long def) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method method = c.getMethod("setLong", String.class, long.class);
            method.invoke(c, key, def );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getString(final String key, final String def) {
        String value = def;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method method = c.getMethod("get", String.class, String.class);
            value = (String)(method.invoke(c, key, "unknown" ));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return value;
        }
    }

    public static long setString(final String key, final String def) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method method = c.getMethod("set", String.class, String.class);
            method.invoke(c, key, def );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getInt(final String key, final int def) {
        int value = def;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method method = c.getMethod("getInt", String.class, int.class);
            value = ((Integer)method.invoke(c, key, def )).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return value;
        }
    }
    public static long setInt(final String key, final int def) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method method = c.getMethod("setInt", String.class, int.class);
            method.invoke(c, key, def );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean getBoolean(final String key, final boolean def) {
        boolean value = def;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("getBoolean", String.class, boolean.class);
            value = ((Boolean)get.invoke(c, key, def )).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return value;
        }
    }
    public static long setBoolean(final String key, final boolean def) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method method = c.getMethod("setBoolean", String.class, boolean.class);
            method.invoke(c, key, def );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
