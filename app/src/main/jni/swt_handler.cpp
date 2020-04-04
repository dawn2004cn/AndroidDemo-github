/*
 * handler.cpp
 *
 *  Created on: 2012-5-16
 *      Author: lenovo
 */
#include <unistd.h>
#include "swt_handler.h"
#include "sync_writer.h"
#include "log.h"

static const int MAX_OPEN_BOOK = 1;
static int sDescripter = 10;
static Swt_Handler * s_Handler[MAX_OPEN_BOOK] = { 0 };

Swt_Handler::Swt_Handler(const char * filepath) {
	if(filepath != NULL) {
		t_handler = SWT_Init(filepath);
	}
}

Swt_Handler::~Swt_Handler() {
	LOGD("Close person health book in handler %d\n", hd);
	if(t_handler != NULL) {
		SWT_Close(t_handler);
	}
}

bool Swt_Handler::isValid() {
	return t_handler != NULL;
}

Swt_Handler * Swt_Handler::getHandler(const char * filepath)
{
	// 先判断书本是否已经打开
	LOGD("Swt_Handler::getHandler %s %d",filepath,__LINE__);
	for(int i=0; i<MAX_OPEN_BOOK; i++) {
		if(s_Handler[i] != NULL) {
			if(filepath != NULL && s_Handler[i]->t_handler != NULL
					&& s_Handler[i]->t_handler->file != NULL
					&& strcmp(filepath, s_Handler[i]->t_handler->file) == 0) {
				return s_Handler[i];
			}
		}
	}

	LOGD("Swt_Handler::getHandler %s %d",filepath,__LINE__);
	// 没有打开则打开书本
	for(int i=0; i<MAX_OPEN_BOOK; i++) {
		if(s_Handler[i] == NULL) {
			s_Handler[i] = new Swt_Handler(filepath);
			s_Handler[i]->hd = sDescripter++;
			LOGD("Open book in handler %d\n", s_Handler[i]->hd);
			return s_Handler[i];
		}
	}

	LOGD("You open too many books. The limit is %d\n", MAX_OPEN_BOOK);
	return NULL;
}

Swt_Handler * Swt_Handler::findHandler(int hd)
{
	for(int i=0; i<MAX_OPEN_BOOK; i++) {
		if(s_Handler[i] != NULL && s_Handler[i]->hd == hd) {
			return s_Handler[i];
		}
	}

	return NULL;
}

void Swt_Handler::releaseHandler(Swt_Handler * handler)
{
	if(handler != NULL) {
		for(int i=0; i<MAX_OPEN_BOOK; i++) {
			if(s_Handler[i] == handler) {
				delete s_Handler[i];
				s_Handler[i] = NULL;
			}
		}
	}
}

void Swt_Handler::clearHandler()
{
	for(int i=0; i<MAX_OPEN_BOOK; i++) {
		if(s_Handler[i] != NULL) {
			delete s_Handler[i];
			s_Handler[i] = NULL;
		}
	}
}






