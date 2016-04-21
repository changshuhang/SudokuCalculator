#include "Sudoku.h"

#define FLAG_SWITCH(flag) switch(flag){
#define CASE_NORMAL case CSudoku::normal:{
#define CASE_NOTONLY break;} case CSudoku::notonly:{
#define CASE_NOSLN break;} case CSudoku::nosln:{
#define END_SWITCH break; }default: break;}

class Stack {
public:
	Stack(int capacity_) :
			elements(NULL), top(0), capacity(capacity_) {
		elements = new int[capacity];
	}
	~Stack() {
		delete[] elements;
	}
	bool isFull() {
		return (top >= capacity);
	}
	void push(int val) {
		elements[top] = val;
		(top)++;
	}
	bool isEmpty() {
		return (top == 0);
	}
	int pop() {
		(top)--;
		return (elements[top]);
	}
	int size() {
		return (top);
	}
	int getCapacity() {
		return (capacity);
	}
private:
	int* elements;
	int top;
	int capacity;
};

CSudoku::CSudoku(int lattice[9][9])
{
	for (int i = 0; i < 9; i++)
	{
		for (int j = 0; j < 9; j++)
		{
			m_lattice[i][j] = lattice[i][j];
		}
	}
	for (int i = 0; i < 9; i++)
	{
		for (int j = 0; j < 9; j++)
		{
			if (m_lattice[i][j] == 0)
			{updataFill(i, j);}
		}
	}
}

void CSudoku::getStartPos(int i, int j, int &s_i, int &s_j)
{
	bool isSuccess = true;

	/**
	j 
	i| 0 1 2 | 3 4 5 | 6 7 8 |
	-+-------+-------+-------+
	0|       |       |       |
	1|   1   |   4   |   7   |
	2|       |       |       |
	-+-------+-------+-------+
	3|       |       |       |
	4|   2   |   5   |   8   |
	5|       |       |       |
	-+-------+-------+-------+
	6|       |       |       |
	7|   3   |   6   |   9   |
	8|       |       |       |
	-+-------+-------+-------+
	*/
	if ( (i >= 0 && i <= 2) && (j >= 0 && j <= 2) )					//1
	{
		s_i = 0;
		s_j = 0;
	}
	else if ( (i >= 3 && i <= 5) && (j >= 0 && j <= 2) )			//2
	{
		s_i = 3;
		s_j = 0;
	}
	else if ( (i >= 6 && i <= 8) && (j >= 0 && j <= 2) )			//3
	{
		s_i = 6;
		s_j = 0;
	}
	else if ( (i >= 0 && i <= 2) && (j >= 3 && j <= 5) )			//4
	{
		s_i = 0;
		s_j = 3;
	}
	else if ( (i >= 3 && i <= 5) && (j >= 3 && j <= 5) )			//5
	{
		s_i = 3;
		s_j = 3;
	}
	else if ( (i >= 6 && i <= 8) && (j >= 3 && j <= 5) )			//6
	{
		s_i = 6;
		s_j = 3;
	}
	else if ( (i >= 0 && i <= 2) && (j >= 6 && j <= 8) )			//7
	{
		s_i = 0;
		s_j = 6;
	}
	else if ( (i >= 3 && i <= 5) && (j >= 6 && j <= 8) )			//8
	{
		s_i = 3;
		s_j = 6;
	}
	else if ( (i >= 6 && i <= 8) && (j >= 6 && j <= 8) )			//9
	{
		s_i = 6;
		s_j = 6;
	}
	else
	{
		isSuccess = false;
	}

}

int CSudoku::BitCount(int bit)
{
	int count;
	for (count = 0; bit != 0; bit >>= 1)
	{
		count += bit & 1;
	}
	return count;
}

void CSudoku::updataFill(int i, int j)
{
	if (m_lattice[i][j] == 0)
	{
		for (int _i = 0; _i < 9; _i++)		//列
		{
			m_lattice[i][j].m_fill = m_lattice[i][j].m_fill & ~GetBin(m_lattice[_i][j].m_dig);
		}
		for (int _j = 0; _j < 9; _j++)		//行
		{
			m_lattice[i][j].m_fill = m_lattice[i][j].m_fill & ~GetBin(m_lattice[i][_j].m_dig);
		}
		/*九宫格*/
		int s_i, s_j;
		getStartPos(i, j, s_i, s_j);

		for (int _i = s_i; _i < s_i+3; _i++)
		{
			for (int _j = s_j; _j < s_j+3; _j++)
			{
				m_lattice[i][j].m_fill = m_lattice[i][j].m_fill & ~GetBin(m_lattice[_i][_j].m_dig);
			}
		}
	}
	else
	{
		m_lattice[i][j].m_fill = GetBin(NO_FILL);
	}
}

int CSudoku::getDigit(int i, int j)
{
	return m_lattice[i][j].m_dig;
}

bool CSudoku::StartCount()
{
	Flag flag = normal;

	int c_fblank = 0;			//为判断是否找到空白单元格
	int c_fsingle = 0;			//为判断是否找到唯一数单元格
	int c_fnosln = 0;			//为判断是否找到无解单元格

	do
	{
		c_fblank = 0;
		c_fsingle = 0;
		c_fnosln = 0;

		FLAG_SWITCH(flag)
		CASE_NORMAL
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				if (m_lattice[i][j] == 0)			//找空白单元格
				{
					updataFill(i, j);
					if ( BitCount(m_lattice[i][j].m_fill) == 1 )		//找唯一单元格
					{
						m_lattice[i][j] = GetDigital(m_lattice[i][j].m_fill);
						c_fsingle++;
						c_fblank--;
					}
					else if ( BitCount(m_lattice[i][j].m_fill) == 0 )	//找无解单元格
					{
						c_fnosln++;
					}
					c_fblank++;
				}
			}//end for
		}
		if (c_fblank == 0) return true;		//循环出口
		CASE_NOTONLY

		int _numfill = 999;
		int _i, _j;

		for (int i = 0; i < 9; i++)			//找到一个可填数最少的单元格
		{
			for (int j = 0; j < 9; j++)
			{
				if ( BitCount(m_lattice[i][j].m_fill) < _numfill && m_lattice[i][j] == 0 )
				{
					_numfill = BitCount(m_lattice[i][j].m_fill);
					_i = i; _j = j;
				}
			}
		}
		int _count = 1;
		int _bin = m_lattice[_i][_j].m_fill;

		while ((_bin & 1) != 1)
		{
			_bin = _bin >> 1;
			_count++;
		}
		/*缓存状态*/
		struct CalculateData *_cache= getNew(m_lattice, _i, _j, _count);
		LOGE("%s","chang shu hang1");
		push(_cache);
		/*填数*/
		m_lattice[_i][_j] = _count;

		flag = CSudoku::normal;
		continue;

		CASE_NOSLN
		do
		{
			if (empty()) return false;

			struct CalculateData *_top=top();

			for (int i = 0; i < 9; i++)
			{
				for (int j = 0; j < 9; j++)
				{
					m_lattice[i][j] = _top->m_lattice[i][j];
				}
			}
			int _count = 1;
			int _bin = m_lattice[_top->m_i][_top->m_j].m_fill;

			while ( ((_bin & 1) != 1) || (_count <= _top->m_digit) )
			{
				_bin = _bin >> 1;
				_count++;
				if (_count > 9) break; //_Debug_message(L"死循环",L"Sudoku.cpp",239);
			}
			if (_count <= 9)
			{
				/*填数并缓存*/
				struct CalculateData *_cache= getNew(m_lattice, _top->m_i, _top->m_j, _count);
				m_lattice[_top->m_i][_top->m_j] = _count;
				LOGE("%s","chang shu hang2");

				pop();
				LOGE("%s","chang shu hang3");
				push(_cache);
				LOGE("%s","chang shu hang4");
				break;			//循环出口
			}
			else
			{
				pop();
			}

		}while(true);

		flag = CSudoku::normal;
		continue;

		END_SWITCH

		if (c_fsingle == 0)
		{
			//发现无唯一数单元格
			flag = CSudoku::notonly;
		}
		if (c_fnosln != 0)
		{
			//发现无解单元格
			flag = CSudoku::nosln;
		}

	}while (true);

	return true;
}

