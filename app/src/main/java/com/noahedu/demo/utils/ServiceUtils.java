package com.noahedu.demo.utils;

import android.content.Context;
import android.app.ActivityManager;
/**
 * © 2020 www.youxuepai.com
 *
 * @version 1.0
 * @file name：ServiceUtils$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/10/27$ 9:43$
 */
class ServiceUtils {
    public static String getRunningServicesInfo(Context context) {
        StringBuffer serviceInfo = new StringBuffer();
        /*final ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> services = activityManager.getRunningServices(100);

        Iterator<RunningServiceInfo> l = services.iterator();
        while (l.hasNext()) {
            RunningServiceInfo si = (RunningServiceInfo) l.next();
            serviceInfo.append("pid: ").append(si.pid);
            serviceInfo.append("\nprocess: "+si.process);
            serviceInfo.append("\nservice: ").append(si.service);
            serviceInfo.append("\ncrashCount: ").append(si.crashCount);
            serviceInfo.append("\nclientCount: ").append(si.clientCount);
            serviceInfo.append("\nactiveSince: ").append(si.activeSince);
            serviceInfo.append("\nlastActivityTime: ").append(si.lastActivityTime);
            serviceInfo.append(";");
        }*/
        return serviceInfo.toString();
    }
}
