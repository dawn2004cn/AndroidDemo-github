/*
 * main.c
 *
 *  Created on: 2012-4-19
 *      Author: lenovo
 */
#include <stdio.h>
#include "sync_writer.h"

#define STR(pchar) (pchar==NULL ? "NULL" : pchar)

static int test_memory = 0;

void _free(void * ptr)
{
	if(test_memory > 0) {
		test_memory--;
	} else {
		printf("ERROR FREE!!!!\n");
	}
	printf("free-test_memory:%d\n", test_memory);
	free(ptr);
}

void * _malloc(size_t size)
{
	test_memory++;
	printf("malloc-test_memory:%d\n", test_memory);
	return malloc(size);
}

int main(int argc, char ** argv)
{
	char * file = 0;
	char cmd[128] = {0};
	char param[128] = {0};
	int searchWordCount = 0;
	int entryCount = 0;

    file = "D:\\360MoveData\\Users\\23061\\Desktop\\项目问题点\\人格健康教育.bin";
    T_SyncWriterHandler * handler = SWT_Init(file);


	return 0;
}




