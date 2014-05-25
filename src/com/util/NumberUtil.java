package com.util;

public class NumberUtil {
	public static double roundDoubleWith2Numbers(double value) {
		return roundDoubleWithCertainNumbers(value, 2);
	}

	public static double roundDoubleWith4Numbers(double value) {

		return roundDoubleWithCertainNumbers(value, 4);
	}

	private static double roundDoubleWithCertainNumbers(double value,
			int numberCount) {
		int ten = 1;
		for (int i = 0; i < numberCount; i++) {
			ten *= 10;
		}
		double d1 = value;
		d1 = (Math.round(d1 * ten));
		value = d1 / ten;
		return value;
	}

}
