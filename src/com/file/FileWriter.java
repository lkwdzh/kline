package com.file;

import java.io.File;

public class FileWriter {

	public static void write(String file, String[] lines) {
		File indicatorFile = new File(file);
		try {
			java.io.FileWriter fileWriter = new java.io.FileWriter(
					indicatorFile);
			for (int i = 0; i < lines.length; i++) {
				fileWriter.write(lines[i] + "\r\n");
			}
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
