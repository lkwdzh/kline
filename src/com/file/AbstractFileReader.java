package com.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFileReader implements FileReader {
	private String file;
	private LineParser lineParser;
	public AbstractFileReader(String file,LineParser lineParser){
		this.file = file;
		this.lineParser = lineParser;
	}
	
	public void setFile(String file) {
		this.file = file;
	}

	public void setLineParser(LineParser lineParser) {
		this.lineParser = lineParser;
	}
	
	public Object[] readLinesToObjects() {
		List list = new ArrayList();
		String line = null;
		StringBuffer sb = new StringBuffer();
		File monitorStockFile = new File(file);
		if (monitorStockFile.exists()) {
			System.out.println("该文件存在");
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(monitorStockFile)));
				while ((line = br.readLine()) != null
						&& !line.trim().equals("")) {
					if (line.indexOf("#") == 0) {
						continue;
					} else {
						Object object = lineParser.parseLine(line);
						list.add(object);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("该文件不存在!");
		}
		return list.toArray();
	}
}
