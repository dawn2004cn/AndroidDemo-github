#ifndef HUFFMAN_H_
#define HUFFMAN_H_

#include "basictype.h"

/*--------------------------------------------
         解码一串数据，直到字符串结束或者遇到0x0A字符

    Parameters:
        pcInData 输入编码字符缓冲区
        wInDataLen 编码字符缓冲区长度
        pcOutData 输出解码字符缓冲区
        wOutDataLen 解码字符缓冲区长度
        pEnTab 单字节解压表
        pCnTab 双字节解压表

    Returns:
    	返回解码得到的字节数目
----------------------------------------------*/
DLLAPI uint Huffman_Unzip(char *pcInData, uint wInDataLen, char * pcOutData, uint wOutDataLen,
		char *pEnTab, char *pCnTab);

/*--------------------------------------------
         解码一串数据，直到字符串结束或遇到结束标记

    Parameters:
        pcInData 输入编码字符缓冲区
        wInDataLen 编码字符缓冲区长度
        pcOutData 输出解码字符缓冲区
        wOutDataLen 解码字符缓冲区长度
        dwCurrPos 解码的当前位置（以bit计数）。解码将从编码字符缓冲区的该位置开始，并且解码结束后，该值会变化以指向下一个需要解码的bit
        currTab 当前使用的解压表
        endFlag 解压结束标志
        pEnTab 单字节解压表
        pCnTab 双字节解压表

    Returns:
    	返回解码得到的字节数目
----------------------------------------------*/
DLLAPI uint Huffman_UnzipToEnd(char *pcInData, uint wInDataLen, char * pcOutData, uint wOutDataLen,
		uint *dwCurrPos, char **currTab, char endFlag, char *pEnTab, char *pCnTab);

/*--------------------------------------------
         解码一个字符

    Parameters:
        pcCompressTab 解压表
        pcCompressData 编码字符缓冲区
        dwCurrPos 解码的当前位置（以bit计数）。解码将从编码字符缓冲区的该位置开始，并且解码结束后，该值会变化以指向下一个需要解码的bit

    Returns:
    	返回解码得到的字符
----------------------------------------------*/
DLLAPI char Huffman_UnzipByte(char * pcCompressTab, char * pcCompressData, uint *dwCurrPos);

#endif  // #ifndef HUFFMAN_H_

