/******************************************************************
* 版权所有 (C) 诺亚舟公司
* 文件名称： videodecryptkey.h
* 文件标识： 视屏文件解密密钥
* 文件内容： 用于得到视屏文件解密密钥
* 当前版本： V1.2
*------------------------------------------------------------------
*  版 本 		作 者		日 		期		备 注
*------------------------------------------------------------------
*  V1.0			蔡成攀		2009.10.19		初版
*  V1.1			蔡成攀		2009.10.27		修改加密后视频文件头的长度128->2048
*  V1.2			蔡成攀		2009.12.2			增加GetVideoFileStartOffset()函数
*  V1.3			周立枚		2010.06.05			增加GetDecryptKey_()函数, 把FileKey同32K改为64k
******************************************************************/

#ifndef _VIDEODECRYPTKEY_H_
#define _VIDEODECRYPTKEY_H_

#ifdef __cplusplus
extern "C"{
#endif


/***  General-Purpose Data Types *******************************************/
typedef unsigned long 	ULINT;/* unsigned 32-bit integer */
typedef int             INT;    /* Signed integer (bit width of processor) */
typedef unsigned int    UINT;   /* Unsigned integer (bit width of processor) */
typedef signed char		CHAR;      /* signed 8-bit integer */
typedef long            LINT;      /* signed 32-bit integer */
typedef unsigned char   UCHAR;     /* unsigned 8-bit integer */



/***********************************************************
*功能:得到视频文件的长度
*输入:1.视频文件所在的文件路径（含文件名）;2.视频文件所在的文件偏移
*输出:成功:视频文件的长度值;失败:0
*作者:
*日期:2009/09/16
*备注:
************************************************************/
ULINT  GetVideoFileLength(CHAR * filename,ULINT offset);

/***********************************************************
*功能:得到视频文件开始的偏移
*输入:1.视频文件所在的文件路径（含文件名）;2.视频文件所在的文件偏移
*输出:成功:偏移值;失败:0
*作者:
*日期:2009/09/16
*备注:
************************************************************/
ULINT  GetVideoFileStartOffset(CHAR * filename,ULINT offset);



/***********************************************************
*功能:得到视频文件解密的密钥
*输入:1.视频文件所在的文件路径（含文件名）;2.视频文件所在的文件偏移
*输出:正常:视频文件解密的密钥 & 1;异常:-1
*作者:
*日期:2009/09/16
*备注:key的长度为32K字节
************************************************************/
INT GetDecryptKey(CHAR * filename,ULINT offset,CHAR * key) ;



/***********************************************************
*功能:得到视频文件解密的密钥
*输入:1.视频文件所在的文件路径（含文件名）;2.视频文件所在的文件偏移;3.指向密钥存放的指针
*输出:成功:视频文件解密的密钥 返回值为0;失败:返回值为-1,此时的密钥不可用
*作者:
*日期:2010/06/05
*备注:key的长度为64Kbyte
************************************************************/
INT GetDecryptKey_(CHAR * filename,ULINT offset,UCHAR * key);

#ifdef __cplusplus
}
#endif

#endif //#ifndef _VIDEODECRYPTKEY_H_
