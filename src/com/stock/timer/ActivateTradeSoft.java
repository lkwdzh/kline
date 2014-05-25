package com.stock.timer;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.auto.StockAutoTrade;


public class ActivateTradeSoft implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			StockAutoTrade.activateTradeSoft();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args){
		
	}

}
