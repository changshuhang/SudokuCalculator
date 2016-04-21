#include <jni.h>
#include <android/log.h>

#include <stdlib.h>
#include <stdio.h>

#include "Unit.h"

#ifndef _DATESELF_H_
#define _DATESELF_H_

#define LEN sizeof(struct CalculateData)

struct CalculateData {
	CUnit m_lattice[9][9];
	int m_i;
	int m_j;
	int m_digit;
	struct CalculateData *next;
	struct CalculateData *pre;
};

//创建一个全局的引导进入链表的指针
struct CalculateData *head = NULL;
struct CalculateData *last = NULL;

//数据的长度
int dateLength = 0;

#endif

/*
 1.入栈：如s.push(x);
 2.出栈:如 s.pop().注意：出栈操作只是删除栈顶的元素，并不返回该元素。
 3.访问栈顶：如s.top();
 4.判断栈空：如s.empty().当栈空时返回true。
 5.访问栈中的元素个数，如s.size（）;
 */

void printAll(void);
struct CalculateData *get(int num);

/*
 ===========================
 功能：创建一个新的
 返回：CalculateData *
 ===========================
 */
struct CalculateData *getNew(CUnit lattice[9][9], int m_i, int m_j, int digit) {
	struct CalculateData *jiedian; //节点指针

	jiedian = (struct CalculateData *) malloc(LEN);   //开辟一个新节点
	if (jiedian == NULL)        //节点开辟不成功
			{
		LOGI("%s", "Cann't create it, try it again in a moment!");
	} else {
		LOGI("%s", "create it success!");

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				jiedian->m_lattice[i][j] = lattice[i][j];
			}
		}

		jiedian->m_i = m_i;
		jiedian->m_j = m_j;
		jiedian->m_digit = digit;
	}
	return jiedian;
}

/*
 ===========================
 功能：判断是否为NULL
 返回：bool
 ===========================
 */
bool empty() {

	if (dateLength > 0) {
		return false;
	} else {
		return true;
	}

}

/*
 ===========================
 功能：添加节点
 返回：void
 ===========================
 */
void push(struct CalculateData *node) {

	node->next = NULL;
	if (head == NULL) {
		head = node;
		head->pre = NULL;
	} else {
		last->next = node;
		node->pre = last;
	}
	last = node;

	//数据长度+1
	dateLength++;

	LOGI("%s,number=%d", "add date success!", dateLength);
}

/*
 ===========================
 功能：清空所有数据
 返回：void
 ===========================
 */
void clear(void) {
	head = NULL;
	last = NULL;
}

/*
 ===========================
 功能：返回所有数据的长度
 返回：int
 ===========================
 */
int size() {
	return dateLength;
}

/*
 ==========================
 功能：删除最顶成的数据
 返回：void
 ==========================
 */
void pop() {
	struct CalculateData *p1;     //p1保存当前需要检查的节点的地址
	struct CalculateData *p2;     //p1保存当前需要检查的节点的地址
	if (head == NULL)       	  //是空链表（结合图3理解）
	{
		LOGI("%s", "List is null!");
	} else {
		p2=last;
		p1 = last->pre;
		last = p1;
		LOGI("%s", "Dateself2!");
		free(p2);      		 	//释放当前节点
		LOGI("%s", "Dateself3!");
		p2 = NULL;
		dateLength -= 1;        //节点总数减1个

		if(dateLength==0){
			last=NULL;
			head=NULL;
		}
	}

	LOGI("%s,number=%d", "delete success!", dateLength);
}

/*
 ==========================
 功能：返回最顶成的数据
 返回：CalculateData
 ==========================
 */
struct CalculateData *top() {

	if (last == NULL) {
		LOGI("%s", "List is null!");
		return NULL;
	} else {
		return last;
	}

}

/*
 ===========================
 功能：输出节点
 返回： void
 ===========================
 */
void printAll(void) {
	struct CalculateData *p;
	p = head;

	LOGI("init success----> %s", "da yin shu ju!");
	if (head != NULL)        //只要不是空链表，就输出链表中所有节点
			{
		do {
			/*
			 输出相应的值：当前节点地址、各字段值、当前节点的下一节点地址。
			 这样输出便于读者形象看到一个单向链表在计算机中的存储结构，和我们
			 设计的图示是一模一样的。
			 */
			LOGI("init success----> x=%d,y=%d,value=%d", p->m_i, p->m_j,
					p->m_digit); //输出头指针指向的地址
			p = p->next;     //移到下一个节点
		} while (p != NULL);
	}
}
