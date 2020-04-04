/*
 * common.c
 *
 *  Created on: 2012-5-4
 *      Author: lenovo
 */
#include <sys/stat.h>
#include <unistd.h>
#include "common.h"
#include "videodecryptkey.h"

/*--------------------------------------------
          释放多媒体数据信息缓冲区

    Parameters:
        buffer 多媒体数据信息结构
----------------------------------------------*/
DLLAPI void ReleaseMediaInfo(T_MediaInfo * buffer)
{
	if(buffer != NULL) {
		FREE(buffer->name);
		FREE(buffer->id);
		FREE(buffer->cachePath);
		FREE(buffer);
	}
}

/*--------------------------------------------
        释放视频信息内容缓冲区

    Parameters:
    	buffer 视频信息内容缓冲区
----------------------------------------------*/
DLLAPI void ReleaseVideoInfo(T_VideoInfo * buffer)
{
	if(buffer != NULL) {
		if(buffer->videos != NULL) {
			uint i;
			for(i=0; i<buffer->count; i++) {
				ReleaseMediaInfo(buffer->videos[i]);
			}
		}

		FREE(buffer->videos);
		FREE(buffer);
	}
}

/*--------------------------------------------
          释放有声文本缓冲区

    Parameters:
        buffer 有声文本缓冲区
----------------------------------------------*/
DLLAPI void ReleaseSoundText(T_SoundText * buffer)
{
	if(buffer != NULL) {
		FREE(buffer->content);
		ReleaseMediaInfo(buffer->voiceInfo);
		FREE(buffer);
	}
}

/*--------------------------------------------
        释放富文本信息内容缓冲区

    Parameters:
    	buffer 富文本信息内容缓冲区
----------------------------------------------*/
DLLAPI void ReleaseSuperTextInfo(T_SuperTextInfo * buffer)
{
	if(buffer != NULL) {
		if(buffer->text != NULL) {
			uint i;
			for(i=0; i<buffer->block; i++) {
				ReleaseSoundText(buffer->text[i]);
			}

			FREE(buffer->text);
		}

//		if(buffer->picInfo != NULL) {
//			uint i;
//			for(i=0; i<buffer->block; i++) {
//				ReleaseMediaInfo(buffer->picInfo[i]);
//			}
//
//			FREE(buffer->picInfo);
//		}

		FREE(buffer->pics);
		FREE(buffer);
	}
}


/*--------------------------------------------
        释放试题信息内容缓冲区

    Parameters:
    	buffer 试题信息内容缓冲区
----------------------------------------------*/
DLLAPI void ReleasePaperInfo(T_Paper * buffer)
{
	if(buffer != NULL) {
		if(buffer->type == PAPER_QUIZ) {
			ReleaseQuizInfo(buffer->quiz);
		} else if(buffer->type == PAPER_EXAMINATION) {
			ReleaseExamInfo(buffer->exam);
		} else if(buffer->type == PAPER_GAME) {
			ReleaseQuizInfo(buffer->quiz);
			ReleaseGameTypeInfo(buffer->game);
		}

		FREE(buffer);
	}
}

/*--------------------------------------------
         释放单个题目信息缓冲区

    Parameters:
        buffer 单个题目信息缓冲区
----------------------------------------------*/
DLLAPI void ReleaseQuestion(T_Question * buffer)
{
	if(buffer != NULL) {
		FREE(buffer->analyse);
		FREE(buffer->answer);
		FREE(buffer->blank);
		FREE(buffer->dispAnswer);
		FREE(buffer->intro);
		FREE(buffer->kws);
		FREE(buffer->options);
		FREE(buffer->question);
		FREE(buffer->score);
		FREE(buffer);
	}
}

/*--------------------------------------------
          释放套题题目信息缓冲区

    Parameters:
        buffer 套题题目信息缓冲区
----------------------------------------------*/
DLLAPI void ReleaseMultiQuestion(T_MultiQuestion * buffer)
{
	if(buffer != NULL) {
		if(buffer->contents != NULL) {
			uint i = 0;
			for(; i<buffer->count; i++) {
				ReleaseQuestion(buffer->contents[i]);
			}
		}

		FREE(buffer->intro);
		FREE(buffer->question);
		FREE(buffer->contents);
		FREE(buffer);
	}
}

/*--------------------------------------------
        释放例题信息内容缓冲区

    Parameters:
    	buffer 例题信息内容缓冲区
----------------------------------------------*/
DLLAPI void ReleaseExampleInfo(T_ExampleInfo * buffer)
{
	if(buffer != NULL) {
		if(buffer->title != NULL) {
			uint i;
			for(i=0; i<buffer->count; i++) {
				ReleaseSoundText(buffer->title[i]);
			}

			FREE(buffer->title);
		}

		if(buffer->analyse != NULL) {
			uint i;
			for(i=0; i<buffer->count; i++) {
				ReleaseSoundText(buffer->analyse[i]);
			}

			FREE(buffer->analyse);
		}

		if(buffer->answer != NULL) {
			uint i;
			for(i=0; i<buffer->count; i++) {
				ReleaseSoundText(buffer->answer[i]);
			}

			FREE(buffer->answer);
		}

		if(buffer->question != NULL) {
			uint i;
			for(i=0; i<buffer->count; i++) {
				ReleaseSoundText(buffer->question[i]);
			}

			FREE(buffer->question);
		}

		FREE(buffer);
	}
}

/*--------------------------------------------
        申请并初始化汉字学习信息缓冲区

  	Parameters:
 		count 生字/词个数

    Returns:
 		成功返回初始化后的缓冲区，否则返回NULL。
----------------------------------------------*/
DLLAPI T_CnCharInfo * NewCnCharInfo(uint count)
{
	T_CnCharInfo * info = (T_CnCharInfo *)MALLOC(sizeof(T_CnCharInfo));
	if(info == NULL) {
		OOM(sizeof(T_CnCharInfo));
		return NULL;
	}

	memset(info, 0, sizeof(T_CnCharInfo));

	info->count = count;

	info->words = (T_NewCnChar **)MALLOC(count*sizeof(T_NewCnChar*));
	if(info->words == NULL) {
		OOM(count*sizeof(T_NewCnChar*));
		ReleaseCnCharInfo(info);
		return NULL;
	}

	memset(info->words, 0, count * sizeof(T_NewCnChar*));
	return info;
}

/*--------------------------------------------
        释汉字信息内容缓冲区

    Parameters:
    	buffer 汉字学习信息内容缓冲区
----------------------------------------------*/
DLLAPI void ReleaseNewCnChar(T_NewCnChar * buffer)
{
	if(buffer != NULL) {
		if(buffer->syncWords != NULL) {
			uint i = 0;
			for(i=0; i<buffer->count; i++) {
				if(buffer->syncWords[i] != NULL) {
					ReleaseMediaInfo(buffer->syncWords[i]->voiceInfo);
					FREE(buffer->syncWords[i]);
				}
			}

			FREE(buffer->syncWords);
		}

		ReleaseMediaInfo(buffer->voiceInfo);
		FREE(buffer);
	}
}

/*--------------------------------------------
        释汉字学习信息内容缓冲区

    Parameters:
    	buffer 汉字学习信息内容缓冲区
----------------------------------------------*/
DLLAPI void ReleaseCnCharInfo(T_CnCharInfo * buffer)
{
	if(buffer != NULL) {
		if(buffer->words != NULL) {
			uint i = 0;
			for(i=0; i<buffer->count; i++) {
				ReleaseNewCnChar(buffer->words[i]);
			}

			FREE(buffer->words);
		}

		FREE(buffer);
	}
}

/*--------------------------------------------
        释放语文课文讲解信息内容缓冲区

    Parameters:
    	buffer 语文课文讲解信息内容缓冲区
----------------------------------------------*/
DLLAPI void ReleaseExplainInfo(T_ExplainInfo * buffer)
{
	if(buffer != NULL) {
		if(buffer->text != NULL) {
			uint i;
			for(i=0; i<buffer->block; i++) {
				ReleaseSoundText(buffer->text[i]);
			}

			FREE(buffer->text);
		}

		if(buffer->summary != NULL) {
			uint i;
			for(i=0; i<buffer->block; i++) {
				ReleaseSoundText(buffer->summary[i]);
			}

			FREE(buffer->summary);
		}

		if(buffer->theme != NULL) {
			uint i;
			for(i=0; i<buffer->block; i++) {
				ReleaseSoundText(buffer->theme[i]);
			}

			FREE(buffer->theme);
		}

		if(buffer->exp != NULL) {
			uint i;
			for(i=0; i<buffer->block; i++) {
				ReleaseSoundText(buffer->exp[i]);
			}

			FREE(buffer->exp);
		}

		FREE(buffer);
	}
}

/*--------------------------------------------
        释放课本介绍缓冲区

    Parameters:
    	buffer 课本介绍缓冲区
----------------------------------------------*/
DLLAPI void ReleaseBookIntro(T_BookIntro * buffer)
{
	if(buffer != NULL) {
		FREE(buffer->text);
		FREE(buffer->pic);
		FREE(buffer->voice);
		FREE(buffer);
	}
}

/*--------------------------------------------
        释放课本ISBN缓冲区

    Parameters:
    	buffer 课本ISBN缓冲区
----------------------------------------------*/
DLLAPI void ReleaseBookISBN(T_BookISBN * buffer)
{
	if(buffer != NULL) {
		FREE(buffer);
	}
}
/*--------------------------------------------
        申请并初始化小测试(小题)信息缓冲区

  	Parameters:
 		count 小测试(小题)包含题目数目

    Returns:
 		成功返回初始化后的缓冲区，否则返回NULL。
----------------------------------------------*/
DLLAPI T_Quiz * NewQuizInfo(uint count)
{
	T_Quiz * quiz = (T_Quiz *)MALLOC(sizeof(T_Quiz));
	if(quiz == NULL) {
		OOM(sizeof(T_Quiz));
		return NULL;
	}

	memset(quiz, 0, sizeof(T_Quiz));

	quiz->count = count;

	quiz->types = (uchar *)MALLOC(count);
	if(quiz->types == NULL) {
		OOM(count);
		ReleaseQuizInfo(quiz);
		return NULL;
	}

	quiz->addrs = (uint *)MALLOC(count * sizeof(uint));
	if(quiz->addrs == NULL) {
		OOM(count * sizeof(uint));
		ReleaseQuizInfo(quiz);
		return NULL;
	}

	memset(quiz->types, 0, count);
	memset(quiz->addrs, 0, count * sizeof(uint));

	return quiz;
}

/*--------------------------------------------
 	释放小测试(小题)信息缓冲区

	Parameters:
		buffer 小测试信息缓冲区
 ----------------------------------------------*/
DLLAPI void ReleaseQuizInfo(T_Quiz * buffer)
{
	if(buffer != NULL) {
		FREE(buffer->addrs);
		FREE(buffer->types);
		FREE(buffer);
	}
}

/*--------------------------------------------
 	申请并初始化考试(大题)信息缓冲区

	Parameters:
		count 考试(大题)包含题目数目

	Returns:
 		成功返回初始化后的缓冲区，否则返回NULL。
----------------------------------------------*/
DLLAPI T_Exam * NewExamInfo(uint count)
{
	T_Exam * exam = (T_Exam *)MALLOC(sizeof(T_Exam));
	if(exam == NULL) {
		OOM(sizeof(T_Exam));
		return NULL;
	}

	memset(exam, 0, sizeof(T_Exam));

	exam->count = count;

	exam->intro = (char **)MALLOC(count * sizeof(char *));
	if(exam->intro == NULL) {
		OOM(count * sizeof(char *));
		ReleaseExamInfo(exam);
		return NULL;
	}

	exam->contents = (T_Quiz **)MALLOC(count * sizeof(T_Quiz *));
	if(exam->contents == NULL) {
		OOM(count * sizeof(T_Quiz *));
		ReleaseExamInfo(exam);
		return NULL;
	}

	memset(exam->intro, 0, count * sizeof(char *));
	memset(exam->contents, 0, count * sizeof(T_Quiz *));

	return exam;
}

/*--------------------------------------------
	释放考试(大题)信息缓冲区

	Parameters:
		buffer 考试信息缓冲区
 ----------------------------------------------*/
DLLAPI void ReleaseExamInfo(T_Exam * buffer)
{
	if(buffer != NULL) {
		uint i = 0;
		for(; i<buffer->count; i++) {
			if(buffer->intro != NULL) {
				FREE(buffer->intro[i]);
			}

			if(buffer->contents != NULL) {
				ReleaseQuizInfo(buffer->contents[i]);
			}
		}

		FREE(buffer->intro);
		FREE(buffer->contents);
		FREE(buffer);
	}
}

/*--------------------------------------------
 	申请并初始化游戏类型信息缓冲区

	Parameters:
		count 游戏包含游戏类型数目

	Returns:
 		成功返回初始化后的缓冲区，否则返回NULL。
----------------------------------------------*/
DLLAPI T_GameType * NewGameTypeInfo(uint count)
{
	T_GameType * game = (T_GameType *)MALLOC(sizeof(T_GameType));
	if(game == NULL) {
		OOM(sizeof(T_GameType));
		return NULL;
	}

	memset(game, 0, sizeof(T_GameType));

	game->typeCount = count;
	game->types = (uchar *)MALLOC(count);
	if(game->types == NULL) {
		OOM(count);
		ReleaseGameTypeInfo(game);
		return NULL;
	}

	game->names = (char **)MALLOC(count * sizeof(char *));
	if(game->names == NULL) {
		OOM(count * sizeof(char *));
		ReleaseGameTypeInfo(game);
		return NULL;
	}

	game->counts = (uint *)MALLOC(count * sizeof(uint));
	if(game->counts == NULL) {
		OOM(count * sizeof(uint));
		ReleaseGameTypeInfo(game);
		return NULL;
	}

	game->indexs = (uint **)MALLOC(count * sizeof(uint *));
	if(game->indexs == NULL) {
		OOM(count * sizeof(uint *));
		ReleaseGameTypeInfo(game);
		return NULL;
	}

	memset(game->types, 0, count);
	memset(game->names, 0, count * sizeof(char *));
	memset(game->counts, 0, count * sizeof(uint));
	memset(game->indexs, 0, count * sizeof(uint *));

	return game;
}

/*--------------------------------------------
	释放游戏类型信息缓冲区

	Parameters:
		buffer 游戏类型信息缓冲区
 ----------------------------------------------*/
DLLAPI void ReleaseGameTypeInfo(T_GameType * buffer)
{
	if(buffer != NULL) {
		if(buffer->indexs != NULL) {
			uint i = 0;
			for(; i<buffer->typeCount; i++) {
				FREE(buffer->indexs[i]);
			}

			FREE(buffer->indexs);
		}

		if(buffer->names != NULL) {
			uint i = 0;
			for(; i<buffer->typeCount; i++) {
				FREE(buffer->names[i]);
			}

			FREE(buffer->names);
		}

		FREE(buffer->counts);
		FREE(buffer->types);
		FREE(buffer);
	}
}

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
DLLAPI T_MediaInfo * GetMediaInfo(FILE * fp, uint libAddr, uint offset)
{
	uchar info[6] = { 0 };
	uint len = 0;
	uint readLen = 0;
	uint i = 0;
	uint splash = 0;
	ushort type = 0;
	char * buffer = NULL;
	T_MediaInfo * media = NULL;
	uint addr = 0;
	const int FACTOR = 3;

	if(fp == NULL || INVALID_ADDR == libAddr || INVALID_ADDR == offset) {
		LOGW("FILE pointer is NULL or lib address is INVALID_ADDR or media address is INVALID_ADDR!\n");
		return NULL;
	}

	addr = libAddr + offset;

	// 读取标记和链长
	fseek(fp, addr, SEEK_SET);
	fread(info, sizeof(info), 1, fp);

	type = READ_INT_2(info);
	len = READ_INT_4(info+2);

	LOGD("GetMediaInfo::addr : 0x%08x, type : 0x%04x, len : 0x%04x\n", addr, type, len);

	if(len <= 0) {
		LOGE("Media content length is 0! type:0x%04x\n", type);
		return NULL;
	}

	readLen = len;

	// TODO: 如果名字长于1024字节，那么下面获取的数据偏移将是错误的！
	if(readLen > 1024) {
		LOGD("GetMediaInfo::The data is %d bytes, but we just read 1024 bytes for media name.\n", len);
		readLen = 1024;
	}

	buffer = (char *)MALLOC(readLen);
	if(buffer == NULL) {
		OOM(readLen);
		return NULL;
	}

	// 读取文件名和内容
	memset(buffer, 0, readLen);
	fread(buffer, readLen, 1, fp);

	media = (T_MediaInfo *)MALLOC(sizeof(T_MediaInfo));
	if(media == NULL) {
		OOM(sizeof(T_MediaInfo));
		FREE(buffer);
		return NULL;
	}

	memset(media, 0, sizeof(T_MediaInfo));
	media->type = type;

	for(i=0; i<readLen; i++) {
		if(buffer[i] == 0) {
			break;
		} else if(buffer[i] == '/') {
			LOGD("GetMediaInfo: There is a splash in file name!\n");
			splash++;
		}
	}

	// 为了字符串末尾的'\0'
	i++;

	media->name = (char *)MALLOC(i+splash*FACTOR);
	if(media->name == NULL) {
		OOM(i);
		FREE(buffer);
		ReleaseMediaInfo(media);
		return NULL;
	}

	memset(media->name, 0, i+splash*FACTOR);
	if(splash == 0) {
		memcpy(media->name, buffer, i);
	} else {
		int j,k,m;
		for(j=0, k=0; k<i; j++, k++) {
			if(buffer[k] == '/') {
				// 将一个‘/’换成FACTOR个‘_’
				for(m=0; m<FACTOR; m++) {
					media->name[j++] = '_';
				}

				// 等下还要加一次，上面循环多加了1，这里减掉
				j--;
			} else {
				media->name[j] = buffer[k];
			}

		}
	}

	LOGD("GetMediaInfo: media name:%s\n", media->name);

	media->addr = addr + 6 + i;
	media->length = len - i;

	FREE(buffer);
	return media;
}

/*--------------------------------------------
        判断题目是否是套题

    Parameters:
        type 题目类型标记

    Returns:
    	是返回TRUE，不是返回FALSE
----------------------------------------------*/
DLLAPI BOOL IsMultiQuestionType(uchar type)
{
	return type == QS_FILL_BLANK || type == QS_FILL_BLANK_VOICE || type == QS_MULTI_VOICE
			|| type == QS_MULTI_READ || type == QS_MULTI_CONNECT;
}

/**
 * 根据题目类型获取内容对应的题目信息区域
 * Parameters:
 * 	index 内容顺序
 * 	qs 题目信息
 * Returns:
 * 	成功题目信息区域指针，否则返回NULL
 */
DLLAPI char ** GetQuestionField(T_Question* qs, uint index)
{
	char ** field = NULL;

	switch(qs->type) {
	case QS_CHOICE_SINGLE:
	case QS_CHOICE_MULTI_EXTRA:
	case QS_CHOICE_MULTI:
	case QS_CHOICE_FILL:
	case QS_CHOICE_SINGLE_VOICE:
	case QS_CHOICE_MULTI_VOICE:
	case QS_CHOICE_FILL_VOICE:
	case QS_CHOICE_CLICK:
		/**
		 * 题目说明 + 0x09 + 题干 + 0x09 + 选项 + 0x09 + 答案 + 0x09 + 显示答案 + 0x09
		 *  + 解析 + 0x09 + 分值  + 0x09 +对应知识点名称+ 0x0a
		 */
		switch(index) {
		case 0:		field = &qs->intro;			break;
		case 1:		field = &qs->question;		break;
		case 2:		field = &qs->options;		break;
		case 3:		field = &qs->answer;		break;
		case 4:		field = &qs->dispAnswer;	break;
		case 5:		field = &qs->analyse;		break;
		case 6:		field = &qs->score;			break;
		case 7:		field = &qs->kws;			break;
		}
		break;
	case QS_FILL_ZONE:
	case QS_FILL_LINE:
	case QS_FILL_FRAME:
	case QS_FILL_ZONE_VOICE:
	case QS_FILL_LINE_VOICE:
	case QS_LINK_PORT:
	case QS_LINK_LAND:
	case QS_ORDER_WORD:
	case QS_ORDER_OPTS:
	case QS_ORDER_OPTS_VOICE:
		/**
		 * 题目说明 + 0x09 + 题干 + 0x09 + 答案 + 0x09 + 显示答案 + 0x09 + 解析 + 0x09
		 *  + 分值 + 0x09 +对应知识点名称 + 0x0a
		 */
		switch(index) {
		case 0:		field = &qs->intro;			break;
		case 1:		field = &qs->question;		break;
		case 2:		field = &qs->answer;		break;
		case 3:		field = &qs->dispAnswer;	break;
		case 4:		field = &qs->analyse;		break;
		case 5:		field = &qs->score;			break;
		case 6:		field = &qs->kws;			break;
		}
		break;
	case QS_PIC_COLOR:
	case QS_PIC_STRING:
	case QS_PIC_PIC:
	case QS_PIC_COLOR_VOICE:
	case QS_PIC_STRING_VOICE:
	case QS_PIC_PIC_VOICE:
		/**
		 * 题目说明 + 0x09 + 题干 + 0x09 + 填空区域 + 0x09 + 颜色选项 + 0x09 +答案 + 0x09
		 *  + 显示答案 + 0x09 + 解析 + 0x09 + 分值  + 0x09 +对应知识点名称 + 0x0a
		 */
		switch(index) {
		case 0:		field = &qs->intro;			break;
		case 1:		field = &qs->question;		break;
		case 2:		field = &qs->blank;			break;
		case 3:		field = &qs->options;		break;
		case 4:		field = &qs->answer;		break;
		case 5:		field = &qs->dispAnswer;	break;
		case 6:		field = &qs->analyse;		break;
		case 7:		field = &qs->score;			break;
		case 8:		field = &qs->kws;			break;
		}
		break;
	case QS_FILL_BLANK:
	case QS_FILL_BLANK_VOICE:
	case QS_MULTI_VOICE:
	case QS_MULTI_READ:
	case QS_MULTI_CONNECT:
		/**
		 * 内容 + 0x0a
		 */
		field = &qs->intro;
		break;
	case QS_NO_STD_ANSWER:
		/**
		 * 题目说明 + 0x09 + 题干 + 0x09 + 显示答案 + 0x09 + 解析 + 0x09
		 *  + 分值  + 0x09 + 对应知识点名称 + 0x0a
		 */
		switch(index) {
		case 0:		field = &qs->intro;			break;
		case 1:		field = &qs->question;		break;
		case 2:		field = &qs->dispAnswer;	break;
		case 3:		field = &qs->analyse;		break;
		case 4:		field = &qs->score;			break;
		case 5:		field = &qs->kws;			break;
		}
		break;
	default:
		LOGW("Unsupported question type:%d\n", qs->type);
		field = &qs->intro; // TODO: FOR TEST
		break;
	}

	return field;
}

/**
 * 根据题目类型获取内容的结束符
 * Parameters:
 * 	type 题目类型
 * 	index 内容顺序
 * Returns:
 * 	该部分内容的结束符
 */
DLLAPI char GetQuestionFieldEndFlag(uchar type, uint index)
{
	char flag = 0x0A;

	switch(type) {
	case QS_CHOICE_SINGLE:
	case QS_CHOICE_MULTI_EXTRA:
	case QS_CHOICE_MULTI:
	case QS_CHOICE_FILL:
	case QS_CHOICE_SINGLE_VOICE:
	case QS_CHOICE_MULTI_VOICE:
	case QS_CHOICE_FILL_VOICE:
	case QS_CHOICE_CLICK:
		/**
		 * 题目说明 + 0x09 + 题干 + 0x09 + 选项 + 0x09 + 答案 + 0x09 + 显示答案 + 0x09
		 *  + 解析 + 0x09 + 分值  + 0x09 +对应知识点名称+ 0x0a
		 */
		flag = index < 7 ? 0x09 : 0x0A;
		break;
	case QS_FILL_ZONE:
	case QS_FILL_LINE:
	case QS_FILL_FRAME:
	case QS_FILL_ZONE_VOICE:
	case QS_FILL_LINE_VOICE:
	case QS_LINK_PORT:
	case QS_LINK_LAND:
	case QS_ORDER_WORD:
	case QS_ORDER_OPTS:
	case QS_ORDER_OPTS_VOICE:
		/**
		 * 题目说明 + 0x09 + 题干 + 0x09 + 答案 + 0x09 + 显示答案 + 0x09 + 解析 + 0x09
		 *  + 分值 + 0x09 +对应知识点名称 + 0x0a
		 */
		flag = index < 6 ? 0x09 : 0x0A;
		break;
	case QS_PIC_COLOR:
	case QS_PIC_STRING:
	case QS_PIC_PIC:
	case QS_PIC_COLOR_VOICE:
	case QS_PIC_STRING_VOICE:
	case QS_PIC_PIC_VOICE:
		/**
		 * 题目说明 + 0x09 + 题干 + 0x09 + 填空区域 + 0x09 + 颜色选项 + 0x09 +答案 + 0x09
		 *  + 显示答案 + 0x09 + 解析 + 0x09 + 分值  + 0x09 +对应知识点名称 + 0x0a
		 */
		flag = index < 8 ? 0x09 : 0x0A;
		break;
	case QS_FILL_BLANK:
	case QS_FILL_BLANK_VOICE:
	case QS_MULTI_VOICE:
	case QS_MULTI_READ:
	case QS_MULTI_CONNECT:
		/**
		 * 内容 + 0x0a
		 */
		flag = 0x0A;
		break;
	case QS_NO_STD_ANSWER:
		/**
		 * 题目说明 + 0x09 + 题干 + 0x09 + 显示答案 + 0x09 + 解析 + 0x09
		 *  + 分值  + 0x09 + 对应知识点名称 + 0x0a
		 */
		flag = index < 5 ? 0x09 : 0x0A;
		break;
	default:
		LOGW("Unsupported question type:%d\n", type);
		break;
	}

	return flag;
}

/*--------------------------------------------
 	判断媒体数据是否加密
 	Parameters:
		type 媒体类型
	Returns:
		TRUE加密，FALSE没有加密
----------------------------------------------*/
DLLAPI BOOL IsEncryptMediaType(ushort type)
{
	return type == 0x0001 || type == 0x0002 || type == 0x0003 || type == 0x0004 ||
			type == 0x0005 || type == 0x000f || type == 0x0011 || type == 0x0012;
}

/*--------------------------------------------
	对媒体数据进行解密
	Parameters:
		offset 媒体数据绝对地址
		media 媒体数据缓冲区
 	Returns:
		TRUE解密成功，FALSE解密失败
 ----------------------------------------------*/
DLLAPI char * DecryptMediaData(char * file, FILE * fp, T_MediaInfo * info)
{
#define KEN_LEN (64*1024)

#ifdef ANDROID
#ifdef TEST
	uchar key[KEN_LEN] = { 0 };
	uint actOffset = INVALID_ADDR;
	uint i = 0;
	char * buffer = NULL;

	if(info == NULL) {
		LOGE("MediaInfo is NULL!\n");
		return NULL;
	}

	if (GetDecryptKey_(file, info->addr, key) == -1) {
		LOGE("Reading decrypt key failed!\n");
		return NULL;
	}

	info->length = GetVideoFileLength(file, info->addr);
	if(info->length == 0) {
		LOGE("Media length is 0!\n");
		return NULL;
	}

	buffer = (char *)MALLOC(info->length);
	if(buffer == NULL) {
		OOM(info->length);
		return NULL;
	}

	actOffset = GetVideoFileStartOffset(file, info->addr);

	fseek(fp, actOffset, SEEK_SET);
	fread(buffer, info->length, 1, fp);

	for (i = 0; i < info->length; i++) {
		buffer[i] ^= key[i % KEN_LEN];
	}

	return buffer;
#endif
#else
	LOGW("Media is not decrypt\n");
	return NULL;
#endif
}

/*--------------------------------------------
	读取同步大包中各个子包的地址偏移

	Parameters:
		file 文件路径
		address 至少有4个原始的地址信息存储缓冲区
 	Returns:
		TRUE读取成功，FALSE读取失败
 ----------------------------------------------*/
DLLAPI BOOL GetPackPackageOffset(const char * file, uint * address)
{
	FILE*  fp  = NULL;
	int  seekResult = 0;
	uint readResult = 0;
	T_FileAddress addrs;
	uint clickOffset = 0;
	BOOL result = FALSE;

	memset(&addrs, 0, sizeof(T_FileAddress));

	/* 打开文件 */
	fp = fopen(file, "rb");
	if (fp == NULL)
	{
		LOGE("File open error:%s\n", file != NULL ? file : "NULL");
		return FALSE;
	}

	result = GetPackPackageOffsetWithFile(fp, address);

	fclose(fp);

	return result;
}

/*--------------------------------------------
	读取同步大包中各个子包的地址偏移

	Parameters:
		file 文件路径
		address 至少有4个原始的地址信息存储缓冲区
 	Returns:
		TRUE读取成功，FALSE读取失败
 ----------------------------------------------*/
DLLAPI BOOL GetPackPackageOffsetWithFile(FILE*  fp, uint * address)
{
	int  seekResult = 0;
	uint readResult = 0;
	T_FileAddress addrs;
	uint clickOffset = 0;

	if (fp == NULL)
	{
		return FALSE;
	}

	memset(&addrs, 0, sizeof(T_FileAddress));

	/* 确认文件类型 */
	{
		char fileFlag[8] = { 0 };
		fseek(fp, 0, SEEK_SET);
		fread(fileFlag, sizeof(fileFlag)-1, 1, fp);
		if( 0 != strcmp(fileFlag, "XXTB") )
		{
			LOGW("%d is not a sync pack lib!\n", fp);
			return FALSE;
		}
	}

	/* 跳过附加文件头、文件头和文件说明块  */
	seekResult = fseek(fp, sizeof(T_ExtraFileHeader) + sizeof(T_FileHeader)
			+ sizeof(T_FileInfo), SEEK_SET);

	/* 读取文件索引数据 */
	memset(&addrs, 0, sizeof(T_FileAddress));
	readResult = fread(&addrs, sizeof(T_FileAddress), 1, fp);
	if (readResult != 1)
	{
		LOGE("Read error: expected %d but returned %d!", 1, readResult);
		return FALSE;
	}

	address[0] = GetSyncLibAddress(READ_INT_4(addrs.mainliboffset));
	address[1] = GetSyncLibAddress(READ_INT_4(addrs.childlib0));
	address[2] = GetSyncLibAddress(READ_INT_4(addrs.childlib1));
	address[3] = GetSyncLibAddress(READ_INT_4(addrs.childlib2));

	LOGD("index: 0x%08x\n", address[0]);
	LOGD("sync: 0x%08x\n",address[1]);
	LOGD("quiz: 0x%08x\n", address[2]);
	LOGD("clickstudy: 0x%08x\n", address[3]);

	return TRUE;
}

/*--------------------------------------------
        释放试题信息库缓冲区

    Parameters:
    	buffer 试题信息库缓冲区
----------------------------------------------*/
DLLAPI void ReleasePaperGroup(T_PaperGroup *buffer)
{
	if(buffer != NULL)
	{
		while(buffer->paperCount > 0){
			buffer->paperCount = buffer->paperCount - 1 ;
			FREE(buffer->papers[buffer->paperCount]->paperName);
		}
		FREE(buffer->papers);
		FREE(buffer);
	}
}

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
		const char * path, int type)
{
	uint len = 0;
	uchar countBuffer[4] = { 0 };
	T_InnerMediaInfo * result = NULL;

	if(NULL == fp || INVALID_ADDR == offset || INVALID_ADDR == libAddr || NULL == path) {
		LOGW("FILE pointer is NULL or lib address is INVALID_ADDR or lib address or path is INVALID_ADDR");
		return NULL;
	}

	fseek(fp, libAddr + offset, SEEK_SET);
	fread(countBuffer, sizeof(countBuffer), 1, fp);

	len = READ_INT_4(countBuffer);
	if(len <= 0 ) {
		LOGW("The data length is 0!");
		return NULL;
	}

	result = (T_InnerMediaInfo *)MALLOC(sizeof(T_InnerMediaInfo));
	if(result == NULL) {
		OOM(sizeof(T_InnerMediaInfo));
		return NULL;
	}

	memset(result, 0, sizeof(T_InnerMediaInfo));

	result->length = len;
	result->addr = libAddr + offset;

	// 写缓存数据
	{
		const int BUFFER_SIZE = 4096;
		char buffer[BUFFER_SIZE];
		int read = 0, write = 0;
		BOOL readOrWriteError = FALSE;

		FILE * wfp = fopen(path, "wb");
		if(fp == NULL) {
			LOGW("Create file(%s) failed!\n", path);
			ReleaseInnerMediaInfo(result);
			return NULL;
		}

		while (len != 0) {
			memset(buffer, 0, BUFFER_SIZE);
			read = fread(buffer, 1, BUFFER_SIZE, fp);
			if(read != BUFFER_SIZE) {
				if(!feof(fp)) {
					LOGW("%s:File read error! Expected %d but return %d\n", __FUNCTION__, BUFFER_SIZE, read);
					readOrWriteError = TRUE;
					break;
				}
			}

			write = fwrite(buffer, 1, read, wfp);
			if(write != read) {
				LOGW("%s:File write error! Expected %d but return %d\n", __FUNCTION__, read, write);
				readOrWriteError = TRUE;
				break;
			}

			if(len > BUFFER_SIZE) {
				len -= BUFFER_SIZE;
			} else {
				len = 0;
			}
		}

		fclose(wfp);

		if(readOrWriteError) {
			remove(path);
			ReleaseInnerMediaInfo(result);
			return NULL;
		} else {
			// 改变文件权限
			chmod(path, S_IRWXU | S_IRGRP | S_IROTH);
			result->cachePath = path;
		}
	}

	return result;
}

/*--------------------------------------------
	释放内部媒体内容

	Parameters:
		buffer 内部媒体内容结构
 ----------------------------------------------*/
DLLAPI void ReleaseInnerMediaInfo(T_InnerMediaInfo * buffer)
{
	if(buffer != NULL) {
		FREE(buffer->cachePath);
		FREE(buffer);
	}
}

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
DLLAPI T_LinkContent * GetLinkContent(FILE * fp, uint libAddr, uint offset)
{
	uint len = 0;
	uchar countBuffer[4] = { 0 };
	T_LinkContent * result = NULL;

	if(NULL == fp || INVALID_ADDR == offset || INVALID_ADDR == libAddr) {
		LOGW("FILE pointer is NULL or lib address is INVALID_ADDR or lib address is INVALID_ADDR");
		return NULL;
	}

	fseek(fp, libAddr + offset, SEEK_SET);
	fread(countBuffer, sizeof(countBuffer), 1, fp);

	len = READ_INT_4(countBuffer);
	if(len <= 0 ) {
		LOGW("The data length is 0!");
		return NULL;
	}

	result = (T_LinkContent *)MALLOC(sizeof(T_LinkContent));
	if(result == NULL) {
		OOM(sizeof(T_LinkContent));
		return NULL;
	}

	result->length = len;

	result->data = (char *)MALLOC(len);
	if(result->data == NULL) {
		OOM(len);
		FREE(result);
		return NULL;
	}

	fread(result->data, len, 1, fp);

	return result;
}

/*--------------------------------------------
	释放链接内容

	Parameters:
		buffer 链接内容结构
 ----------------------------------------------*/
DLLAPI void ReleaseLinkContent(T_LinkContent * buffer)
{
	if(buffer != NULL)
	{
		FREE(buffer->data);
		FREE(buffer);
	}
}

//合并各字节为整数
DLLAPI uint getIntByChars(char *bytes, int offset, int len)
{
	uint result = 0;
	int i=0;
	for(; i<len; ++i){
		result += (bytes[offset+i] & 0x0FF)<<(i*8);
	}
	return result ;
}

//每字节代表一个字符数（0~9），将相应字节翻译成整数
DLLAPI uint turnChars2Int(char *bytes, int offset, int len)
{
	uint result = 0;
	int i=0;
	for(; i<len; ++i){
		result += ((bytes[offset+i] & 0x0FF) - '0')*(int)pow(10, len - i -1);
	}
	return result;
}

//字节流转换为十六进制字符串
void ByteToHexStr(const unsigned char* source, char* dest, int sourceLen)
{
    short i;
    unsigned char highByte, lowByte;

    for (i = 0; i < sourceLen; i++)
    {
        highByte = source[i] >> 4;
        lowByte = source[i] & 0x0f ;

        highByte += 0x30;

        if (highByte > 0x39)
                dest[i * 2] = highByte + 0x07;
        else
                dest[i * 2] = highByte;

        lowByte += 0x30;
        if (lowByte > 0x39)
            dest[i * 2 + 1] = lowByte + 0x07;
        else
            dest[i * 2 + 1] = lowByte;
    }
    return ;
}

//字节流转换为十六进制字符串的另一种实现方式
void Hex2Str( const char *sSrc,  char *sDest, int nSrcLen )
{
    int  i;
    char szTmp[3];

    for( i = 0; i < nSrcLen; i++ )
    {
        sprintf( szTmp, "%02X", (unsigned char) sSrc[i] );
        memcpy( &sDest[i * 2], szTmp, 2 );
    }
    return ;
}

//十六进制字符串转换为字节流
void HexStrToByte(const char* source, unsigned char* dest, int sourceLen)
{
    short i;
    unsigned char highByte, lowByte;

    for (i = 0; i < sourceLen; i += 2)
    {
        highByte = toupper(source[i]);
        lowByte  = toupper(source[i + 1]);

        if (highByte > 0x39)
            highByte -= 0x37;
        else
            highByte -= 0x30;

        if (lowByte > 0x39)
            lowByte -= 0x37;
        else
            lowByte -= 0x30;

        dest[i / 2] = (highByte << 4) | lowByte;
    }
    return ;
}


