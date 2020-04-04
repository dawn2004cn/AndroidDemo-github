/*
 * sync_test_engine.h
 *
 *  Created on: 2012-4-19
 *      Author: lenovo
 */

#ifndef SINGLE_SYNC_WRITER_ENGINE_H_
#define SINGLE_SYNC_WRITER_ENGINE_H_
#include <jni.h>
#include "log.h"
#include "common.h"
#ifdef _WIN32
#pragma pack(1)
#endif


 /*引擎结构体定义*/
 typedef struct
 {
 	 char*  file;   // 文件名
     FILE*  fp;    // 库文件指针

     char*  byteTabBuf;        // 单字节解压表
     char*  wordTabBuf;        // 双字节解压表

     uint  mainIndex; 		// 主题索引库
     uint  itemIndex; 		// 子项索引库
     uint  questionIndex;		//习题索引库
     uint  soundOffset;	//声音索引库偏移
     uint  picOffset;	//图片索引库偏移


     uint maincount;
     unsigned char* mainTb;
 }PACKED T_SyncWriterHandler;


 typedef struct
 {
     char type;
     int len;
     char* pic;
 }PACKED T_Picture;

 typedef struct
 {
 	uint  order;		//目录序号
 	uint  addr;
 	char* title;		//主题名称
 	char * cacheFile;
 	uint  soundAddr;   //声音地址
 	uint  picAddr;      //图片地址
 	uint  itemCount;
 	uint  itemOrder;
 	T_Picture * pic;
 }PACKED T_WriterItem;

 typedef struct
 {
 	uint  stage;		//年龄段标记
 	char*  name;     //名称
 	char*  videoId;    //视频id
 	char * cacheFile;
 	uint  picAddr;   //图片地址
 	uint  questCount;  //试题个数
 	uint  questOrder;   //试题序号
 	T_Picture* pic;
 }PACKED T_Item;

 typedef struct
 {
 	int  count;		//索引个数
 	T_WriterItem* item;
 }PACKED T_Writer;


 typedef struct
 {
     int len;
     char* sound;
 }PACKED T_Sound;

#ifdef _WIN32
#pragma pack()
#endif


 /************************************************************************/
 /* 全局函数原型                                                         */
 /************************************************************************/

 /*--------------------------------------------
              人格健康教育库引擎

     Parameters:
         file 输入文件路径，同步作文库文件

     Returns:
     	成功返回TRUE，失败返回FALSE
 ----------------------------------------------*/
 DLLAPI T_SyncWriterHandler * SWT_Init(const char * file);

 /*--------------------------------------------
          关闭人格健康教育库引擎
 ----------------------------------------------*/
 DLLAPI void SWT_Close(T_SyncWriterHandler * handler);
 /*--------------------------------------------
         初始化后获取类别个数后，根据索引 获取人格健康教育库类别数据
 ----------------------------------------------*/
 DLLAPI T_WriterItem* GetWriterItem(T_SyncWriterHandler * handler, int index);

 DLLAPI BOOL SWT_ReleaseWriterItem(T_WriterItem * item);
 DLLAPI BOOL SWT_ReleaseItem(T_Item * item);
 /*--------------------------------------------
         初始化后获取类别个数后，根据当前类别目录获取子目录的数据数据
 ----------------------------------------------*/
 DLLAPI T_Item* GetChildrenItem(T_SyncWriterHandler * handler, int count,int order,int index);
 /*--------------------------------------------
         获取声音数据
  ----------------------------------------------*/
 DLLAPI T_Sound* SWT_GetSoundInfo(T_SyncWriterHandler * handler,int add,int off);

 DLLAPI BOOL SWT_ReleaseSound(T_Sound * sound);
 /*--------------------------------------------
         获取图片数据
  ----------------------------------------------*/
 DLLAPI T_Picture* SWT_GetPicInfo(T_SyncWriterHandler * handler,int add,int off);

 DLLAPI BOOL SWT_ReleasePicture(T_Picture * pic);
#endif /* SINGLE_SYNC_WRITER_ENGINE_H_ */
