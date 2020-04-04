#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "huffman.h"

DLLAPI char Huffman_UnzipByte(char * pcCompressTab, char * pcCompressData, uint *dwCurrPos)
{
    char    uByte;			            // 解压后字符
    uint	lStep = 0;		    // 步长，0 = 步长1
    uint	dwInputData;	    // 压缩数据输入流
    uint	lOffset;          // 解码字符的偏移长度
    uint	dwFirst;          // 该长度的hfm编码的first 值
    short	wAddr;          // 该长度的hfm编码对应的字母表地址

    // 解压，从哈夫曼码表中搜索符合的字节
    dwInputData = (pcCompressData[(*dwCurrPos)/8] >> (7 - (*dwCurrPos)%8)) & 0x01;	// 输入第一个解码bit
    (*dwCurrPos)++;
    memcpy(&dwFirst,pcCompressTab,4);
    memcpy(&wAddr,&pcCompressTab[4],2);
    while ( dwInputData < dwFirst )
    {
        dwInputData = (dwInputData << 1) + ((pcCompressData[(*dwCurrPos)/8] >> (7 - (*dwCurrPos)%8)) & 0x01);//输入下一个解码bit
        (*dwCurrPos)++;
        lStep++;
        memcpy(&dwFirst,&pcCompressTab[lStep * 6],4);
        memcpy(&wAddr,&pcCompressTab[lStep * 6+4],2);
    }

    // 计算偏移得到解压的字节
    lOffset = wAddr + dwInputData - dwFirst;
    uByte = pcCompressTab[lOffset];

    return uByte;
}

DLLAPI uint Huffman_UnzipToEnd(char *pcInData, uint wInDataLen, char * pcOutData, uint wOutDataLen,
		uint *dwCurrPos, char **currTab, char endFlag, char *pEnTab, char *pCnTab)
{
	uint wInDataBitLen = wInDataLen * 8; // 解码总长度 按位计算

	uint      dwDataLen = 0;
	char		*pcCompressTab = *currTab; // 默认从英文解压表开始解压
	char		cCompressByte;	                // 解码后的字节
	char		*pcDataTmp = pcOutData;	        // 数据存储临时指针

//	printf("wInDataLen:%d, wOutDataLen:%d, dwCurrPos:%d, endFlag:0x%02x\n", wInDataLen, wOutDataLen, *dwCurrPos, endFlag);

	// 解码所需要的数据
	while (*dwCurrPos < wInDataBitLen)
	{
		// 解码一个字节
		cCompressByte = Huffman_UnzipByte(pcCompressTab, pcInData, dwCurrPos);

		// 判断解到的字节内容
		if (0x02 == cCompressByte)
		{
			// 如果是转码标志，转换码表
			if (pcCompressTab == pEnTab)
			{
				pcCompressTab = pCnTab;
				*currTab = pCnTab;
			}
			else
			{
				pcCompressTab = pEnTab;
				*currTab = pEnTab;
			}
			continue;
		}
		else if (endFlag == cCompressByte)
		{
			// 结束了
			break;
		}
		else
		{
			// 保存解码数据
			if (dwDataLen < wOutDataLen)
			{
				*pcDataTmp = cCompressByte;
				pcDataTmp++;
			}
			dwDataLen++;
			continue;
		}
	}

	return dwDataLen;
}

DLLAPI uint Huffman_Unzip(char *pcInData, uint wInDataLen, char * pcOutData, uint wOutDataLen,
		char *pEnTab, char *pCnTab)
{
	const uint wInDataBitLen = wInDataLen * 8; // 解码总长度 按位计算

	uint		dwCurrPos = 0;          // 解码当前的位置 按位计算
	uint      dwDataLen = 0;
	char		*pcCompressTab = pEnTab; // 默认从英文解压表开始解压
	char		cCompressByte;	                // 解码后的字节
	char		*pcDataTmp = pcOutData;	        // 数据存储临时指针

//	printf("unzip start\n");
	// 解码所需要的数据
	while (dwCurrPos < wInDataBitLen)
	{
		// 解码一个字节
		cCompressByte = Huffman_UnzipByte(pcCompressTab, pcInData, &dwCurrPos);

//		printf("%02x  ", (uchar)cCompressByte);

		// 判断解到的字节内容
		if (0x02 == cCompressByte)
		{
			// 如果是转码标志，转换码表
			if (pcCompressTab == pEnTab)
			{
				pcCompressTab = pCnTab;
			}
			else
			{
				pcCompressTab = pEnTab;
			}
			continue;
		}
		else if (0x0A == cCompressByte)
		{
			// 结束了
			break;
		}
		else
		{
			// 保存解码数据
			if (dwDataLen < wOutDataLen)
			{
				*pcDataTmp = cCompressByte;
				pcDataTmp++;
			}
			dwDataLen++;
			continue;
		}
	}
//	printf("unzip end\n");

	return dwDataLen;
}

