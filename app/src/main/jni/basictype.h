/*************************************************************************/
/*                                                                       */
/*        Copyright (c) 2004-2005 NOAY Mobile department.                */
/*                                                                       */
/* PROPRIETARY RIGHTS of NOAY Mobile department are involved in the      */
/* subject matter of this material.  All manufacturing, reproduction,    */
/* use, and sales rights pertaining to this subject matter are governed  */
/* by the license agreement.  The recipient of this software implicitly  */
/* accepts the terms of the license.                                     */
/*                                                                       */
/*************************************************************************/

/*************************************************************************/
/*                                                                       */
/* FILE NAME                                            VERSION          */
/*                                                                       */
/*      BasicType.h                                     NM 1.00          */
/*                                                                       */
/*                                                                       */
/* DESCRIPTION                                                           */
/*                                                                       */
/*      This file contains the basic type define be used in H.223 		 */
/*      stack system.  													 */
/*     									                                 */                                                                      
/* AUTHOR                                                                */
/*                                                                       */
/*      liuyaoxue, NOAY Mobile department.                               */
/*                                                                       */
/* DATA STRUCTURES                                                       */
/*                                                                       */
/*                                                                       */
/* FUNCTIONS                                                             */
/*                                                                       */
/*	    None										                     */
/*                                                                       */
/* DEPENDENCIES                                                          */
/*      None                                                             */
/*                                                                       */
/* HISTORY                                                               */
/*                                                                       */
/*         NAME            DATE                    REMARKS               */
/*			liuyaoxue	03-11-2006      Created initial version 1.0      */
/*			maliang		12-21-2006      redefined BOOL TRUE FALSE	     */
/*			maliang		12-21-2006      added a macro for dllexport	     */
/*                                                                       */
/*                                                                       */
/*************************************************************************/
#ifndef _BASICTYPE_H_
#define _BASICTYPE_H_

/* Win32 uses DLL by default; it needs special stuff for exported functions. */
#ifdef _WIN32
#  ifdef DLL
#    define DLLAPI __declspec(dllexport)
#  else
#    define DLLAPI extern __declspec(dllimport)
#  endif
#endif

/* For other operating systems, we use the standard "extern". */
#ifndef DLLAPI
#  ifdef __cplusplus
#    define DLLAPI     extern "C"
#  else
#    define DLLAPI     extern
#  endif
#endif

#ifndef PACKED
#	ifndef _WIN32
#		define PACKED __attribute__((packed))
#	else
#		define PACKED
#	endif
#endif

#ifndef NULL
#define NULL                 (0)
#endif

#ifndef uchar
#define uchar unsigned char
#endif

#ifndef ushort
#define ushort unsigned short
#endif

#ifndef uint
#define uint unsigned int
#endif

#ifndef BOOL
#define BOOL unsigned char
#endif

#ifndef TRUE
#define TRUE		1
#endif

#ifndef FALSE
#define FALSE		0
#endif

#ifndef UINT_MAX
#define UINT_MAX	0xFFFFFFFF
#endif

#ifndef INVALID_ADDR
#define INVALID_ADDR 0xFFFFFFFF
#endif

#if 1
#ifndef FREE
#define FREE(p)  if(p){free(p); p=NULL;}
#endif

#define MALLOC  malloc

#else
#ifndef FREE
#define FREE(p)  if(p){_free(p); p=NULL;}
#endif

#define MALLOC  _malloc

void _free(void * ptr);
void * _malloc(size_t size);
#endif

#endif
