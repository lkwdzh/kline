package com.klinelib;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DataCollector {
	public static void main(String[] args) {
		String flag = Constants.FLAG;
		//String flag = "SSBK";
		if (args.length > 0) {
			flag = args[0].toUpperCase();
		}
		List stockSSID = StockIDReader.read(flag);
		// stockSSID.add("000001");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String today = null;
			try {
				today = sdf.format(new Date());
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
			File file = new File(Constants.DATA_ROOT + today + "\\" + flag);
			if (!file.exists()) {
				file.mkdirs();
			}
			String runningID = "";
			String idFlag = flag;
			if(flag.equals("SSBK")){
				idFlag = "SS";
			}
			if(flag.equals("SZBK")){
				idFlag = "SZ";
			}
			
			for (int i = 0; i < stockSSID.size(); i++) {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
