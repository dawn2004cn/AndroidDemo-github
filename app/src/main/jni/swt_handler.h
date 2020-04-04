/*
 * single_handler.h
 *
 *  Created on: 2012-5-16
 *      Author: lenovo
 */

#ifndef SINGLE_HANDLER_H_
#define SINGLE_HANDLER_H_
#include "sync_writer.h"

class Swt_Handler{
public:
	int hd;
	T_SyncWriterHandler * t_handler;

	bool isValid();

	static Swt_Handler * getHandler(const char * filepath);
	static Swt_Handler * findHandler(int hd);
	static void releaseHandler(Swt_Handler * handler);
	static void clearHandler();

private:
	Swt_Handler(const char * filepath) ;
	~Swt_Handler();
};


#endif /* SINGLE_HANDLER_H_ */
