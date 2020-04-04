/*
 * sync_test_engine.c
 *
 *  Created on: 2012-4-19
 *      Author: lenovo
 */
#include <stdio.h>
#include "sync_writer.h"
#include "videodecryptkey.h"
#include "util.h"
#include "log.h"

#define LIB_OFFSET 0x40

/*--------------------------------------------
         初始化人格健康教育库引擎

    Parameters:
        file 输入文件路径，人格健康教育库文件

    Returns:
    	成功返回TRUE，失败返回FALSE
----------------------------------------------*/
DLLAPI T_SyncWriterHandler * SWT_Init(const char * file)
{
	FILE*  fp  = NULL;
	int  seekResult = 0;
	uint readResult = 0;
	T_FileHeader headeraddrs;
	T_FileAddress addrs;
	T_SyncWriterHandler * handler = NULL;
	uchar	uAddress[4];

	LOGD("SWT_Init %s %d",file,__LINE__);
	memset(&headeraddrs, 0, sizeof(T_FileHeader));
	LOGD("SWT_Init %s %d",file,__LINE__);
	memset(&addrs, 0, sizeof(T_FileAddress));
	LOGD("SWT_Init %s %d",file,__LINE__);
	/* 打开文件 */
	fp = fopen(file, "rb");
	if (fp == NULL)
	{
		LOGE("File open error:%s\n", file != NULL ? file : "NULL");
		return NULL;
	}

	LOGD("SWT_Init  %d ,fileHandle:%d",__LINE__,fp);
	/* 申请内存 */
	handler = (T_SyncWriterHandler*)MALLOC(sizeof(T_SyncWriterHandler));
	LOGD("SWT_Init  %d,T_SyncWriterHandler %d",__LINE__,handler);
	if (handler == NULL)
	{
		fclose(handler->fp);
		OOM(sizeof(T_SyncWriterHandler));
		return NULL;
	}
	LOGD("SWT_Init  %d",__LINE__);
	memset(handler, 0, sizeof(T_SyncWriterHandler));

	handler->fp  = fp;

	LOGD("SWT_Init  %d,lenth:%d",__LINE__,strlen(file)+1);
	handler->file = (char *)MALLOC(strlen(file)+1);
	LOGD("SWT_Init  %d,file:%d",__LINE__,handler->file);
	if (handler->file == NULL)
	{
		fclose(handler->fp);
		OOM(strlen(file)+1);
		FREE(handler);
		return NULL;
	}

	LOGD("SWT_Init  %d",__LINE__);
	memset(handler->file, 0, strlen(file)+1);
	memcpy(handler->file, file, strlen(file));

	LOGD("SWT_Init  %d ,handler->fp :%d",__LINE__,handler->fp);
	/* 跳到文件头  */
	seekResult = fseek(handler->fp, 0, SEEK_SET);
	LOGD("SWT_Init  line %d  seek %d",__LINE__ ,seekResult);
	if (seekResult)
	{
		fclose(handler->fp);
		FREE(handler->file);
		FREE(handler);
		LOGE("File seek error!\n");
		return NULL;
	}

	LOGD("SWT_Init  %d",__LINE__);
	/* 读取文件索引数据 */
	memset(&headeraddrs, 0, sizeof(T_FileHeader));
	readResult = fread(&headeraddrs, sizeof(T_FileHeader), 1, handler->fp);
	if (readResult != 1)
	{
		fclose(handler->fp);
		FREE(handler->file);
		FREE(handler);
		LOGE("Read error: expected %d but returned %d!", 1, readResult);
		return NULL;
	}

	LOGD("SWT_Init  %d",__LINE__);
	//读取文件地址索引数据
	seekResult = fseek(handler->fp, 0x60, SEEK_SET);
	if (seekResult)
	{
		fclose(handler->fp);
		FREE(handler->file);
		FREE(handler);
		LOGE("File seek error!\n");
		return NULL;
	}
	LOGD("SWT_Init  %d",__LINE__);
	readResult = fread(&addrs,sizeof(T_FileAddress),1,handler->fp);
	if (readResult != 1)
	{
		fclose(handler->fp);
		FREE(handler->file);
		FREE(handler);
		LOGE("Read error: expected %d but returned %d!", 1, readResult);
		return NULL;
	}

	LOGD("SWT_Init  %d",__LINE__);
	/* 获取各个库的偏移地址 */
	handler->mainIndex = READ_INT_3(addrs.index_m)+LIB_OFFSET;
	handler->itemIndex = READ_INT_3(addrs.index_s)+LIB_OFFSET;
	handler->questionIndex = READ_INT_4(addrs.mainliboffset)+LIB_OFFSET;
	handler->soundOffset = READ_INT_4(addrs.childlib0)+LIB_OFFSET;
	handler->picOffset = READ_INT_4(addrs.childlib1)+LIB_OFFSET;

	LOGD("mainIndex: 0x%08x\n", handler->mainIndex);
	LOGD("itemIndex: 0x%08x\n", handler->itemIndex);
	LOGD("questionIndex: 0x%08x\n", handler->questionIndex);
	LOGD("soundOffset: 0x%08x\n", handler->soundOffset);
	LOGD("picOffset: 0x%08x\n", handler->picOffset);

	//读取主题库索引地址
	seekResult = fseek(handler->fp, handler->mainIndex, SEEK_SET);
	if (seekResult)
	{
		fclose(handler->fp);
		FREE(handler->file);
		FREE(handler);
		LOGE("File seek error!\n");
		return NULL;
	}

	readResult = fread(&uAddress,sizeof(uAddress),1,handler->fp);
	if (readResult != 1)
	{
		fclose(handler->fp);
		FREE(handler->file);
		FREE(handler);
		LOGE("Read error: expected %d but returned %d!", 1, readResult);
		return NULL;
	}
	LOGE("uAddress:0x%08x",  uAddress);
	//获取主题个数
	handler->maincount = READ_INT_4(uAddress);
	LOGE("handler->maincount:%d",  handler->maincount);
	handler->mainTb = (unsigned char*)MALLOC(handler->maincount*4);

	fread(handler->mainTb,handler->maincount*4,1,handler->fp);
	return handler;
}

/*--------------------------------------------
         关闭人格健康教育库引擎
----------------------------------------------*/
DLLAPI void SWT_Close(T_SyncWriterHandler * handler)
{
	if (handler != NULL)
	{
		if(handler->fp != NULL)
		{
			fclose(handler->fp);
			handler->fp = NULL;
		}

		if(handler->mainTb != NULL)
		{
			FREE(handler->mainTb);
		}
		FREE(handler->file);
		FREE(handler);
	}
}

//return 指针需要释放使用SWT_ReleaseWriterItem
DLLAPI T_WriterItem* GetWriterItem(T_SyncWriterHandler * handler, int index)
{
	int len = 0;
	uint addr;
	int seekResult = 0;

	uint readResult = 0;

	char * buffer = NULL;

	if(handler == NULL)
	{
		return NULL;
	}

	T_WriterItem* t_writerItem = (T_WriterItem*)MALLOC(sizeof(T_WriterItem));

	if(t_writerItem == NULL)
	{
		OOM(len);
		return NULL;
	}

	LOGD("GetWriterItem %d,line:%d,index = %d", t_writerItem,__LINE__,index);
	memset(t_writerItem,0,sizeof(T_WriterItem));
	LOGD("GetWriterItem Line: %d\r\n",__LINE__);
	t_writerItem->addr =  READ_INT_4(handler->mainTb+index*4);
	t_writerItem->order =  index;

	LOGD("GetWriterItem seek: %d\r\n",handler->mainIndex+t_writerItem->addr);

	LOGD("GetWriterItem t_writerItem->order  = %d\r\n",
			t_writerItem->order);

	seekResult = fseek(handler->fp, handler->mainIndex+t_writerItem->addr, SEEK_SET);

	LOGD("GetWriterItem Line: %d\r\n",__LINE__);
    readResult = fread(&len,2,1,handler->fp);

	LOGD("GetWriterItem Line: %d,lenth:%d\r\n",__LINE__,len);
    //读取主题数据内容
	buffer = (char*)MALLOC(len);
	if(buffer== NULL)
	{
		OOM(len);
		return NULL;
	}
	memset(buffer,0,len);

    //读取主题数据内容
    readResult = fread(buffer,len,1,handler->fp);
    if (readResult != 1)
    {
		FREE(buffer);
		return NULL;
    }

	LOGD("GetWriterItem Line: %d\r\n",__LINE__);
    char * cursor = buffer;
	if(cursor != NULL) {
		int idLen = 0;
		int left = 0;
		while(*cursor != 0) {
				if(*cursor == 0x09) {
					*cursor = 0;
					cursor++;
				    left++;
					break;
				}
				left++;
				cursor++;
		}

		t_writerItem->title = (char*)MALLOC(left+10);
		memset(t_writerItem->title,0,left+10);
		if(t_writerItem->title == NULL)
		{
			OOM(left);
			return NULL;
		}
		LOGD("GetWriterItem Line: %d,left:%d\r\n",__LINE__,left);
		memcpy(t_writerItem->title,buffer,left-1);
		LOGD("GetWriterItem Line: %d,%s,left:%d,other:%d\r\n",__LINE__,t_writerItem->title,left,len-left);
		t_writerItem->soundAddr = READ_INT_4((unsigned char*)(buffer+left+0*4));
		t_writerItem->picAddr = READ_INT_4((unsigned char*)(buffer+left+1*4));
		t_writerItem->itemCount = READ_INT_4((unsigned char*)(buffer+left+2*4));
		t_writerItem->itemOrder = READ_INT_4((unsigned char*)(buffer+left+3*4));

		//获取图片
		t_writerItem->pic = SWT_GetPicInfo(handler,handler->picOffset,t_writerItem->picAddr);

	    LOGW("GetWriterItem  is %d!,soundAddr:0x%08x,picAddr:0x%08x,itemCount:0x%08x,itemOrder:0x%08x\n", __LINE__,t_writerItem->soundAddr,
	    		t_writerItem->picAddr,t_writerItem->itemCount,t_writerItem->itemOrder);
	}
	if(buffer != NULL)
	{
		FREE(buffer);
	}

	LOGW("GetWriterItem  is %d!\n", __LINE__);
	return t_writerItem;
}


//return 指针需要释放使用SWT_ReleaseItem
DLLAPI T_Item* GetChildrenItem(T_SyncWriterHandler * handler, int count,int order,int index)
{
	int len = 0;
	uint addr;
	int seekResult = 0;

	uint readResult = 0;
	uchar	uAddress[4];
	//char* charItems = NULL;

	char * buffer = NULL;

	if(handler == NULL)
	{
		return NULL;
	}

	LOGW("GetWriterItem  is %d,handler->itemIndex:0x%08x,index=%d\n", __LINE__,handler->itemIndex,index);
	//子项个数N（4bytes） + 每一个子项数据在本库内的偏移地址（N*4bytes）,读取第几个的地址
	seekResult = fseek(handler->fp, handler->itemIndex+4+(order-1+index)*4, SEEK_SET);

    readResult = fread(&uAddress,1,4,handler->fp);

    addr = READ_INT_4(uAddress);

	LOGW("GetWriterItem  is %d,handler->itemIndex:seek0x%08x\n", __LINE__,addr);
    T_Item* item = (T_Item*)MALLOC(sizeof(T_Item));
    //for(int i =0;i<count;i++)
    {
    	//直接seek到子项的数据结构位置：链长（2bytes,不包括本身）+ 年龄段标记（1byte）+ 显示名称 + 0x09 + 视频ID + 0x09 + 图标地址（4bytes）+ 习题个数（4bytes）+ 首习题序号（4bytes）+ 0x00
	    seekResult = fseek(handler->fp, handler->itemIndex+addr, SEEK_SET);
	    len = 0;
	    //链长
        readResult = fread(&len,2,1,handler->fp);

    	LOGW("GetWriterItem  is %d,seek:0x%08x,len=%d\n", __LINE__,handler->itemIndex+addr,len);
        buffer = (char*)MALLOC(len);

        if(buffer == NULL)
        {
        	OOM(len);
        	return NULL;
        }
        memset(buffer,0,len);
        item->stage = 0;

        readResult = fread(&item->stage,1,1,handler->fp);

        readResult = fread(buffer,len-1,1,handler->fp);

    	LOGW("GetWriterItem  is %d,seek:%d,%s\n", __LINE__,item->stage,buffer);
        char * cursor = buffer;
        if(cursor != NULL) {
            int idLen = 0;
            int left = 0;
            int nameLen = 0;
            int videoLen = 0;
            while(*cursor != 0) {
                    if(*cursor == 0x09) {
                        *cursor = 0;
                        cursor++;
                        left++;
                        break;
                    }
                    left++;
                    cursor++;
            }
            nameLen = left;

        	LOGW("GetChildrenItem  is name len:%d ,line:%d\n", nameLen,__LINE__);
            item->name = (char*)MALLOC(nameLen+10);
            if(item->name == NULL)
            {
            	OOM(nameLen);
            	return NULL;
            }
            memset(item->name,0,nameLen+10);
            memcpy(item->name, buffer,nameLen-1);

        	LOGW("GetChildrenItem  is name :%s ,line:%d\n", item->name,__LINE__);
            cursor = buffer+nameLen;
            left = 0;
            while(*cursor != 0) {
                    if(*cursor == 0x09) {
                        *cursor = 0;
                        cursor++;
                        left++;
                        break;
                    }
                    left++;
                    cursor++;
            }
            videoLen = left;

        	LOGW("GetChildrenItem  is videoLen:%d,line:%d\n", videoLen,__LINE__);
            item->videoId = (char*)MALLOC(videoLen+10);

            if(item->videoId == NULL)
            {
            	OOM(videoLen);
            	return NULL;
            }
            memset(item->videoId,0,videoLen+10);
            memcpy(item->videoId, buffer+nameLen,videoLen-1);

        	LOGW("GetChildrenItem  is videoId :%s ,line:%d\n", item->videoId,__LINE__);
            item->picAddr = READ_INT_4((unsigned char*)(buffer+nameLen+videoLen+0*4));
            item->questCount = READ_INT_4((unsigned char*)(buffer+nameLen+videoLen+1*4));
            item->questOrder = READ_INT_4((unsigned char*)(buffer+nameLen+videoLen+2*4));

            item->pic = SWT_GetPicInfo(handler,handler->picOffset,item->picAddr);

	        LOGW("GetChildrenItem  is %d!,picAddr:0x%08x,questCount:0x%08x,questOrder:0x%08x\n", __LINE__,
	        		item->picAddr,item->questCount,item->questOrder);

        }
    }
    if(buffer != NULL)
    {
    	FREE(buffer);
    }

	LOGW("GetChildrenItem  is %d!\n", __LINE__);
	return item;
}
//return 指针需要释放使用SWT_ReleaseSound
DLLAPI T_Sound* SWT_GetSoundInfo(T_SyncWriterHandler * handler,int add,int off)
{
	int len = 0;
	uint addr;
	char type =0;
	int seekResult = 0;

	uint readResult = 0;

    T_Sound * sound;

	if(handler == NULL)
	{
		return NULL;
	}

	if(off == INVALID_ADDR)
	{
		return NULL;
	}
	seekResult = fseek(handler->fp, add+off, SEEK_SET);
	if (seekResult)
    {
    		return NULL;
    }

	readResult = fread(&len,2,1,handler->fp);

	sound = (T_Sound *)MALLOC(sizeof(T_Sound));

	memset(sound, 0, sizeof(T_Sound));
	if(sound != NULL) {
	    sound->len = len;
	    sound->sound = (char *)MALLOC(len);
	    memset(sound->sound,0,len);
	    readResult = fread(sound->sound,len,1,handler->fp);
	}
	return sound;
}
//return 指针需要释放使用SWT_ReleasePicture
DLLAPI T_Picture* SWT_GetPicInfo(T_SyncWriterHandler * handler,int add,int off)
{
	int len = 0;
	uint addr;
	char type =0;

	int seekResult = 0;

	uint readResult = 0;

    T_Picture * pic = NULL;

	if(handler == NULL )
	{
		return NULL;
	}
	if(off == INVALID_ADDR)
	{
		return NULL;
	}
	seekResult = fseek(handler->fp, add+off, SEEK_SET);
	if (seekResult)
    {
    	return NULL;
    }

	readResult = fread(&type,1,1,handler->fp);
	readResult = fread(&len,4,1,handler->fp);

	pic = (T_Picture *)MALLOC(sizeof(T_Picture));

	memset(pic, 0, sizeof(T_Picture));
	if(pic != NULL) {
	    pic->type = type;
	    pic->len = len;
	    pic->pic = (char *)MALLOC(len);
	    memset(pic->pic,0,len);
	    readResult = fread(pic->pic,len,1,handler->fp);
	}
	return pic;
}

DLLAPI BOOL SWT_ReleaseWriterItem(T_WriterItem * item)
{
	if(item != NULL)
	{
		FREE(item->title);
		FREE(item->cacheFile);
		SWT_ReleasePicture(item->pic);
		FREE(item);
	}
	return true;
}
DLLAPI BOOL SWT_ReleaseItem(T_Item * item)
{
	if(item != NULL)
	{
		FREE(item->name);
		FREE(item->videoId);
		FREE(item->cacheFile);
		SWT_ReleasePicture(item->pic);
		FREE(item);
	}
	return true;
}

DLLAPI BOOL SWT_ReleaseSound(T_Sound * sound)
{
	if(sound != NULL)
	{
		FREE(sound->sound);
		FREE(sound);
	}
	return true;
}


DLLAPI BOOL SWT_ReleasePicture(T_Picture * pic)
{
	if(pic != NULL)
	{
		FREE(pic->pic);
		FREE(pic);
	}
	return true;
}










