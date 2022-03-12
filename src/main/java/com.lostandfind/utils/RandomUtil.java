package com.lostandfind.utils;

public class RandomUtil {
	public static String getRandom() {
		String[] letters = new String[] {"0","1","2","3","4","5","6","7","8","9"};
		String code ="";
		for (int i = 0; i < 6; i++) {
			code = code + letters[(int)Math.floor(Math.random()*letters.length)];
		}
		return code;
	}
}
