#include <malloc.h>
#include "basictype.h"
#include "list.h"

ListNode *list_new(void)
{
	ListNode *list = (ListNode *)MALLOC(sizeof(ListNode));
	list->prev = NULL;
	list->next = NULL;
	list->data = NULL;
	return list;
}

ListNode *list_insert(ListNode *head, void *data)
{
	ListNode *node = (ListNode *)MALLOC(sizeof(ListNode));
	node->data = data;
	node->next = head;
	node->prev = NULL;
	if (head != NULL)
		head->prev = node;
	return node;
}

ListNode *list_append(ListNode *head, void *data)
{
	ListNode *tmp = head;
	ListNode *node = (ListNode *)MALLOC(sizeof(ListNode));
	node->data = data;
	while(tmp->next != NULL)
	{
		tmp = tmp->next;
	}
	tmp->next = node;
	node->prev = tmp;
	node->next = NULL;
	return head;
}

void 	  list_free(ListNode *head, DataFreeFunc free_func)
{
	ListNode *tmp = head;
	while(tmp != NULL)
	{
		if (free_func != NULL &&
		    tmp->data != NULL)
		    free_func(tmp->data); 
		head = tmp->next;
		FREE(tmp);
		tmp = head;
	}
}

