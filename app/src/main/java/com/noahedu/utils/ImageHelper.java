/**  
 * Copyright (c) 2013 by Logan.
 *  
 * 爱分享-微博客户端，是一款运行在android手机上的开源应用，代码和文档已托管在GitHub上，欢迎爱好者加入
 * 1.授权认证：Oauth2.0认证流程
 * 2.服务器访问操作流程
 * 3.新浪微博SDK和腾讯微博SDK
 * 4.HMAC加密算法
 * 5.SQLite数据库相关操作
 * 6.字符串处理，表情识别
 * 7.JSON解析，XML解析：超链接解析，时间解析等
 * 8.Android UI：样式文件，布局
 * 9.异步加载图片，异步处理数据，多线程  
 * 10.第三方开源框架和插件
 *    
 */
package com.noahedu.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * 图片处理类
 * @author Logan <a href="https://github.com/Logan676/JustSharePro"/>
 *  
 * @version 1.0
 *  
 */
public class ImageHelper
{
  public static Bitmap getPicFromBytes(byte[] paramArrayOfByte, BitmapFactory.Options paramOptions)
  {
    Bitmap localBitmap = null;
    if (paramArrayOfByte != null)
      if (paramOptions != null)
        localBitmap = BitmapFactory.decodeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length, paramOptions);
    while (true)
    {
      return localBitmap;
//      localBitmap = BitmapFactory.decodeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length);
//      continue;
//      localBitmap = null;
    }
  }

  public static byte[] readStream(InputStream paramInputStream)
    throws Exception
  {
    byte[] arrayOfByte1 = new byte[1024];
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    while (true)
    {
      int i = paramInputStream.read(arrayOfByte1);
      if (i == -1)
      {
        byte[] arrayOfByte2 = localByteArrayOutputStream.toByteArray();
        localByteArrayOutputStream.close();
        paramInputStream.close();
        return arrayOfByte2;
      }
      localByteArrayOutputStream.write(arrayOfByte1, 0, i);
    }
  }
}
