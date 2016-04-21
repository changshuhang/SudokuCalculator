#include "Unit.h"

int GetBin(int digital)
{
	switch (digital)
	{
	case 0: return 0;	break;		//0b000000000
	case 1: return 1;	break;		//0b000000001
	case 2: return 2;	break;		//0b000000010
	case 3: return 4;	break;		//0b000000100
	case 4: return 8;	break;		//0b000001000
	case 5: return 16;	break;		//0b000010000
	case 6: return 32;	break;		//0b000100000
	case 7: return 64;	break;		//0b001000000
	case 8: return 128;	break;		//0b010000000
	case 9: return 256;	break;		//0b100000000
	case NO_FILL:
		return 0;		break;		//0b000000000，什么都不可以填
	case ALL_FILL:
		return 511;		break;		//0b111111111, 什么都可以填
	}
}

int GetDigital(int bin)
{
	switch (bin)
	{
	case 0:		return 0;	break;		//0b000000000
	case 1:		return 1;	break;		//0b000000001
	case 2:		return 2;	break;		//0b000000010
	case 4:		return 3;	break;		//0b000000100
	case 8:		return 4;	break;		//0b000001000
	case 16:	return 5;	break;		//0b000010000
	case 32:	return 6;	break;		//0b000100000
	case 64:	return 7;	break;		//0b001000000
	case 128:	return 8;	break;		//0b010000000
	case 256:	return 9;	break;		//0b100000000
	//case 0:		return NO_FILL;			//0b000000000，什么都不可以填
		break;
	}
}

CUnit::CUnit()
	: m_dig(0), m_fill(GetBin(ALL_FILL))
{}

CUnit &CUnit::operator=(int dig)
{
	m_dig = dig;
	if (dig == 0)
	{
		m_fill = GetBin(ALL_FILL);
	}
	else
	{
		m_fill = GetBin(NO_FILL);
	}
	return *this;
}

bool CUnit::operator==(int dig)
{
	return m_dig == dig;
}

bool CUnit::operator!=(int dig)
{
	return m_dig != dig;
}
