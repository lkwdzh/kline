package com.stock;

public class StockHanQinParseException extends Exception{
	private Exception rootException;
	private String stockInfo;
	public StockHanQinParseException(Exception rootException,String stockInfo){
		this.rootException = rootException;
		this.stockInfo = stockInfo;
	}
	
	public void printStackTrace(){
		System.out.print("StockInfo = " + stockInfo);
		rootException.printStackTrace();	
	}
}
