/*
 * log.h
 *
 *  Created on: 2012-4-26
 *      Author: lenovo
 */

#ifndef LOG_H_
#define LOG_H_

#define SYNC_DEBUG
#ifndef ANDROID
#define ANDROID
#endif

#ifdef ANDROID
	#include <android/log.h>
	#define STR(s) #s
	#define CONCAT(a, b, c) STR(a##b##c)
	#define _CONCAT(a, b, c) CONCAT(a, b, c)
	#define TAG "PH_JNI"
	#define LOGI(fmt, arg...)	__android_log_print(ANDROID_LOG_INFO, TAG, fmt, ##arg)
	#ifdef SYNC_DEBUG
		#define LOGD(fmt, arg...)	__android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##arg)
	#else
		#define LOGD(fmt, arg...)
	#endif
	#define LOGW(fmt, arg...)	__android_log_print(ANDROID_LOG_WARN, TAG, fmt, ##arg)
	#define LOGE(fmt, arg...)	__android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##arg)
	#define OOM(len)			__android_log_print(ANDROID_LOG_ERROR, TAG, "Out of memory! %s,%d MALLOC:0x%08x\n", __FILE__, __LINE__, len)
#else
	#define LOGI(fmt, arg...)	printf("INFO: %s,%d\t", __FILE__, __LINE__);printf(fmt, ##arg)
	#define LOGD(fmt, arg...)	printf("DEBUG: %s,%s,%d\t", __FILE__, __FUNCTION__, __LINE__);printf(fmt, ##arg)
	#define LOGW(fmt, arg...)	printf("WARNNING: %s,%d\t", __FILE__, __LINE__);printf(fmt, ##arg)
	#define LOGE(fmt, arg...)	printf("ERROR: %s,%d\t", __FILE__, __LINE__);printf(fmt, ##arg)
	#define OOM(len)			printf("ERROR: %s,%d\t", __FILE__, __LINE__);printf("Out of memory! MALLOC:0x%08x\n", len)
#endif


#endif /* LOG_H_ */
