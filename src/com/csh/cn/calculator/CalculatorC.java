package com.csh.cn.calculator;

public class CalculatorC {

	public int[][] getCalcuator(int[][] olddata) {
		int old[] = new int[81];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				old[i * 9 + j] = olddata[i][j];
			}
		}
		int[] data = calculate(old);
		int value[][] = new int[9][9];
		for (int i = 0; i < 81; i++) {
			value[i / 9][i % 9] = data[i];
		}
		return value;
	}

	// ¼ÆËãÊý¾Ý
	public native int[] calculate(int[] olddata);

	static {
		System.loadLibrary("calculate");
	}

}
