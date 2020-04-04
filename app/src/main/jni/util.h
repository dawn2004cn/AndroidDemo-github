/*
 * util.h
 *
 *  Created on: 2012-5-16
 *      Author: lenovo
 */

#ifndef UTIL_H_
#define UTIL_H_
#include <jni.h>

jstring GBK2UTF8(JNIEnv * env, const char * str);

char * UTF82GBK(JNIEnv * env, jstring str);

jbyteArray copyArray(JNIEnv * env, char * data, jint len);
#endif /* UTIL_H_ */
