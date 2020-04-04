/*
 * utils.h
 *
 *  Created on: 2012-4-27
 *      Author: lenovo
 */

#ifndef UTILS_H_
#define UTILS_H_
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "basictype.h"
#include "log.h"

#define SL_FILE_ID_LEN 300
#define SL_FILE_VERSION_LEN 16
// 数学应用题模型
#define FILE_TYPE_YYT 	1
// 图形计算器
#define FILE_TYPE_DMS	2
// 诺亚动漫
#define FILE_TYPE_DMP	3

typedef enum
{
	PAPER_QUIZ = 0x01,	/*只有小题的试卷*/
	PAPER_EXAMINATION = 0x02,		/*包含大题的试卷*/
	PAPER_GAME = 0x03, /*游戏题*/
	PAPER_UNSUPPORT = 0xFF,	/*不支持的试卷类型*/
}E_PAPER_TYPE;

typedef enum
{
	LEVEL_WRITE,	/*会写的字*/
	LEVEL_REG,		/*会认的字*/
}E_CNCHAR_LEVEL;

typedef enum
{
	QS_CHOICE_SINGLE = 0x01,	/*单项选择题*/
	QS_CHOICE_MULTI_EXTRA = 0x02,		/*多项选择题， 顺序固定*/
	QS_CHOICE_MULTI = 0x03,		/*多项选择题，顺序不固定*/
	QS_CHOICE_FILL = 0x04,	/*判断题（选择填空题：点击选项，选项的内容自动显示到选择区）*/
	QS_CHOICE_SINGLE_VOICE = 0x05,	/*听力单选题*/
	QS_CHOICE_MULTI_VOICE = 0x06,	/*听力多选题*/
	QS_CHOICE_FILL_VOICE = 0x07,	/*听力选择填空题*/
	QS_CHOICE_CLICK = 0x08,	/*点选题（选择填空题：点击选项，选项的内容自动显示到选择区）*/

	QS_FILL_ZONE = 0x10,	/*填空题（在填空区域填字符串内容）*/
	QS_FILL_LINE = 0x11,	/*填空题（在横线上填字符串内容）*/
	QS_FILL_FRAME = 0x12,	/*田字格填空题*/
	QS_FILL_ZONE_VOICE = 0x13,	/*听力填空题（在填空区域填字符串内容）*/
	QS_FILL_LINE_VOICE = 0x14,	/*听力填空题（在横线上填字符串内容）*/

	QS_LINK_PORT = 0x20,	/*连线题（竖排）*/
	QS_LINK_LAND = 0x21,	/*连线题（横排）*/

	QS_ORDER_WORD = 0x30,	/*排序题(几个选项拉倒固定的位置排序，例如连词成句)*/
	QS_ORDER_OPTS = 0x31,	/*排序题（将选项的标记按循序点击，自动填到固定区域，例如：连句成文）*/
	QS_ORDER_OPTS_VOICE = 0x32,	/*听力排序题（听一段录音，将选项的标记按循序点击，自动填到固定区域，例如：连句成文）*/

	QS_PIC_COLOR = 0x40,	/*图片填颜色*/
	QS_PIC_STRING = 0x41,	/*图片填字符串*/
	QS_PIC_PIC = 0x42,	/*图片填图片*/
	QS_PIC_COLOR_VOICE = 0x43,	/*听力题（图片填颜色）*/
	QS_PIC_STRING_VOICE = 0x44,	/*听力题（图片填字符串）*/
	QS_PIC_PIC_VOICE = 0x45,	/*听力题（图片填图片）*/

	QS_FILL_BLANK = 0x50,	/*完型填空题*/
	QS_FILL_BLANK_VOICE = 0x51,	/*听力题（完型填空题）*/

	QS_MULTI_VOICE = 0x60,	/*听力套题（一段声音对应多道题目）*/

	QS_MULTI_READ = 0x70,	/*阅读理解套题（一篇阅读文章对应多道题目）*/

	QS_NO_STD_ANSWER = 0x80,	/*不可在题目上作答的题*/

	QS_MULTI_CONNECT = 0x90, /*连词成句套题*/
}E_QUESTION_TYPE;

typedef enum
{
	GT_R1 = 0x10,	/*规则为R1类型的游戏*/
	GT_R2 = 0x20,	/*规则为R2类型的游戏*/
	GT_R3 = 0x30,	/*规则为R3类型的游戏*/
	GT_R4 = 0x40,	/*规则为R4类型的游戏*/
	GT_R5 = 0x50,	/*规则为R5类型的游戏*/
	GT_R6 = 0x60,	/*规则为R6类型的游戏*/
	GT_MAX = 0x70,	/*不支持的游戏类型，游戏类型上限*/
}E_GAME_TYPE;

#ifdef _WIN32
#pragma pack(1)
#endif

/*文件头结构*/
 typedef struct
 {
     // 文件头数据共64 Bytes
     char fileFlag[8]; 	// 文件标识(8 bytes)
     char version[4]; // 版本号(4 Bytes).
     char size[4]; // 文件大小，不包括文件头大小(4 Bytes).
     char headerSize[2]; // 文件头大小(2 Bytes).
     char date[12]; // 文件生成的时间(12 Bytes).
     char encrypt[2]; // 加密标志(2 Bytes).
     char checksum[2]; // 不包括文件头的所有文件字节校验和(2 Bytes).
     char encode[1]; // 点阵编码体系(1 Bytes).
     char catalog[1]; // 目录的结构标记(1 Bytes).
     char catalogLang[1]; // 目录的语言标记(1 Bytes).
     char catalogContent[1]; // 目录的内容结构标记(1 Bytes).
     char name[26];	// 文件名(26 Bytes)
 }PACKED T_FileHeader;

 /*文件说明区结构*/
  typedef struct
  {
      // 文件说明数据共32 Bytes
      char fileNO; 	// 资料编号(1 bytes)
      char dictreadmebuff[28]; // 词典文件说明(28 Bytes).
      char	wordnumbuff[3];	// 词典主词条数目(3 Bytes)

      /*注意: 对偏移地址数据和文件大小数据,先低字节后高字节存放.	*/
  }PACKED T_FileInfo;

/*文件地址区结构*/
 typedef struct
 {
     // 文件地址索引数据共96 Bytes
     uchar	extracttab0[3];	// 解压表0偏移地址(3 Bytes)
     uchar	extracttab0filesize[2]; // 解压表0文件字节数(2 Bytes)
     uchar	extracttab1[3];	// 解压表1偏移地址(3 Bytes)
     uchar	extracttab1filesize[2]; // 解压表1文件字节数(2 Bytes)
     uchar	extracttab2[3];	// 解压表2偏移地址(3 Bytes)
     uchar	extracttab2filesize[2]; // 解压表2文件字节数(2 Bytes)

     uchar	reserve1[10]; // 保留(10 Bytes).

     uchar	logoPic1[3]; // 词典Logo图片1偏移地址(3 Bytes).
     uchar	logoPic2[3]; // 词典Logo图片2偏移地址(3 Bytes).
     uchar	extendaddr1[4]; // 扩充地址

     uchar	index_m[3]; // 主索引表(32分索引)偏移地址(3 Bytes).
     uchar	index_s[3]; // 次索引表偏移地址(3 Bytes).
     uchar	index_z[3]; // 再索引表偏移地址(3 Bytes).

     uchar	reserve2[12];   // 保留(12 Bytes)

     uchar	mainliboffset[4]; // 主库文件偏移地址(4 Bytes).
     uchar	childlib0[4]; // 子库0文件偏移地址(4 Bytes).
     uchar	childlib1[4]; // 子库1文件偏移地址(4 Bytes).
     uchar	childlib2[4]; // 子库2文件偏移地址(4 Bytes).
     uchar	childlib3[4]; // 子库3文件偏移地址(4 Bytes).
     uchar	childlib4[4]; // 子库4文件偏移地址(4 Bytes).
     uchar	childlib5[4]; // 子库5文件偏移地址(4 Bytes).
     uchar	childlib6[4]; // 子库6文件偏移地址(4 Bytes).
     uchar	childlib7[4]; // 子库7文件偏移地址(4 Bytes).
     uchar	childlib8[4]; // 子库8文件偏移地址(4 Bytes).

     /*注意: 对偏移地址数据和文件大小数据,先低字节后高字节存放.	*/
 }PACKED T_FileAddress;

 /* 小学同步额外文件头结构 */
 typedef struct
 {
 	// 额外头文件数据共512 Bytes
     char  type[16];   /* 库类型 */
     char  id[SL_FILE_ID_LEN];  /* 库id */
     char  version[SL_FILE_VERSION_LEN];  /* 库版本 */
     char  reserved[180]; /*保留字节*/
 }PACKED T_ExtraFileHeader;

/* 试卷结构 */
typedef struct
{
	E_PAPER_TYPE type; // 试卷类型
	uint time; // 时间限制
	uint score; // 总分值

	// 大题数据和小题数据不能同时存在。
	struct T_QuizStruct * quiz; // 小题数据
	struct T_ExamStruct * exam; // 大题数据
	struct T_GameTypeStruct * game; // 游戏类型及对应题目信息
}PACKED T_Paper;

/* 小题结构 */
typedef struct T_QuizStruct
{
	uint count; // 题目总数
	uchar* types; // 各题题型
	uint* addrs; // 各题内容地址
}PACKED T_Quiz;

/* 大题结构 */
typedef struct T_ExamStruct
{
	uint count; // 题目总数
	char** intro; // 各题题目说明
	T_Quiz** contents; // 各题内容
}PACKED T_Exam;

/* 试卷对应游戏类型结构 */
typedef struct T_GameTypeStruct
{
	uint typeCount; // 类型总数
	uchar *types; // 各类型标记
	char **names; // 各类型名称
	uint *counts; // 各类型下题目数目
	uint **indexs; // 各类型下题目在对应小题库中的序号
}PACKED T_GameType;

/* 题目结构 */
typedef struct
{
	char* intro; // 题目说明
	char* question; // 题干
	char* options; // 选项（只有选择题有）或颜色选项（只有图片题有）或图片选项（只有图片题有）
	char* blank; // 填空区域（只有图片题有）
	char* answer; // 答案（不可作答题目无）
	char* dispAnswer; // 显示答案
	char* analyse; // 解析
	char* score; // 分值
	char* kws; // 对应知识点
	uchar type; // 题目类型
}PACKED T_Question;

/* 媒体信息结构 */
typedef struct
{
	ushort type; // 文件类型标记
	uint length; // 数据长度
	uint addr; // 绝对地址
	char * name; // 文件名称
	char * id; // 文件ID, 即视频文件下载id
	char * cachePath; // 缓存文件名称
}PACKED T_MediaInfo;

/* 套题结构 */
typedef struct
{
	uint count; // 小题总数
	char* intro; // 题目说明
	char* question; // 题干
	T_Question** contents; // 各小题信息
	uchar type; // 题目类型
}PACKED T_MultiQuestion;

/* 带声音的文本结构 */
typedef struct
{
	char * content; // 文本内容
	uint   voice; // 文本声音地址，相对于其它库
	T_MediaInfo * voiceInfo;
}PACKED T_SoundText;

/* 视频信息结构 */
typedef struct
{
	uint count; // 视频数量
	T_MediaInfo ** videos; // 视频信息
}PACKED T_VideoInfo;

/* 富文本信息结构 */
typedef struct
{
	uint block; // 文本块数
	T_SoundText ** text; // 文本声音列表
	uint * pics; // 文本图片地址表，相对于其它库
//	T_MediaInfo ** picInfo;
}PACKED T_SuperTextInfo;

/* 数学例题信息结构 */
typedef struct
{
	uint count; // 例题数量
	T_SoundText ** title; // 标题列表
	T_SoundText ** question; // 题干列表
	T_SoundText ** answer; // 答案列表
	T_SoundText ** analyse; // 解析列表
}PACKED T_ExampleInfo;

/* 语文课文讲解信息结构 */
typedef struct
{
	uint block; // 文本块数
	T_SoundText ** text; // 原文列表
	T_SoundText ** exp; // 段落详解列表
	T_SoundText ** summary; // 段落总结列表
	T_SoundText ** theme; // 大段的中心思想列表
}PACKED T_ExplainInfo;

/* 汉字同步词汇结构 */
typedef struct
{
	uchar tear; // 能否拆字：标记为0x00，不可以做拆字游戏；标记为0x01表示可以做拆字游戏
	char content[32]; // 词汇
	char pinyin[160]; // 拼音
	uint voice; // 声音地址
	T_MediaInfo * voiceInfo;
}PACKED T_Words;

/* 汉字结构 */
typedef struct
{
	uchar type; // 写或者认标记
	char word[4]; // 生词
	char pinyin[16]; // 拼音
	uint voice; // 声音地址
	uint count; // 对应同步词汇个数
	T_Words ** syncWords; // 同步词汇
	T_MediaInfo * voiceInfo;
}PACKED T_NewCnChar;

/* 汉字学习信息结构 */
typedef struct
{
	uint count; // 本课生词个数
	T_NewCnChar ** words;
}PACKED T_CnCharInfo;

/* 书本简介结构 */
typedef struct
{
	char *text; // 书本简介内容
	uint voiceLen;
	char *voice; // 书本简介声音
	uint picLen;
	char *pic; // 书本简介图片
}PACKED T_BookIntro;

/*教材编码库结构*/
typedef struct
{
	char  code[256];   // 教材编码
    char  interCode[256]; // 教材内部编码
}PACKED T_BookISBN;

/* 试卷信息单元*/
typedef struct
{
	uint paperAddr ;//指向试卷索引库
	char *paperName ;//试卷名称

}PACKED T_InfoUnit;

/* 试卷信息结构 */
typedef struct{
	uint  paperCount; // 试卷总套数
	T_InfoUnit **papers;//各试卷信息

}PACKED T_PaperGroup;

/* 内部媒体（诺亚动漫、应用题模型、图形计算）信息结构 */
typedef struct
{
	uint length; // 数据长度
	uint addr; // 绝对地址
	char * cachePath; // 缓存文件名称
}PACKED T_InnerMediaInfo;

/* 链接内容结构  */
typedef struct
{
	uint length; // 数据长度
	char * data; // 缓存文件名称
}PACKED T_LinkContent;

#ifdef _WIN32
#pragma pack()
#endif

static inline long READ_LONG_8(uchar * buffer)
{
	long temp  =  (long)buffer[0] + (buffer[1]<<8) + (buffer[2]<<16) + (buffer[3]<<24);
	long temp1 = (long)((buffer[4]))
				+ ((buffer[5]<<8))
				+ ((buffer[6]<<16))
				+ ((buffer[7]<<24));

	long version = (temp1 <<16) + ((temp >> 16)&0x0000FFFF);

	LOGE("version:0x%x,temp:0x%x,temp1:0x%x,(temp>>16):0x%x,(temp1 <<16):0x%x\n",
			version,temp,temp1,(temp>>16),(temp1 <<16));
	return version;
}
static inline long READ_LONG_4(uchar * buffer)
{
	return (long)buffer[0] + (buffer[1]<<8) + (buffer[2]<<16) + (buffer[3]<<24);
}

static inline uint READ_INT_4(uchar * buffer)
{
	return (uint)buffer[0] + (buffer[1]<<8) + (buffer[2]<<16) + (buffer[3]<<24);
}

static inline uint READ_INT_3(uchar * buffer)
{
	return (uint)buffer[0] + (buffer[1]<<8) + (buffer[2]<<16);
}

static inline uint READ_INT_2(uchar * buffer)
{
	return (uint)buffer[0] + (buffer[1]<<8);
}

static inline uint READ_INT_1(uchar * buffer)
{
	return (uint)buffer[0];
}

/*--------------------------------------------
        将同步包文件头中的地址转换为绝对地址

    Parameters:
        offset 相对地址

    Returns:
    	相对地址有效返回相应绝对地址，否则返回INVALID_ADDR
----------------------------------------------*/
static inline uint GetSyncLibAddress(uint offset) {
	if(offset != INVALID_ADDR) {
		return offset + sizeof(T_ExtraFileHeader) + sizeof(T_FileHeader);
	}

	return INVALID_ADDR;
}

//#define READ_INT_4(buffer) ((uint)*((uchar*)buffer)) + (((uint)*((uchar*)buffer+1))<<8) + (((uint)*((uchar*)buffer+2))<<16) + (((uint)*((uchar*)buffer+3))<<24)
//#define READ_INT_3(buffer) ((uint)*((uchar*)buffer)) + (((uint)*((uchar*)buffer+1))<<8) + (((uint)*((uchar*)buffer+2))<<16)
//#define READ_INT_2(buffer) ((uint)*((uchar*)buffer)) + (((uint)*((uchar*)buffer+1))<<8)
//#define READ_INT_1(buffer) ((uint)*((uchar*)buffer))




/*--------------------------------------------
          释放多媒体数据信息缓冲区

    Parameters:
        buffer 多媒体数据信息结构
----------------------------------------------*/
DLLAPI void ReleaseMediaInfo(T_MediaInfo * buffer);

/*--------------------------------------------
        释放知识点视频版面信息内容缓冲区

    Parameters:
    	buffer 视频版面信息内容缓冲区
----------------------------------------------*/
DLLAPI void ReleaseVideoInfo(T_VideoInfo * buffer);

/*--------------------------------------------
          释放有声文本缓冲区

    Parameters:
        buffer 有声文本缓冲区
----------------------------------------------*/
DLLAPI void ReleaseSoundText(T_SoundText * buffer);

/*--------------------------------------------
        释放知识点富文本版面信息内容缓冲区

    Parameters:
    	buffer 富文本版面信息内容缓冲区
----------------------------------------------*/
DLLAPI void ReleaseSuperTextInfo(T_SuperTextInfo * buffer);

/*--------------------------------------------
        释放知识点试题版面信息内容缓冲区

    Parameters:
    	buffer 试题版面信息内容缓冲区
----------------------------------------------*/
DLLAPI void ReleasePaperInfo(T_Paper * buffer);

/*--------------------------------------------
         释放单个题目信息缓冲区

    Parameters:
        buffer 单个题目信息缓冲区
----------------------------------------------*/
DLLAPI void ReleaseQuestion(T_Question * buffer);

/*--------------------------------------------
          释放套题题目信息缓冲区

    Parameters:
        buffer 套题题目信息缓冲区
----------------------------------------------*/
DLLAPI void ReleaseMultiQuestion(T_MultiQuestion * buffer);

/*--------------------------------------------
        释放知识点例题版面信息内容缓冲区

    Parameters:
    	buffer 例题版面信息内容缓冲区
----------------------------------------------*/
DLLAPI void ReleaseExampleInfo(T_ExampleInfo * buffer);

/*--------------------------------------------
        申请并初始化汉字学习信息缓冲区

  	Parameters:
 		count 生字/词个数

    Returns:
 		成功返回初始化后的缓冲区，否则返回NULL。
----------------------------------------------*/
DLLAPI T_CnCharInfo * NewCnCharInfo(uint count);

/*--------------------------------------------
        释汉字学习信息内容缓冲区

    Parameters:
    	buffer 汉字学习信息内容缓冲区
----------------------------------------------*/
DLLAPI void ReleaseCnCharInfo(T_CnCharInfo * buffer);

/*--------------------------------------------
        释汉字信息内容缓冲区

    Parameters:
    	buffer 汉字学习信息内容缓冲区
----------------------------------------------*/
DLLAPI void ReleaseNewCnChar(T_NewCnChar * buffer);

/*--------------------------------------------
        释放语文课文讲解信息内容缓冲区

    Parameters:
    	buffer 语文课文讲解信息内容缓冲区
----------------------------------------------*/
DLLAPI void ReleaseExplainInfo(T_ExplainInfo * buffer);

/*--------------------------------------------
        释放课本介绍缓冲区

    Parameters:
    	buffer 课本介绍缓冲区
----------------------------------------------*/
DLLAPI void ReleaseBookIntro(T_BookIntro * buffer);

/*--------------------------------------------
        申请并初始化小测试(小题)信息缓冲区

  	Parameters:
 		count 小测试(小题)包含题目数目

    Returns:
 		成功返回初始化后的缓冲区，否则返回NULL。
----------------------------------------------*/
DLLAPI T_Quiz * NewQuizInfo(uint count);

/*--------------------------------------------
 	释放小测试(小题)信息缓冲区

	Parameters:
		buffer 小测试信息缓冲区
 ----------------------------------------------*/
DLLAPI void ReleaseQuizInfo(T_Quiz * buffer);

/*--------------------------------------------
 	申请并初始化考试(大题)信息缓冲区

	Parameters:
		count 考试(大题)包含题目数目

	Returns:
 		成功返回初始化后的缓冲区，否则返回NULL。
----------------------------------------------*/
DLLAPI T_Exam * NewExamInfo(uint count);

/*--------------------------------------------
	释放考试(大题)信息缓冲区

	Parameters:
		buffer 考试信息缓冲区
 ----------------------------------------------*/
DLLAPI void ReleaseExamInfo(T_Exam * buffer);

/*--------------------------------------------
 	申请并初始化游戏类型信息缓冲区

	Parameters:
		count 游戏包含游戏类型数目

	Returns:
 		成功返回初始化后的缓冲区，否则返回NULL。
----------------------------------------------*/
DLLAPI T_GameType * NewGameTypeInfo(uint count);

/*--------------------------------------------
	释放游戏类型信息缓冲区

	Parameters:
		buffer 游戏类型信息缓冲区
 ----------------------------------------------*/
DLLAPI void ReleaseGameTypeInfo(T_GameType * buffer);

/*--------------------------------------------
           读取多媒体数据信息。如果数据加密，数据长度数值是密文的长度
            注意：返回的缓冲区需要通过相关ReleaseXXXX函数来释放，否则会引起内存泄漏

    Parameters:
    	fp 文件指针
        libAddr 多媒体数据所在库在文件中的绝对地址
        offset 多媒体数据在相应库中的偏移地址

    Returns:
    	成功返回多媒体数据信息缓冲区，失败返回NULL
----------------------------------------------*/
DLLAPI T_MediaInfo * GetMediaInfo(FILE * fp, uint libAddr, uint offset);

/*--------------------------------------------
        判断题目是否是套题

    Parameters:
        type 题目类型标记

    Returns:
    	是返回TRUE，不是返回FALSE
----------------------------------------------*/
DLLAPI BOOL IsMultiQuestionType(uchar type);

/**
 * 根据题目类型获取内容对应的题目信息区域
 * Parameters:
 * 	index 内容顺序
 * 	qs 题目信息
 * Returns:
 * 	成功题目信息区域指针，否则返回NULL
 */
DLLAPI char ** GetQuestionField(T_Question* qs, uint index);

/**
 * 根据题目类型获取内容的结束符
 * Parameters:
 * 	type 题目类型
 * 	index 内容顺序
 * Returns:
 * 	该部分内容的结束符
 */
DLLAPI char GetQuestionFieldEndFlag(uchar type, uint index);

/*--------------------------------------------
 	判断媒体数据是否加密

 	Parameters:
		type 媒体类型
	Returns:
		TRUE加密，FALSE没有加密
----------------------------------------------*/
DLLAPI BOOL IsEncryptMediaType(ushort type);

/*--------------------------------------------
	对媒体数据进行解密
	  注意：返回的缓冲区需要调用者释放，否则会引起内存泄漏

	Parameters:
		offset 媒体数据绝对地址
		media 媒体数据缓冲区
 	Returns:
		TRUE解密成功，FALSE解密失败
 ----------------------------------------------*/
DLLAPI char * DecryptMediaData(char * file, FILE * fp, T_MediaInfo * info);


/*--------------------------------------------
	读取同步大包中各个子包的地址偏移

	Parameters:
		file 文件路径
		address 至少有4个原始的地址信息存储缓冲区
 	Returns:
		TRUE读取成功，FALSE读取失败
 ----------------------------------------------*/
DLLAPI BOOL GetPackPackageOffset(const char * file, uint * address);

/*--------------------------------------------
	读取同步大包中各个子包的地址偏移

	Parameters:
		file 文件路径
		address 至少有4个原始的地址信息存储缓冲区
 	Returns:
		TRUE读取成功，FALSE读取失败
 ----------------------------------------------*/
DLLAPI BOOL GetPackPackageOffsetWithFile(FILE*  fp, uint * address);

/*--------------------------------------------
        释放试题信息库缓冲区

    Parameters:
    	buffer 试题信息库缓冲区
----------------------------------------------*/
DLLAPI void ReleasePaperGroup(T_PaperGroup *buffer);

/*--------------------------------------------
	读取内部媒体内容
	注意：返回的缓存区需要通过相关ReleaseXXXX函数来释放，否则会引起内存泄漏

    Parameters:
    	fp 文件指针
        libAddr 内部媒体内容所在库在文件中的绝对地址
        offset 内部媒体内容在相应库中的偏移地址
        path 媒体缓存路径
        type 文件类型

    Returns:
    	成功缓存文件，返回内部媒体内容缓存区，失败返回NULL
 ----------------------------------------------*/
DLLAPI T_InnerMediaInfo * getInnerMediaInfo(FILE * fp, uint libAddr, uint offset,
		const char * path, int type);

/*--------------------------------------------
	释放内部媒体内容

	Parameters:
		buffer 内部媒体内容结构
 ----------------------------------------------*/
DLLAPI void ReleaseInnerMediaInfo(T_InnerMediaInfo * buffer);

/*--------------------------------------------
	读取链接内容
	 注意：返回的缓存区需要通过相关ReleaseXXXX函数来释放，否则会引起内存泄漏

    Parameters:
    	fp 文件指针
        libAddr 链接内容所在库在文件中的绝对地址
        offset 链接内容在相应库中的偏移地址

    Returns:
    	成功返回链接内容缓存区，失败返回NULL
 ----------------------------------------------*/
DLLAPI T_LinkContent * GetLinkContent(FILE * fp, uint libAddr, uint offset);

/*--------------------------------------------
	释放链接内容

	Parameters:
		buffer 链接内容结构
 ----------------------------------------------*/
DLLAPI void ReleaseLinkContent(T_LinkContent * buffer);

//合并各字节为整数
DLLAPI uint getIntByChars(char *bytes, int offset, int len);

//每字节代表一个字符数（0~9），将相应字节翻译成整数
DLLAPI uint turnChars2Int(char *bytes, int offset, int len);

#endif /* UTILS_H_ */
