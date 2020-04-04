/*
 * util.cpp
 *
 *  Created on: 2012-5-16
 *      Author: lenovo
 */
#include <string.h>
#include "basictype.h"
#include "util.h"
#include "log.h"

jstring GBK2UTF8(JNIEnv * env, const char * str)
{
	if(str == NULL) {
		return NULL;
	}

    //定义java String类 strClass
    jclass strClass = env->FindClass("java/lang/String");

    //获取java String类方法String(byte[],String)的构造器,用于将本地byte[]数组转换为一个新String
    jmethodID ctorID = env->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");

    // 设置String, 保存语言类型,用于byte数组转换至String时的参
    jstring encoding = env->NewStringUTF("GBK");

    int len = strlen( str );
    jbyteArray bytes = env->NewByteArray(len);//建立byte数组
    env->SetByteArrayRegion(bytes, 0, len, (jbyte*)str);//将char* 转换为byte数组

    jstring result = (jstring)env->NewObject(strClass, ctorID, bytes, encoding);//将byte数组转换为java String

    env->DeleteLocalRef(bytes);
    env->DeleteLocalRef(encoding);
    env->DeleteLocalRef(strClass);

    return result;
}

char * UTF82GBK(JNIEnv * env, jstring str)
{
	if(str == NULL) {
		return NULL;
	}

    jclass strClass = env->FindClass("java/lang/String");

    jmethodID getBytes = env->GetMethodID(strClass, "getBytes", "(Ljava/lang/String;)[B");
    jstring encoding = env->NewStringUTF("GBK");
    jbyteArray bytes = (jbyteArray)env->CallObjectMethod(str, getBytes, encoding);

    int len = env->GetArrayLength( bytes );
    char * cstr = new char[len+1] ();
    env->GetByteArrayRegion(bytes, 0, len, (jbyte*)cstr);

    env->DeleteLocalRef(encoding);
    env->DeleteLocalRef(bytes);
    env->DeleteLocalRef(strClass);

    return cstr;
}

jbyteArray copyArray(JNIEnv * env, char * data, jint len)
{
	jbyteArray buffer = env->NewByteArray(len);
	env->SetByteArrayRegion(buffer, 0, len, (jbyte*)data);
	return buffer;
}
