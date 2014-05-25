package com.klinelib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StockIDReader {
	public static List read(String flag) {
		List result = new ArrayList();
		String file = Constants.DATA_ROOT + flag + ".txt";
		String s = null;
		StringBuffer sb = new StringBuffer();
		File f = new File(file);
		if (f.exists()) {
			System.out.println("该文件存在");
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(f)));
				while ((s = br.readLine()) != null && !s.trim().equals("")) {
					String id = s.substring(0, s.indexOf(" "));
					result.add(id);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("该文件不存在!");
		}
		return result;
	}

	public static List read(String flag, int first, int size) {
		List result = new ArrayList();
		String file = Constants.DATA_ROOT + flag + ".txt";
		String s = null;
		StringBuffer sb = new StringBuffer();
		File f = new File(file);
		if (f.exists()) {
			System.out.println("该文件存在");
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(f)));
				int i = 0;
				while ((s = br.readLine()) != null) {
					i++;
					if (i > first + size) {
						break;
					}
					if (i >= first) {
						String id = s.substring(0, s.indexOf(" "));
						System.out.println(id);
						result.add(id);
					}

				}
				System.out.println(sb);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("该文件不存在!");
		}
		return result;
	}

	public static List read(String flag, int size) {
		List result = new ArrayList();
		String file = Constants.DATA_ROOT + flag + ".txt";
		String s = null;
		StringBuffer sb = new StringBuffer();
		File f = new File(file);
		if (f.exists()) {
			System.out.println("该文件存在");
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(f)));
				int i = 0;
				while ((s = br.readLine()) != null) {
					i++;
					if (i > size) {
						break;
					}
					String id = s.substring(0, s.indexOf(" "));
					System.out.println(id);
					result.add(id);
				}
				System.out.println(sb);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("该文件不存在!");
		}
		return result;
	}
}
