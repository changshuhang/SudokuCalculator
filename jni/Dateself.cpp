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

//����һ��ȫ�ֵ��������������ָ��
struct CalculateData *head = NULL;
struct CalculateData *last = NULL;

//���ݵĳ���
int dateLength = 0;

#endif

/*
 1.��ջ����s.push(x);
 2.��ջ:�� s.pop().ע�⣺��ջ����ֻ��ɾ��ջ����Ԫ�أ��������ظ�Ԫ�ء�
 3.����ջ������s.top();
 4.�ж�ջ�գ���s.empty().��ջ��ʱ����true��
 5.����ջ�е�Ԫ�ظ�������s.size����;
 */

void printAll(void);
struct CalculateData *get(int num);

/*
 ===========================
 ���ܣ�����һ���µ�
 ���أ�CalculateData *
 ===========================
 */
struct CalculateData *getNew(CUnit lattice[9][9], int m_i, int m_j, int digit) {
	struct CalculateData *jiedian; //�ڵ�ָ��

	jiedian = (struct CalculateData *) malloc(LEN);   //����һ���½ڵ�
	if (jiedian == NULL)        //�ڵ㿪�ٲ��ɹ�
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
 ���ܣ��ж��Ƿ�ΪNULL
 ���أ�bool
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
 ���ܣ���ӽڵ�
 ���أ�void
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

	//���ݳ���+1
	dateLength++;

	LOGI("%s,number=%d", "add date success!", dateLength);
}

/*
 ===========================
 ���ܣ������������
 ���أ�void
 ===========================
 */
void clear(void) {
	head = NULL;
	last = NULL;
}

/*
 ===========================
 ���ܣ������������ݵĳ���
 ���أ�int
 ===========================
 */
int size() {
	return dateLength;
}

/*
 ==========================
 ���ܣ�ɾ����ɵ�����
 ���أ�void
 ==========================
 */
void pop() {
	struct CalculateData *p1;     //p1���浱ǰ��Ҫ���Ľڵ�ĵ�ַ
	struct CalculateData *p2;     //p1���浱ǰ��Ҫ���Ľڵ�ĵ�ַ
	if (head == NULL)       	  //�ǿ��������ͼ3��⣩
	{
		LOGI("%s", "List is null!");
	} else {
		p2=last;
		p1 = last->pre;
		last = p1;
		LOGI("%s", "Dateself2!");
		free(p2);      		 	//�ͷŵ�ǰ�ڵ�
		LOGI("%s", "Dateself3!");
		p2 = NULL;
		dateLength -= 1;        //�ڵ�������1��

		if(dateLength==0){
			last=NULL;
			head=NULL;
		}
	}

	LOGI("%s,number=%d", "delete success!", dateLength);
}

/*
 ==========================
 ���ܣ�������ɵ�����
 ���أ�CalculateData
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
 ���ܣ�����ڵ�
 ���أ� void
 ===========================
 */
void printAll(void) {
	struct CalculateData *p;
	p = head;

	LOGI("init success----> %s", "da yin shu ju!");
	if (head != NULL)        //ֻҪ���ǿ�������������������нڵ�
			{
		do {
			/*
			 �����Ӧ��ֵ����ǰ�ڵ��ַ�����ֶ�ֵ����ǰ�ڵ����һ�ڵ��ַ��
			 ����������ڶ������󿴵�һ�����������ڼ�����еĴ洢�ṹ��������
			 ��Ƶ�ͼʾ��һģһ���ġ�
			 */
			LOGI("init success----> x=%d,y=%d,value=%d", p->m_i, p->m_j,
					p->m_digit); //���ͷָ��ָ��ĵ�ַ
			p = p->next;     //�Ƶ���һ���ڵ�
		} while (p != NULL);
	}
}
