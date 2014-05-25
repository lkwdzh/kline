package com.util;
public class ClassUtil {
	public static Object getInstance(String classPattern, String type) {
		String className = classPattern.replace("XXXX", type);
		Object result = null;
		try {
			Class classType= Class.forName(className);
			result = classType.newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
}
