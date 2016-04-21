#pragma once

#include <jni.h>
#include <android/log.h>

#define LENGTH sizeof(struct CalculateData1)
#define LOG_TAG    "com.csh.wuziqi from C"
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#define NO_FILL			-1
#define ALL_FILL		-2

#define DIGITAL_ERROR	101
#define BIN_ERROR		102
#define POS_ERROR		103

int GetBin(int digital);
int GetDigital(int bin);

class CUnit
{
public:
	int m_fill;			//表示可填的数，是一个9位二进制数
	int m_dig;			//已填的数，0表示什么都没填

	CUnit();

	CUnit &operator =(int dig);
	bool operator ==(int dig);
	bool operator !=(int dig);
};
