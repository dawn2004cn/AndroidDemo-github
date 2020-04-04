#ifndef LIST_H_
#define LIST_H_
#include <jni.h>

typedef struct _ListNode
{
	struct _ListNode *prev;
	struct _ListNode *next;
	void *data;
}ListNode;

typedef void DataFreeFunc(void*); 

#ifdef __cplusplus
extern "C"
{
#endif

ListNode *list_new(void);
ListNode *list_insert(ListNode *list, void *data);
ListNode *list_append(ListNode *list, void *data);
void 	  list_free(ListNode *list, DataFreeFunc free_func);

#ifdef __cplusplus
}
#endif
#endif /* LIST_H_ */
