#include <jni.h>
#include <android/log.h>
#include <stdio.h>

#include "Unit.cpp"
#include "Sudoku.cpp"

using namespace std;

//¼ÆËãÊý¾Ý
// JNI interface functions, be careful about the naming.
extern "C" {
jintArray Java_com_csh_cn_calculator_CalculatorC_calculate(JNIEnv* env,
		jobject thiz, jintArray oldArray) {

	jint data[81];
	jintArray javaArray;
	int test[9][9];

	env->GetIntArrayRegion(oldArray, 0, 81, data);
	for (int i = 0; i < 81; i++) {
		test[i / 9][i % 9] = data[i];
	}

	CSudoku sudo(test);
	if (!sudo.StartCount())
		LOGE("%s", "shu du wu jie!");
	for (int i = 0; i < 9; i++) {
		for (int j = 0; j < 9; j++) {
			data[i * 9 + j] = sudo.getDigit(i, j);
		}
	}

	javaArray = env->NewIntArray(81);
	env->SetIntArrayRegion(javaArray, 0, 81, data);

	return javaArray;
}

}
;
