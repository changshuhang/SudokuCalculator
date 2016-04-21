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
	CUnit m_lattice[9][9];			//表示数独格子的矩阵

	void getStartPos(int i, int j, int &s_i, int &s_j);		//获取i, j所在九宫格的初始坐标
	int BitCount(int bit);			//计算二进制数有多少个1
	void updataFill(int i, int j);	//更新指定格子的可填的数
};

