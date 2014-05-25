package com.stock.timer;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.stock.InvestmentAccountDataHolder;


public class CloseStockMarket implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			InvestmentAccountDataHolder.getInstance().closeStockMarket();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args){
		
	}

}
