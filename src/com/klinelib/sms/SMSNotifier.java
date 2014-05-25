package com.klinelib.sms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.klinelib.Constants;
import com.sms.SMSUtil;

public class SMSNotifier {
	public final static String SMS_STOCK_IDS = "SMSStock";

	public static List read() {
		List result = new ArrayList();
		String file = Constants.DATA_ROOT + SMS_STOCK_IDS + ".txt";
		String s = null;
		StringBuffer sb = new StringBuffer();
		File f = new File(file);
		if (f.exists()) {
			System.out.println("该文件存在");
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(f)));
				while ((s = br.readLine()) != null && !s.trim().equals("")) {
					if (s.indexOf("#") == 0) {
						continue;
					}
					StringTokenizer st = new StringTokenizer(s, "&");
					SMSStockBean stockBean = new SMSStockBean();
					stockBean.setStockId(st.nextToken().trim());
					stockBean.setStockName(st.nextToken().trim());
					stockBean.setZhangFu(Double.parseDouble(st.nextToken()
							.trim()));
					stockBean.setDieFu(Double
							.parseDouble(st.nextToken().trim()));
					stockBean.setZhangFuZeng(Double.parseDouble(st.nextToken()
							.trim()));
					stockBean.setDieFuZeng(Double.parseDouble(st.nextToken()
							.trim()));
					stockBean.setZhangTargetPrice(Double.parseDouble(st
							.nextToken().trim()));
					stockBean.setDieTargetPrice(Double.parseDouble(st
							.nextToken().trim()));
					stockBean.setZhangTargetPriceZeng(Double.parseDouble(st
							.nextToken().trim()));
					stockBean.setDieTargetPriceZeng(Double.parseDouble(st
							.nextToken().trim()));
					stockBean.setStockCharacteristics(st.nextToken().trim());

					if (st.hasMoreTokens()) {
						stockBean.setZS(Boolean.parseBoolean(st.nextToken()
								.trim()));
					}
					result.add(stockBean);
					// System.out.println(stockBean);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("该文件不存在!");
		}
		return result;
	}

	public static void main(String[] args) {
		List result = read();
		try {
			HttpClient client = new HttpClient();
			HttpMethod method = null;
			boolean isMarkOpen = isMarketOpen();
			//boolean isMarkOpen = true;
			while (isMarkOpen) {
				if (isNoonRest()) {
					System.out.println("noon rest");
					Thread.currentThread().sleep(5 * 1000);
				} else {
					System.out.println("running");
					for (int i = 0; i < result.size(); i++) {
						SMSStockBean stockBean = (SMSStockBean) result.get(i);
						String line = queryStock(stockBean, client, method);
						sendWarning(line, stockBean);
					}
				}
				Thread.currentThread().sleep(2 * 1000);
			}
			if (method != null) {
				method.releaseConnection();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main1(String[] args) {
		try {
			List result = read();
			SMSStockBean stockBean = (SMSStockBean) result.get(0);
			String[] lines = {
					"'cn_002206','海 利 得','19.72','-0.8%','-0.28',''",
					"'cn_002206','海 利 得','19.72','-1.40%','-0.28',''",
					"'cn_002206','海 利 得','19.72','-2.40%','-0.28',''",
					"'cn_002206','海 利 得','19.72','-3.40%','-0.28',''",
					"'cn_002206','海 利 得','19.72','-4.40%','-0.28',''",
					"'cn_002206','海 利 得','19.72','-5.40%','-0.28',''",
					"'cn_002206','海 利 得','19.72','-2.40%','-0.28',''",
					"'cn_002206','海 利 得','19.72','-1.40%','-0.28',''",
					"'cn_002206','海 利 得','19.72','1.40%','-0.28',''",
					"'cn_002206','海 利 得','19.72','2.40%','-0.28',''",
					"'cn_002206','海 利 得','19.72','3.40%','-0.28',''",
					"'cn_002206','海 利 得','19.72','4.40%','-0.28',''",
					"'cn_002206','海 利 得','19.72','5.40%','-0.28',''",
					"'cn_002206','海 利 得','19.72','1.40%','-0.28',''",
					"'cn_002206','海 利 得','20.72','1.40%','-0.28',''",
					"'cn_002206','海 利 得','21.52','1.40%','-0.28',''",
					"'cn_002206','海 利 得','21.72','1.40%','-0.28',''",
					"'cn_002206','海 利 得','21.92','1.40%','-0.28',''",
					"'cn_002206','海 利 得','22.72','1.40%','-0.28',''",
					"'cn_002206','海 利 得','19.72','5.40%','-0.28',''",
					"'cn_002206','海 利 得','18.72','5.40%','-0.28',''",
					"'cn_002206','海 利 得','18.32','5.40%','-0.28',''",
					"'cn_002206','海 利 得','17.72','5.40%','-0.28',''",
					"'cn_002206','海 利 得','17.32','5.40%','-0.28',''",
					"'cn_002206','海 利 得','16.72','5.40%','-0.28',''",
					"'cn_002206','海 利 得','16.42','5.40%','-0.28',''",
					"'cn_002206','海 利 得','15.72','5.40%','-0.28',''",
					"'cn_002206','海 利 得','19.72','5.40%','-0.28',''",
					};
			for (int i = 0; i < lines.length; i++) {
				sendWarning(lines[i], stockBean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void sendWarning(String line, SMSStockBean stockBean)
			throws Exception {
		//System.out.println("line = " + line);
		StringTokenizer st = new StringTokenizer(line, ",");
		st.nextToken();
		st.nextToken();
		String scurrentP = st.nextToken();
		if(scurrentP.indexOf("0") == 0){
			return;
		}
		double currentP = Double.parseDouble(scurrentP.substring(1, scurrentP
				.length() - 1));
		
		if(currentP == 0.0){
			return;
		}
		if(currentP == 18.72){
			System.out.println("currentP" + currentP);
		}
		String sZdf = st.nextToken();
		sZdf = sZdf.substring(1, sZdf.length() - 2);
		double dZdf = Double.parseDouble(sZdf);

		boolean isCausedZF = dZdf >= stockBean.getZhangFu()
				&& stockBean.getZhangFu() != 0;
		boolean isCausedDF = dZdf <= stockBean.getDieFu()
				&& stockBean.getDieFu() != 0;
		boolean isCausedZhangTargetPrice = currentP >= stockBean
				.getZhangTargetPrice()
				&& stockBean.getZhangTargetPrice() != 0;
		boolean isCausedDieTargetPrice = currentP <= stockBean
				.getDieTargetPrice()
				&& stockBean.getDieTargetPrice() != 0;
		if (isCausedZF || isCausedDF || isCausedZhangTargetPrice
				|| isCausedDieTargetPrice) {
			String cause = "";

			if (isCausedZF) {
				stockBean.setZhangFu(stockBean.getZhangFu()
						+ stockBean.getZhangFuZeng());
				cause = "涨到目标涨幅";
			}
			if (isCausedDF) {
				stockBean.setDieFu(stockBean.getDieFu()
						- stockBean.getDieFuZeng());
				cause = "跌到目标跌幅";
			}
			if (isCausedZhangTargetPrice) {
				stockBean.setZhangTargetPrice(stockBean.getZhangTargetPrice()
						+ stockBean.getZhangTargetPriceZeng());
				cause = "涨到目标价";
			}
			if (isCausedDieTargetPrice) {
				stockBean.setDieTargetPrice(stockBean.getDieTargetPrice()
						- stockBean.getDieTargetPriceZeng());
				cause = "跌到目标价";
			}

			String message = stockBean.getStockId() + " "
					+ stockBean.getStockName() + " " + dZdf + "%" + " "
					+ currentP + " 原因: " + cause + " 股票特征:"
					+ stockBean.getStockCharacteristics();
			;
			//System.out.println(" message = " + message);
			SMSUtil.sendSM(message, "13814838918");
		}
	}

	private static boolean isMarketOpen() {
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		calendar.set(Calendar.HOUR_OF_DAY, 9);
		calendar.set(Calendar.MINUTE, 30);
		calendar.set(Calendar.SECOND, 0);
		Date morningStart = calendar.getTime();
		calendar.set(Calendar.HOUR_OF_DAY, 15);
		calendar.set(Calendar.MINUTE, 0);
		Date afterEnd = calendar.getTime();
		boolean runing = now.after(morningStart) && now.before(afterEnd);
		return runing;
	}

	private static boolean isNoonRest() {
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		calendar.set(Calendar.MINUTE, 30);
		calendar.set(Calendar.HOUR_OF_DAY, 11);
		calendar.set(Calendar.SECOND, 0);
		Date morningEnd = calendar.getTime();
		calendar.set(Calendar.HOUR_OF_DAY, 13);
		calendar.set(Calendar.MINUTE, 0);
		Date afterStart = calendar.getTime();

		boolean runing = now.after(morningEnd) && now.before(afterStart);
		return runing;
	}

	private static String queryStock(SMSStockBean stockBean, HttpClient client,
			HttpMethod method) throws Exception {
		String stockID = stockBean.getStockId();
		String postStockID = stockID.substring(3);
		String type = "cn";
		if (stockBean.isZS()) {
			type = "zs";
		}
		String url = "http://hq.stock.sohu.com/" + type + "/" + postStockID
				+ "/" + type + "_" + stockID + "-1.html";

		//System.out.println("URL = " + url);
		method = new GetMethod(url);
		client.executeMethod(method);
		// System.out.println(method.getStatusLine());
		InputStream in = method.getResponseBodyAsStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = br.readLine();
		line = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
		line = line.substring(line.indexOf("[") + 1);
		return line;
	}
}
