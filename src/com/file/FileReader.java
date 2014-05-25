package com.file;

public interface FileReader {
	void setFile(String file);
	void setLineParser(LineParser lineParser);
	Object[] readLinesToObjects();
}
