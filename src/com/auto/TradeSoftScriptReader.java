package com.auto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.klinelib.Constants;

public class TradeSoftScriptReader {
	private Map scriptMap = new HashMap();

	private static TradeSoftScriptReader instance = new TradeSoftScriptReader();

	public static TradeSoftScriptReader getInstance() {
		return instance;
	}

	private TradeSoftScriptReader() {
		String filePath = Constants.DATA_ROOT
				+ Constants.TRADE_SOFT_SCRIPT_FILE;
		String line = null;
		File tradeSoftScriptFile = new File(filePath);
		if (tradeSoftScriptFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(tradeSoftScriptFile)));
				List scriptList = null;
				while ((line = br.readLine()) != null
						&& !line.trim().equals("")) {
					if (line.indexOf("###") == 0) {
						scriptList = new ArrayList();
						String cmdName = line.substring(3).trim();
						scriptMap.put(cmdName, scriptList);
					} else {
						scriptList.add(line);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("交易软件脚本文件 : " + filePath + "不存在!");
		}
	}
	
	public List getCMDScripts(String cmdName){
		return (List)scriptMap.get(cmdName);
	}
}
