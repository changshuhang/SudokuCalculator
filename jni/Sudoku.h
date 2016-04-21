#pragma once

#include <stdlib.h>
#include "Unit.h"
#include "Dateself.cpp"

#include <jni.h>
#include <android/log.h>

class CSudoku {
public:
	CSudoku() {
	}
	CSudoku(int lattice[9][9]);
	~CSudoku() {
	}

	int getDigit(int i, int j);

	bool StartCount();

private:
	enum Flag {
		normal = 0, notonly, nosln
	};

private:
	CUnit m_lattice[9][9];			//��ʾ�������ӵľ���

	void getStartPos(int i, int j, int &s_i, int &s_j);		//��ȡi, j���ھŹ���ĳ�ʼ����
	int BitCount(int bit);			//������������ж��ٸ�1
	void updataFill(int i, int j);	//����ָ�����ӵĿ������
};

