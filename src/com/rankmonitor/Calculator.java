package com.rankmonitor;

public class Calculator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double lowestPrice = 6.88;
		double highestPrice = 9.44;
		int roundNumber = 0;
		double everyRoundPrice = highestPrice;
		System.out.println("everyRoundPrice = " + everyRoundPrice);
		while (everyRoundPrice >= lowestPrice) {
			everyRoundPrice = (everyRoundPrice) * 0.95;
			everyRoundPrice = round2Numbers(everyRoundPrice);
			System.out.println("everyRoundPrice = " + everyRoundPrice);
			roundNumber++;
		}
		System.out.println("roundNumber = " + roundNumber);

		roundNumber = 0;
		everyRoundPrice = lowestPrice;
		System.out.println("everyRoundPrice = " + everyRoundPrice);
		while (everyRoundPrice <= highestPrice) {
			everyRoundPrice = (everyRoundPrice) * 1.05;
			everyRoundPrice = round2Numbers(everyRoundPrice);
			System.out.println("everyRoundPrice = " + everyRoundPrice);
			roundNumber++;
		}
		System.out.println("roundNumber = " + roundNumber);

	
	}

	private static double round2Numbers(double everyRoundPrice) {
		double d1 = everyRoundPrice;
		d1 = (Math.round(d1 * 100));
		everyRoundPrice = d1 / 100;
		return everyRoundPrice;
	}

}
