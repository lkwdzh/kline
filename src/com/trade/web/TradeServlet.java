package com.trade.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auto.StockAutoTrade;

public class TradeServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		trade(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		trade(request, response);
	}

	public void trade(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String stockID = request.getParameter("stockid");
		String sCount = request.getParameter("count");
		String direction = request.getParameter("group2");
		PrintWriter out = response.getWriter();
		if (stockID == null || sCount == null || "".equals(stockID)
				|| "".equals(sCount)) {
			out.print("parameter is blank");
			return;
		}
		int count = 0;
		try {
			count = Integer.parseInt(sCount);
		} catch (Exception ex) {
			out.println("number is invalid");
			return;
		}
		TradeJob job = new TradeJob(stockID, count, direction);
		Thread worker = new Thread(job);
		worker.start();
		out.println("buy stock successful");

	}
	
	
	 class TradeJob implements Runnable{
			private String stockID;
			private int count;
			private String direction;
			public TradeJob(String stockID, int count, String direction){
				this.stockID = stockID;
				this.count = count;
				this.direction = direction;
			}
			public void run(){ 
				if("in".equals(direction)){
					StockAutoTrade.buyStock(stockID, count);
				}else{
					StockAutoTrade.sellStock(stockID, count);
				}
			}
		}
}


