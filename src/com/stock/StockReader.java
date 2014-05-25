package com.stock;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

public class StockReader {
	private static HttpClient client = new HttpClient();
	private static HttpMethod method = null;

	public static void main(String[] args) {
		try {
			String stockID = "600016";
			StockHangQinBean stockHangQin = getStockHangQinFromInternet(stockID, false);
			System.out.println("stockInfo = " + stockHangQin);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static StockHangQinBean getStockHangQinFromInternet(String stockID,
			boolean isSZ) throws Exception {
		String stockHangQinInfo = getStockHangQinInfoFromSohu(stockID, false);
		StockHangQinBean result = parseStockHangQin(stockHangQinInfo);
//		StockHangQinBean result = mockStockHangQinBean();
		return result;
	}
	
	private static StockHangQinBean mockStockHangQinBean(){
		StockHangQinBean result = new StockHangQinBean();
		result.setStockId("600036");
		result.setName("招商银行");
		result.setCurrentPrice(12);
		result.setZde(0.53);
		result.setZdf(0.053);
		return result;
	}

	private static String getStockHangQinInfoFromSohu(String stockID,
			boolean isSZ) throws Exception {
		String postStockID = stockID.substring(3);
		String url = getStockDetailPageURL(stockID, isSZ, postStockID);
		method = new GetMethod(url);
		client.executeMethod(method);
		InputStream in = method.getResponseBodyAsStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = br.readLine();
		line = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
		line = line.substring(line.indexOf("[") + 2);
		return line;
	}

	private static String getStockDetailPageURL(String stockID, boolean isSZ,
			String postStockID) {
		String type = "cn";
		if (isSZ) {
			type = "zs";
		}
		String url = "http://hq.stock.sohu.com/" + type + "/" + postStockID
				+ "/" + type + "_" + stockID + "-1.html";
		return url;
	}

	private final static int STOCKID_POSITION = 0;
	private final static int NAME_POSITION = 1;
	private final static int CURRENT_PRICE_POSITION = 2;
	private final static int ZDF_POSITION = 3;
	private final static int ZDE_POSITION = 4;

	/**
	 * Parse stock information from the String which format like
	 * [cn_600036','招商银行','12.52','-2.03%','-0.26','']
	 * 
	 * @param stockInfo
	 * @return
	 */
	private static StockHangQinBean parseStockHangQin(String stockInfo)
			throws StockHanQinParseException {
		try {
			StockHangQinBean result = new StockHangQinBean();
			Pattern pattern = Pattern.compile("[',]+");
			String[] stockInfoPieces = pattern.split(stockInfo);
			result.setStockId(stockInfoPieces[STOCKID_POSITION].substring(3));
			result.setName(stockInfoPieces[NAME_POSITION]);
			result.setCurrentPrice(Double
					.parseDouble(stockInfoPieces[CURRENT_PRICE_POSITION]));
			result.setZdf(Double.parseDouble(stockInfoPieces[ZDF_POSITION]
					.substring(0, stockInfoPieces[ZDF_POSITION].length() - 1)));
			result.setZde(Double.parseDouble(stockInfoPieces[ZDE_POSITION]));
			return result;
		} catch (Exception ex) {
			System.out.println("stockInfo = " + stockInfo);
			ex.printStackTrace();
			throw new StockHanQinParseException(ex, stockInfo);
		}
	}

}
